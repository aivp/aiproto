package com.ling5821.aiproto;

import com.ling5821.aiproto.annotation.Convert;
import com.ling5821.aiproto.annotation.Field;
import com.ling5821.aiproto.field.BasicField;
import com.ling5821.aiproto.field.DynamicLengthField;
import com.ling5821.aiproto.field.FixedField;
import com.ling5821.aiproto.field.FixedLengthField;
import com.ling5821.aiproto.schema.*;

import java.beans.PropertyDescriptor;
import java.nio.ByteBuffer;

/**
 * FieldFactory
 *
 * @author lsj
 * @date 2021/3/24 16:22
 */
public abstract class FieldFactory {
    public static boolean EXPLAIN = false;

    public static BasicField create(Field field, FieldProperty fieldProperty) {
        return create(field, fieldProperty, null);
    }

    public static BasicField create(Field field, FieldProperty fieldProperty, Schema schema) {
        DataType dataType = field.type();
        PropertyDescriptor property = fieldProperty.getPropertyDescriptor();
        Class<?> typeClass = property.getPropertyType();

        Schema fieldSchema;
        switch (dataType) {
            case BYTE:
            case UNSIGNED_BYTE:
            case UNSIGNED_BYTE_INT:
            case SHORT:
            case SHORT_LE:
            case UNSIGNED_SHORT:
            case UNSIGNED_SHORT_LE:
            case INT:
            case INT_LE:
            case UNSIGNED_INT:
            case UNSIGNED_INT_LE:
            case LONG:
            case LONG_LE:
            case FLOAT:
            case FLOAT_LE:
            case DOUBLE:
            case DOUBLE_LE:
                if (typeClass.isArray()) {
                    fieldSchema = ArraySchema.getSchema(dataType);
                } else {
                    fieldSchema = NumberSchema.getSchema(dataType);
                }
                break;
            case BYTES:
                if (String.class.isAssignableFrom(typeClass)) {
                    fieldSchema = StringSchema.Chars.getInstance(field.pad(), field.charset());
                } else if (ByteBuffer.class.isAssignableFrom(typeClass)) {
                    fieldSchema = ByteBufferSchema.INSTANCE;
                } else {
                    fieldSchema = ArraySchema.ByteArraySchema.INSTANCE;
                }
                break;
            case STRING:
                fieldSchema = StringSchema.Chars.getInstance(field.pad(), field.charset());
                break;
            case OBJ:
                if (schema != null) {
                    fieldSchema = ObjectSchema.getInstance(schema);
                } else {
                    Convert convert = property.getReadMethod().getAnnotation(Convert.class);
                    if (fieldProperty.getDeclaredField() != null) {
                        convert = fieldProperty.getDeclaredField().getAnnotation(Convert.class);
                    }
                    fieldSchema = ConvertSchema.getInstance(convert.converter());
                }
                break;
            case LIST:
                fieldSchema = CollectionSchema.getInstance(schema);
                break;
            case MAP:
                Convert convert = property.getReadMethod().getAnnotation(Convert.class);
                if (fieldProperty.getDeclaredField() != null) {
                    convert = fieldProperty.getDeclaredField().getAnnotation(Convert.class);
                }
                fieldSchema = ConvertSchema.getInstance(convert.converter());
                break;
            default:
                throw new RuntimeException("不支持的类型转换");
        }

        BasicField result;
        if (EXPLAIN) {
            if (field.lengthSize() > 0) {
                result = new DynamicLengthField.Logger(field, fieldProperty.getPropertyDescriptor(), fieldSchema);
            } else if (field.length() > 0) {
                result = new FixedLengthField.Logger(field, property, fieldSchema);
            } else {
                result = new FixedField.Logger(field, property, fieldSchema);
            }
        } else {
            if (field.lengthSize() > 0) {
                result = new DynamicLengthField(field, property, fieldSchema);
            } else if (field.length() > 0) {
                result = new FixedLengthField(field, property, fieldSchema);
            } else {
                result = new FixedField(field, property, fieldSchema);
            }
        }
        return result;
    }
}