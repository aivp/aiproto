package com.ling5821.aiproto;

import com.ling5821.aiproto.annotation.JsonMessage;
import com.ling5821.aiproto.schema.JsonSchema;
import com.ling5821.aiproto.util.ClassUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lsj
 * @date 2021/8/26 11:47
 */
public class JsonStrategy implements LoadStrategy {

    private Map<Object, Map<Integer, Schema<?>>> classSchemaMapping = new HashMap<>(140);

    private Map<Object, Map<Integer, Class<?>>> typeIdClassMapping = new HashMap<>(140);


    public JsonStrategy() {
    }

    public JsonStrategy(String basePackage) {
        List<Class<?>> types = ClassUtils.getClassList(basePackage);
        for (Class<?> type : types) {
            JsonMessage message = type.getAnnotation(JsonMessage.class);
            if (message != null) {
                String[] values = message.value();
                for (String typeId : values) {
                    loadSchema(classSchemaMapping, typeId, type);
                    loadClass(typeIdClassMapping, typeId, type);
                }
            }
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
