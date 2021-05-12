package com.ling5821.aiproto.convert;

import com.ling5821.aiproto.IdStrategy;
import com.ling5821.aiproto.Schema;
import com.ling5821.aiproto.converter.MapConverter;
import io.netty.buffer.ByteBuf;

public class AttributeConverter extends MapConverter<Integer, Object> {

    private static final IdStrategy INSTANCE = AttributeType.INSTANCE;

    @Override
    public Object convert(Integer key, ByteBuf input) {
        if (!input.isReadable())
            return null;
        Schema schema = INSTANCE.getSchema(key);
        if (schema != null)
            return INSTANCE.readFrom(key, input);
        byte[] bytes = new byte[input.readableBytes()];
        input.readBytes(bytes);
        return bytes;
    }

    @Override
    public void convert(Integer key, ByteBuf output, Object value) {
        Schema schema = INSTANCE.getSchema(key);
        if (schema != null) {
            schema.writeTo(output, value);
        }
    }

    @Override
    protected Integer readKey(ByteBuf input) {
        return (int)input.readUnsignedByte();
    }

    @Override
    protected void writeKey(ByteBuf output, Integer key) {
        output.writeByte(key);
    }

    @Override
    protected int valueSize() {
        return 1;
    }
}