package com.ling5821.aiproto.schema;

import com.ling5821.aiproto.Schema;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lsj
 * @date 2021/1/25 11:24
 */
public class ObjectSchema<T> implements Schema<T> {
    private static volatile Map<Object, ObjectSchema> cache = new HashMap<>();
    private final Schema<T> schema;

    public ObjectSchema(Schema<T> schema) {
        this.schema = schema;
    }

    public static Schema getInstance(Schema schema) {
        Object key = schema;
        ObjectSchema instance;
        if ((instance = cache.get(key)) == null) {
            synchronized (cache) {
                if ((instance = cache.get(key)) == null) {
                    instance = new ObjectSchema(schema);
                    cache.put(schema, instance);
                }
            }
        }
        return instance;
    }

    @Override
    public T readFrom(ByteBuf input) {
        return schema.readFrom(input);
    }

    @Override
    public T readFrom(ByteBuf input, int length) {
        if (length > 0) {
            input = input.readSlice(length);
        }
        return schema.readFrom(input);
    }

    @Override
    public void writeTo(ByteBuf output, T message) {
        schema.writeTo(output, message);
    }

    @Override
    public void writeTo(ByteBuf output, int length, T message) {
        schema.writeTo(output, length, message);
    }
}
