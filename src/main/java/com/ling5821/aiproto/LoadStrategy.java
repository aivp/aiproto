package com.ling5821.aiproto;

import com.ling5821.aiproto.constant.VersionConstant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Schema 加载策略
 *
 * @author lsj
 * @date 2021/1/22 10:32
 */
public interface LoadStrategy {

    Map<Object, Map<Integer, Schema<?>>> typeIdSchemaMapping = new ConcurrentHashMap<>(64);

    Map<Object, Map<Integer, Schema<?>>> getClassSchemaMapping();

    Map<Object, Map<Integer, Class<?>>> getTypeIdClassMapping();


    default <T> Map<Integer, Schema<T>> getSchema(Class<T> typeClass) {
        Map<Integer, Schema<?>> schemas = this.getClassSchemaMapping().get(typeClass.getName());
        if (schemas == null) {
            schemas = loadSchema(this.getClassSchemaMapping(), typeClass);
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

    default <T> Schema<T> getSchema(Class<T> typeClass, Integer version) {
        Map<Integer, Schema<?>> schemas = this.getClassSchemaMapping().get(typeClass.getName());
        if (schemas == null) {
            schemas = loadSchema(this.getClassSchemaMapping(), typeClass);
        }
        if (schemas == null) {
            return null;
        }
        return (Schema<T>)schemas.get(version);
    }

    default Schema<?> getSchema(Object typeId, Integer version) {
        Map<Integer, Schema<?>> schemaMap = typeIdSchemaMapping.get(typeId);
        if (schemaMap == null) {
            return null;
        }
        return schemaMap.get(version);
    }

    default Class<?> getClass(Object typeId, Integer version) {
        Map<Integer, Class<?>> classMap = this.getTypeIdClassMapping().get(typeId);
        if (classMap == null) {
            return null;
        }
        return classMap.get(version);
    }

    default void loadClass(Map<Object, Map<Integer, Class<?>>> root, Object typeId, Class<?> typeClass) {
        Map<Integer, Class<?>> classes = new HashMap<>(1);
        classes.put(VersionConstant.DEFAULT_VERSION, typeClass);
        root.put(typeId, classes);
    }

    default void loadSchema(Map<Object, Map<Integer, Schema<?>>> root, Object typeId, Class<?> typeClass) {
        Map<Integer, Schema<?>> schemas = typeIdSchemaMapping.get(typeId);
        if (schemas == null) {
            schemas = loadSchema(root, typeClass);
            typeIdSchemaMapping.put(typeId, schemas);
        }
    }

    Map<Integer, Schema<?>> loadSchema(Map<Object, Map<Integer, Schema<?>>> root,
        Class<?> typeClass);
}