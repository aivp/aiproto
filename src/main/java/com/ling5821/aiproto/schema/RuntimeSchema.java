package com.ling5821.aiproto.schema;

import com.ling5821.aiproto.Schema;
import com.ling5821.aiproto.field.BasicField;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.StringJoiner;

/**
 * @author lsj
 * @date 2021/1/25 11:25
 */
public class RuntimeSchema<T> implements Schema<T> {
    protected final int version;
    protected final int length;
    protected final Class<T> typeClass;
    protected final BasicField[] fields;
    protected final Constructor<T> constructor;

    public RuntimeSchema(Class<T> typeClass, int version, BasicField[] fields) {
        this.typeClass = typeClass;
        this.version = version;
        this.fields = fields;
        BasicField lastField = fields[fields.length - 1];
        int lastIndex = lastField.index();
        int lastLength = lastField.length() < 0 ? 256 : lastField.length();
        this.length = lastIndex + lastLength;
        try {
            this.constructor = typeClass.getDeclaredConstructor();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T readFrom(ByteBuf input) {
        BasicField field = null;
        try {
            T result = constructor.newInstance();
            for (int i = 0; i < fields.length; i++) {
                if (!input.isReadable()) {
                    break;
                }
                field = fields[i];
                field.readFrom(input, result);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Read failed " + typeClass.getName() + field, e);
        }
    }

    @Override
    public void writeTo(ByteBuf output, T message) {
        BasicField field = null;
        try {
            for (int i = 0; i < fields.length; i++) {
                field = fields[i];
                field.writeTo(output, message);
            }
        } catch (Exception e) {
            throw new RuntimeException("Write Failed " + typeClass.getName() + field, e);
        }
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", RuntimeSchema.class.getSimpleName() + "[", "]").add("version=" + version)
            .add("length=" + length).add("typeClass=" + typeClass).add("fields=" + Arrays.toString(fields))
            .add("constructor=" + constructor).toString();
    }
}
