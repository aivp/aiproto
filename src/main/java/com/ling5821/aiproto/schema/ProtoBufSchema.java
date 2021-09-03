package com.ling5821.aiproto.schema;

import com.ling5821.aiproto.Schema;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lsj
 * @date 2021/9/1 10:03
 */
public class ProtoBufSchema<T> implements Schema<T> {

    private static volatile Map<Object, ProtoBufSchema<?>> CACHE = new HashMap<>();

    protected final Class<T> typeClass;

    public ProtoBufSchema(Class<T> typeClass) {
        this.typeClass = typeClass;
    }

    public static Schema getInstance(Class<?> typeClass) {
        ProtoBufSchema<?> instance;
        if ((instance = CACHE.get(typeClass)) == null) {
            synchronized (CACHE) {
                if ((instance = CACHE.get(typeClass)) == null) {
                    instance = new ProtoBufSchema<>(typeClass);
                    CACHE.put(typeClass, instance);
                }
            }
        }
        return instance;
    }

    @Override
    public T readFrom(ByteBuf input) {

        try {
            int len = input.readableBytes();
            byte[] bytes = new byte[len];
            input.readBytes(bytes);
            Method parseFrom = typeClass.getMethod("parseFrom", byte[].class);
            return (T)parseFrom.invoke(null, bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeTo(ByteBuf output, T message) {
        try {
            byte[] bytes = (byte[])message.getClass().getMethod("toByteArray").invoke(message);
            output.writeBytes(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
