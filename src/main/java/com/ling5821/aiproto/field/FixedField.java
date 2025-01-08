package com.ling5821.aiproto.field;

import com.ling5821.aiproto.Schema;
import com.ling5821.aiproto.annotation.Field;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

import java.beans.PropertyDescriptor;
import java.util.Objects;

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

        try {
            if (value != null) {
                // 针对字段类型与读取值不一致进行转换
                if (!fieldType.isInstance(value)) {
                    // 待设置字段类型为数字，当前读取值value也是数字，进行数字之间的转换
                    if (value instanceof Number) {
                        // 根据预估的类型占比优先级进行优先判断
                        if (Integer.class.equals(fieldType) || int.class.equals(fieldType)) {
                            value = ((Number) value).intValue();
                        } else if (Long.class.equals(fieldType) || long.class.equals(fieldType)) {
                            value = ((Number) value).longValue();
                        } else if (Double.class.equals(fieldType) || double.class.equals(fieldType)) {
                            value = ((Number) value).doubleValue();
                        } else if (Float.class.equals(fieldType) || float.class.equals(fieldType)) {
                            value = ((Number) value).floatValue();
                        } else if (Short.class.equals(fieldType) || short.class.equals(fieldType)) {
                            value = ((Number) value).shortValue();
                        } else if (Byte.class.equals(fieldType) || byte.class.equals(fieldType)) {
                            value = ((Number) value).byteValue();
                        } else {
                            value = null;
                        }
                    } else if (String.class.isAssignableFrom(fieldType)) {
                        // 其他类型转换为String
                        value = value.toString();
                    } else if (Boolean.class.equals(fieldType) || boolean.class.equals(fieldType)) {
                        // 布尔类型转换
                        if (value instanceof Number) {
                            value = Objects.equals(value, 0);
                        } else if (value instanceof String) {
                            value = Boolean.parseBoolean((String) value);
                        }
                    } else {
                        // 其他无法处理的状况
                        value = null;
                    }
                }
            }

            // 基础类型设置默认值
            if (Objects.isNull(value) && fieldType.isPrimitive()) {
                if (int.class.equals(fieldType)) {
                    value = 0;
                } else if (long.class.equals(fieldType)) {
                    value = 0L;
                } else if (double.class.equals(fieldType)) {
                    value = 0d;
                } else if (float.class.equals(fieldType)) {
                    value = 0f;
                } else if (short.class.equals(fieldType)) {
                    value = (short) 0;
                } else if (byte.class.equals(fieldType)) {
                    value = (byte) 0;
                } else if (boolean.class.equals(fieldType)) {
                    value = false;
                } else if (char.class.equals(fieldType)) {
                    value = ' ';
                }
            }

        } catch (Exception e) {
            // TODO 转换失败的情况
        }

        writeMethod.invoke(message, value);

        return true;
    }

    @Override
    public void writeTo(ByteBuf output, Object message) throws Exception {
        Object value = readMethod.invoke(message);
        if (value != null) {
            schema.writeTo(output, (T) value);
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
                schema.writeTo(output, (T) value);
            }

            int after = output.writerIndex();
            String hex = ByteBufUtil.hexDump(output.slice(before, after - before));
            println(this.index, this.desc, hex, value);
        }
    }
}