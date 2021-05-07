package com.ling5821.aiproto.schema;

import com.ling5821.aiproto.Schema;
import com.ling5821.aiproto.converter.Converter;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lsj
 * @date 2021/1/25 10:09
 */
public class ConvertSchema<T> implements Schema<T> {

    private static volatile Map<Object, ConvertSchema> cache = new HashMap<>();
    private final Converter<T> converter;

    public ConvertSchema(Converter<T> converter) {
        this.converter = converter;
    }

    public static Schema getInstance(Class<? extends Converter> clazz) {
        String key = clazz.getName();
        ConvertSchema instance;
        if ((instance = cache.get(key)) == null) {
            synchronized (cache) {
                if ((instance = cache.get(key)) == null) {
                    try {
                        Converter converter = clazz.newInstance();
                        instance = new ConvertSchema(converter);
                        cache.put(key, instance);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return instance;
    }

    @Override
    public T readFrom(ByteBuf input) {
        return converter.convert(input);
    }

    @Override
    public T readFrom(ByteBuf input, int length) {
        if (length > 0) {
            input = input.readSlice(length);
        }
        return converter.convert(input);
    }

    @Override
    public void writeTo(ByteBuf output, T message) {
        converter.convert(output, message);
    }

    @Override
    public void writeTo(ByteBuf output, int length, T message) {
        converter.convert(output, message);
    }
}
