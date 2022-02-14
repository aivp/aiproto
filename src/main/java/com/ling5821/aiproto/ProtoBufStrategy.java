package com.ling5821.aiproto;

import com.google.protobuf.*;
import com.ling5821.aiproto.constant.VersionConstant;
import com.ling5821.aiproto.schema.ProtoBufSchema;
import com.ling5821.aiproto.util.ClassUtils;
import com.ling5821.aiproto.util.StrUtils;

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
public class ProtoBufStrategy implements LoadStrategy {

    private Map<Object, Map<Integer, Schema<?>>> classSchemaMapping = new HashMap<>(140);

    private Map<Object, Map<Integer, Class<?>>> typeIdClassMapping = new HashMap<>(140);

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
            if (javaPackage == null || javaPackage.length() == 0) {
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
                            loadSchema(classSchemaMapping, messageTypeId.toStringUtf8(), messageClass);
                            loadClass(typeIdClassMapping, messageTypeId.toStringUtf8(), messageClass);
                        });
                    } catch (ClassNotFoundException ignored) {
                        //加载失败
                    }
                }
            });
        }
    }

    @Override
    public void loadSchema(Map<Object, Map<Integer, Schema<?>>> root, Object typeId, Class<?> typeClass) {
        Map<Integer, Schema<?>> schemas = typeIdSchemaMapping.get(typeId);
        if (schemas == null) {
            schemas = loadSchema(root, typeClass);
            typeIdSchemaMapping.put(typeId, schemas);
        } else {
            schemas.putAll(loadMessageSchemas(typeClass));
        }
    }

    @Override
    public Map<Integer, Schema<?>> loadSchema(Map<Object, Map<Integer, Schema<?>>> root, Class<?> typeClass) {
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
    public Map<Object, Map<Integer, Schema<?>>> getClassSchemaMapping() {
        return classSchemaMapping;
    }

    @Override
    public Map<Object, Map<Integer, Class<?>>> getTypeIdClassMapping() {
        return typeIdClassMapping;
    }

    private Map<Integer, Schema<?>> loadMessageSchemas(Class<?> typeClass) {
        Map<Integer, Schema<?>> schemas = new HashMap<>(1);
        schemas.put(VersionConstant.DEFAULT_VERSION, new ProtoBufSchema<>(typeClass));
        return schemas;
    }
}
