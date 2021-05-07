package com.ling5821.aiproto.field;

import com.ling5821.aiproto.Schema;
import com.ling5821.aiproto.annotation.Field;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

import java.beans.PropertyDescriptor;

/**
 * @author lsj
 * @date 2021/1/25 19:49
 */
public class FixedField<T> extends BasicField<T> {

    protected final Schema<T> schema;

    public FixedField(Field field, PropertyDescriptor property, Schema<T> schema) {
        super(field, property);
        this.schema = schema;
    }

    @Override
    public boolean readFrom(ByteBuf input, Object message) throws Exception {
        Object value = schema.readFrom(input);
        writeMethod.invoke(message, value);
        return true;
    }

    @Override
    public void writeTo(ByteBuf output, Object message) throws Exception {
        Object value = readMethod.invoke(message);
        if (value != null) {
            schema.writeTo(output, (T)value);
        }
    }

    public static class Logger<T> extends FixedField<T> {

        public Logger(Field field, PropertyDescriptor property, Schema<T> schema) {
            super(field, property, schema);
        }

        @Override
        public boolean readFrom(ByteBuf input, Object message) throws Exception {
            int before = input.readerIndex();

            Object value = schema.readFrom(input);
            writeMethod.invoke(message, value);

            int after = input.readerIndex();
            String hex = ByteBufUtil.hexDump(input.slice(before, after - before));
            println(this.index, this.desc, hex, value);
            return true;
        }

        @Override
        public void writeTo(ByteBuf output, Object message) throws Exception {
            int before = output.writerIndex();

            Object value = readMethod.invoke(message);
            if (value != null) {
                schema.writeTo(output, (T)value);
            }

            int after = output.writerIndex();
            String hex = ByteBufUtil.hexDump(output.slice(before, after - before));
            println(this.index, this.desc, hex, value);
        }
    }
}