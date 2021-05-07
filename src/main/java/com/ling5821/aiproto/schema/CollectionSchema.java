package com.ling5821.aiproto.schema;

import com.ling5821.aiproto.Schema;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lsj
 * @date 2021/1/25 10:41
 */
public class CollectionSchema<T> implements Schema<List<T>> {
    private static volatile Map<Object, CollectionSchema> cache = new HashMap<>();

    private final Schema<T> schema;

    public CollectionSchema(Schema<T> schema) {
        this.schema = schema;
    }

    public static Schema<List> getInstance(Schema schema) {
        Object key = schema;
        CollectionSchema instance;
        if ((instance = cache.get(key)) == null) {
            synchronized (cache) {
                if ((instance = cache.get(key)) == null) {
                    instance = new CollectionSchema(schema);
                    cache.put(schema, instance);
                }
            }
        }
        return instance;
    }

    @Override
    public List<T> readFrom(ByteBuf input) {
        List<T> list = new ArrayList<>();
        if (!input.isReadable()) {
            return list;
        }
        do {
            T obj = schema.readFrom(input);
            if (obj == null) {
                break;
            }
            list.add(obj);
        } while (input.isReadable());
        return list;
    }

    @Override
    public List<T> readFrom(ByteBuf input, int length) {
        return this.readFrom(input.readSlice(length));
    }

    @Override
    public void writeTo(ByteBuf output, List<T> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        for (T obj : list) {
            schema.writeTo(output, obj);
        }
    }

    @Override
    public void writeTo(ByteBuf output, int length, List<T> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        for (T obj : list) {
            schema.writeTo(output, length, obj);
        }

    }
}
