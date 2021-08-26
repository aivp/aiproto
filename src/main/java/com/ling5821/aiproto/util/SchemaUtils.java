package com.ling5821.aiproto.util;

import com.ling5821.aiproto.DefaultLoadStrategy;
import com.ling5821.aiproto.JsonStrategy;
import com.ling5821.aiproto.LoadStrategy;
import com.ling5821.aiproto.Schema;

import java.util.Map;

/**
 * SchemaUtils
 *
 * @author lsj
 * @date 2021/3/24 16:22
 */
public class SchemaUtils {

    private static volatile boolean Initial = false;

    private static LoadStrategy LOAD_STRATEGY = new DefaultLoadStrategy();
    private static LoadStrategy JSON_STRATEGY = new JsonStrategy();

    public static void initial(String basePackage) {
        if (!Initial) {
            synchronized (SchemaUtils.class) {
                if (!Initial) {
                    Initial = true;
                    LOAD_STRATEGY = new DefaultLoadStrategy(basePackage);
                    JSON_STRATEGY = new JsonStrategy(basePackage);
                }
            }
        }
    }

    public static Schema getSchema(Object typeId, Integer version) {
        return LOAD_STRATEGY.getSchema(typeId, version);
    }

    public static Schema getJsonSchema(Object typeId, Integer version) {
        return JSON_STRATEGY.getSchema(typeId, version);
    }

    public static Schema getSchema(Class<?> typeClass, Integer version) {
        return LOAD_STRATEGY.getSchema(typeClass, version);
    }

    public static Schema getJsonSchema(Class<?> typeClass, Integer version) {
        return JSON_STRATEGY.getSchema(typeClass, version);
    }

    public static <T> Map<Integer, Schema<T>> getSchema(Class<T> typeClass) {
        return LOAD_STRATEGY.getSchema(typeClass);
    }

    public static <T> Map<Integer, Schema<T>> getJsonSchema(Class<T> typeClass) {
        return JSON_STRATEGY.getSchema(typeClass);
    }
}