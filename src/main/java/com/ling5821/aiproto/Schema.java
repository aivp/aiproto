package com.ling5821.aiproto;

import io.netty.buffer.ByteBuf;

/**
 * @author lsj
 * @date 2021/1/22 10:16
 */
public interface Schema<T> {

    T readFrom(ByteBuf input);

    void writeTo(ByteBuf output, T message);

    default T readFrom(ByteBuf input, int length) {
        return readFrom(input);
    }

    default void writeTo(ByteBuf output, int length, T message) {
        writeTo(output, message);
    }

    default int length() {
        return 128;
    }
}
