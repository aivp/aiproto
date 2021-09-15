package com.ling5821.aiproto.util;

import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.GeneratedMessage;
import com.ling5821.aiproto.*;

import java.util.Map;

/**
 * SchemaUtils
 *
 * @author lsj
 * @date 2021/3/24 16:22
 */
public class SchemaUtils {

    private static volatile boolean Initial = false;
    private static volatile boolean InitialJson = false;
    private static volatile boolean InitialProtoBuf = false;

    private static LoadStrategy LOAD_STRATEGY = new DefaultLoadStrategy();
    private static LoadStrategy JSON_STRATEGY = new JsonStrategy();
    private static LoadStrategy PROTO_BUF_STRATEGY = new ProtoBufStrategy();

    public static void initial(String basePackage) {
        if (!Initial) {
            synchronized (SchemaUtils.class) {
                if (!Initial) {
                    Initial = true;
                    LOAD_STRATEGY = new DefaultLoadStrategy(basePackage);
                }
            }
        }
    }

    public static void initialJson(String basePackage) {
        if (!InitialJson) {
            synchronized (SchemaUtils.class) {
                if (!InitialJson) {
                    InitialProtoBuf = true;
                    JSON_STRATEGY = new JsonStrategy(basePackage);
                }
            }
        }
    }

    public static void initialProtoBuf(String basePackage, GeneratedMessage.GeneratedExtension<DescriptorProtos.MessageOptions, ?> extension, String messageTypeFieldName) {
        if (!InitialProtoBuf) {
            synchronized (SchemaUtils.class) {
                if (!InitialProtoBuf) {
                    InitialProtoBuf = true;
                    PROTO_BUF_STRATEGY = new ProtoBufStrategy(basePackage, extension, messageTypeFieldName);
                }
            }
        }

    }

    public static Schema getSchema(Object typeId, Integer version) {
        return LOAD_STRATEGY.getSchema(typeId, version);
    }

    public static Schema getSchema(Class<?> typeClass, Integer version) {
        return LOAD_STRATEGY.getSchema(typeClass, version);
    }

    public static <T> Map<Integer, Schema<T>> getSchema(Class<T> typeClass) {
        return LOAD_STRATEGY.getSchema(typeClass);
    }

    public static Schema getJsonSchema(Object typeId, Integer version) {
        return JSON_STRATEGY.getSchema(typeId, version);
    }

    public static Schema getJsonSchema(Class<?> typeClass, Integer version) {
        return JSON_STRATEGY.getSchema(typeClass, version);
    }

    public static <T> Map<Integer, Schema<T>> getJsonSchema(Class<T> typeClass) {
        return JSON_STRATEGY.getSchema(typeClass);
    }

    public static Schema getProtoBufSchema(Object typeId, Integer version) {
        return PROTO_BUF_STRATEGY.getSchema(typeId, version);
    }

    public static Schema getProtoBufSchema(Class<?> typeClass, Integer version) {
        return PROTO_BUF_STRATEGY.getSchema(typeClass, version);
    }

    public static <T> Map<Integer, Schema<T>> getProtoBufSchema(Class<T> typeClass) {
        return PROTO_BUF_STRATEGY.getSchema(typeClass);
    }
}