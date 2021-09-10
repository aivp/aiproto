package com.ling5821.aiproto;

import com.google.protobuf.*;
import com.ling5821.aiproto.schema.ProtoBufSchema;
import com.ling5821.aiproto.util.ClassUtils;
import com.ling5821.aiproto.util.StrUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lsj
 * @date 2021/8/26 11:47
 */
public class ProtoBufStrategy extends LoadStrategy {

    private Map<String, Map<Integer, Schema<?>>> typeClassMapping = new HashMap(140);

    public ProtoBufStrategy() {
    }

    public ProtoBufStrategy(String basePackage, GeneratedMessage.GeneratedExtension<DescriptorProtos.MessageOptions, ?> extension, String messageTypeFieldName) {
        List<Class<?>> types = ClassUtils.getClassList(basePackage);
        for (Class<?> type : types) {
            Method getDescriptor;
            try {
                getDescriptor = type.getDeclaredMethod("getDescriptor");
            } catch (NoSuchMethodException e) {
                continue;
            }

            Descriptors.FileDescriptor fileDescriptor;

            try {
                fileDescriptor = (Descriptors.FileDescriptor)getDescriptor.invoke(null);
            } catch (Exception ignored) {
                continue;
            }
            boolean javaMultipleFiles = fileDescriptor.getOptions().getJavaMultipleFiles();
            String javaPackage = fileDescriptor.getOptions().getJavaPackage();
            if (StringUtils.isEmpty(javaPackage)) {
                // 如果不包含Java的包直接跳过
                continue;
            }
            List<Descriptors.Descriptor> messageTypes = fileDescriptor.getMessageTypes();
            messageTypes.stream().collect(Collectors.toMap(Descriptors.Descriptor::getName, messageType -> {
                Object extensionInstance = messageType.getOptions().getExtension(extension);
                try {
                    return ((ProtocolStringList)extensionInstance.getClass().getMethod("get" + StrUtils.upperCaseFirst(messageTypeFieldName) + "List").invoke(extensionInstance)).asByteStringList();
                } catch (Exception e) {
                    return new ArrayList<ByteString>();
                }
            })).forEach((messageName, messageTypeIdList) -> {
                if (messageTypeIdList.size() > 0) {

                    String className =
                        javaMultipleFiles ? javaPackage + "." + messageName : type.getName() + "$" + messageName;
                    try {
                        Class<?> messageClass = Class.forName(className);
                        messageTypeIdList.forEach(messageTypeId -> {
                            loadSchema(typeClassMapping, messageTypeId.toStringUtf8(), messageClass);
                        });
                    } catch (ClassNotFoundException ignored) {
                        //加载失败
                    }
                }
            });
        }
    }

    @Override
    protected void loadSchema(Map<String, Map<Integer, Schema<?>>> root, Object typeId, Class<?> typeClass) {
        Map<Integer, Schema<?>> schemas = typeIdMapping.get(typeId);
        if (schemas == null) {
            schemas = loadSchema(root, typeClass);
            typeIdMapping.put(typeId, schemas);
        } else {
            schemas.putAll(loadMessageSchemas(typeClass));
        }
    }

    @Override
    protected Map<Integer, Schema<?>> loadSchema(Map<String, Map<Integer, Schema<?>>> root, Class<?> typeClass) {
        Map<Integer, Schema<?>> schemas = root.get(typeClass.getName());
        //不支持循环引用
        if (schemas != null) {
            return schemas;
        }


        schemas = loadMessageSchemas(typeClass);
        root.put(typeClass.getName(), schemas);
        return schemas;
    }

    @Override
    public <T> Schema<T> getSchema(Class<T> typeClass, Integer version) {
        Map<Integer, Schema<?>> schemas = typeClassMapping.get(typeClass.getName());
        if (schemas == null) {
            schemas = loadSchema(typeClassMapping, typeClass);
        }
        if (schemas == null) {
            return null;
        }
        return (Schema<T>)schemas.get(version);
    }

    @Override
    public <T> Map<Integer, Schema<T>> getSchema(Class<T> typeClass) {
        Map<Integer, Schema<?>> schemas = typeClassMapping.get(typeClass.getName());
        if (schemas == null) {
            schemas = loadSchema(typeClassMapping, typeClass);
        }
        if (schemas == null) {
            return null;
        }

        HashMap<Integer, Schema<T>> result = new HashMap<>(schemas.size());
        for (Map.Entry<Integer, Schema<?>> entry : schemas.entrySet()) {
            result.put(entry.getKey(), (Schema<T>)entry.getValue());
        }
        return result;
    }

    private Map<Integer, Schema<?>> loadMessageSchemas(Class<?> typeClass) {
        Map<Integer, Schema<?>> schemas = new HashMap<>(1);
        schemas.put(1, new ProtoBufSchema<>(typeClass));
        return schemas;
    }
}
