package com.ling5821.aiproto;

import com.ling5821.aiproto.annotation.JsonMessage;
import com.ling5821.aiproto.schema.JsonSchema;
import com.ling5821.aiproto.util.ClassUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author aidong
 * @date 2021/8/26 11:47
 */
public class JsonStrategy extends LoadStrategy {

    private Map<String, Map<Integer, Schema<?>>> typeClassMapping = new HashMap(140);

    public JsonStrategy() {
    }

    public JsonStrategy(String basePackage) {
        List<Class<?>> types = ClassUtils.getClassList(basePackage);
        for (Class<?> type : types) {
            JsonMessage message = type.getAnnotation(JsonMessage.class);
            if (message != null) {
                String[] values = message.value();
                for (String typeId : values) {
                    loadSchema(typeClassMapping, typeId, type);
                }
            }
        }
    }

    @Override
    protected void loadSchema(Map<String, Map<Integer, Schema<?>>> root, Object typeId, Class<?> typeClass) {
        Map<Integer, Schema<?>> schemas = typeIdMapping.get(typeId);
        if (schemas == null) {
            schemas = loadSchema(root, typeClass);
            typeIdMapping.put(typeId, schemas);
        } else {
            schemas.putAll(loadJsonSchemas(typeClass));
        }
    }

    @Override
    protected Map<Integer, Schema<?>> loadSchema(Map<String, Map<Integer, Schema<?>>> root, Class<?> typeClass) {
        Map<Integer, Schema<?>> schemas = root.get(typeClass.getName());
        //不支持循环引用
        if (schemas != null) {
            return schemas;
        }


        schemas = loadJsonSchemas(typeClass);
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

    private Map<Integer, Schema<?>> loadJsonSchemas(Class<?> typeClass) {
        Map<Integer, Schema<?>> schemas = new HashMap<>(4);
        JsonMessage message = typeClass.getAnnotation(JsonMessage.class);
        if (message != null) {
            for (Integer version : message.version()) {
                schemas.put(version, new JsonSchema<>(typeClass, version));
            }
        }
        return schemas;
    }
}
