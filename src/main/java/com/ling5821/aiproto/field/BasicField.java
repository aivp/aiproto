package com.ling5821.aiproto.field;

import com.ling5821.aiproto.annotation.Field;
import com.ling5821.aiproto.util.StrUtils;
import io.netty.buffer.ByteBuf;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.StringJoiner;

/**
 * @author lsj
 * @date 2021/1/25 12:15
 */
public abstract class BasicField<T> implements Comparable<BasicField<T>> {

    protected final int index;
    protected final int length;
    protected final String desc;
    protected final Method readMethod;
    protected final Method writeMethod;
    protected final PropertyDescriptor property;
    protected final Field field;
    protected Class<?> fieldType;

    public BasicField(Field field, PropertyDescriptor property) {
        this.index = field.index();
        this.length = field.length();
        this.desc = field.desc();
        this.readMethod = property.getReadMethod();
        this.writeMethod = property.getWriteMethod();
        this.property = property;
        this.field = field;
        this.fieldType = property.getPropertyType();
    }

    public abstract boolean readFrom(ByteBuf input, Object message) throws Exception;

    public abstract void writeTo(ByteBuf output, Object message) throws Exception;

    public int index() {
        return index;
    }

    public int length() {
        return length;
    }

    public void println(int index, String desc, String hex, Object value) {
        if (value != null) {
            System.out.println(index + "\t" + "[" + hex + "] " + desc + ": " + StrUtils.toString(value));
        }
    }

    @Override
    public int compareTo(BasicField<T> that) {
        return Integer.compare(this.index, that.index);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BasicField.class.getSimpleName() + "[", "]").add("index=" + index)
            .add("length=" + length).add("desc='" + desc + "'").add("readMethod=" + readMethod)
            .add("writeMethod=" + writeMethod).add("property=" + property).add("field=" + field).toString();
    }
}
