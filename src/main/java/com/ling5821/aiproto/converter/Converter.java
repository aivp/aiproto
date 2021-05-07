package com.ling5821.aiproto.converter;

import io.netty.buffer.ByteBuf;

/**
 * @author lsj
 * @date 2021/1/25 10:24
 */
public interface Converter<T> {

    T convert(ByteBuf input);

    void convert(ByteBuf output, T value);
}
