package com.ling5821.aiproto.schema;

import com.ling5821.aiproto.Schema;
import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;

/**
 * @author lsj
 * @date 2021/1/25 11:33
 */
public class ByteBufferSchema implements Schema<ByteBuffer> {
    public static final Schema INSTANCE = new ByteBufferSchema();

    @Override
    public ByteBuffer readFrom(ByteBuf input) {
        ByteBuffer buffer = input.nioBuffer();
        input.skipBytes(input.readableBytes());
        return buffer;
    }

    @Override
    public ByteBuffer readFrom(ByteBuf input, int length) {
        if (length < 0) {
            length = input.readableBytes();
        }
        ByteBuffer buffer = input.nioBuffer(input.readerIndex(), length);
        input.skipBytes(length);
        return buffer;
    }

    @Override
    public void writeTo(ByteBuf output, ByteBuffer message) {
        output.writeBytes(message);
    }

    @Override
    public void writeTo(ByteBuf output, int length, ByteBuffer message) {
        if (length > 0) {
            message.position(message.limit() - length);
        }
        output.writeBytes(message);
    }
}
