package com.ling5821.aiproto.schema;

import com.alibaba.fastjson.JSON;
import com.ling5821.aiproto.Schema;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;

/**
 * @author aidong
 * @date 2021/8/26 14:13
 */
public class JsonSchema<T> implements Schema<T> {
    protected final Class<T> typeClass;
    protected final int version;

    public JsonSchema(Class<T> typeClass, int version) {
        this.typeClass = typeClass;
        this.version = version;
    }

    @Override
    public T readFrom(ByteBuf input) {
        int len = input.readableBytes();
        byte[] bytes = new byte[len];
        input.readBytes(bytes);
        String json = new String(bytes, StandardCharsets.UTF_8);
        return JSON.parseObject(json, typeClass);
    }

    @Override
    public void writeTo(ByteBuf output, T message) {
        byte[] bytes = JSON.toJSONBytes(message);
        output.writeBytes(bytes);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", JsonSchema.class.getSimpleName() + "[", "]").add("version=" + version)
            .add("typeClass=" + typeClass).toString();
    }
}
