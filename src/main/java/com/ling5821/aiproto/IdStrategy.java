package com.ling5821.aiproto;

import com.ling5821.aiproto.annotation.Field;
import com.ling5821.aiproto.field.BasicField;
import com.ling5821.aiproto.schema.RuntimeSchema;
import io.netty.buffer.ByteBuf;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * Schema 加载策略
 *
 * @author lsj
 * @date 2021/1/22 10:32
 */
public abstract class IdStrategy {

    protected Map<Object, Schema> typeIdSchemaMapping = new HashMap<>(64);

    protected static <T> Schema<T> loadSchema(Map<Object, Schema> root, Class<T> typeClass) {
        Schema schema = root.get(typeClass.getName());
        //不支持循环引用
        if (schema != null) {
            return (Schema<T>)schema;
        }

        /*List<PropertyDescriptor> properties = findFieldProperties(typeClass);
        if (properties.isEmpty()) {
            return null;
        }*/

        List<BasicField> fieldList = findFields(root, typeClass);
        BasicField[] fields = fieldList.toArray(new BasicField[fieldList.size()]);
        Arrays.sort(fields);

        schema = new RuntimeSchema(typeClass, 0, fields);
        root.put(typeClass.getName(), schema);
        return (Schema<T>)schema;
    }

    /*protected static List<PropertyDescriptor> findFieldProperties(Class typeClass) {
        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(typeClass);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        PropertyDescriptor[] properties = beanInfo.getPropertyDescriptors();
        List<PropertyDescriptor> result = new ArrayList<>(properties.length);

        for (PropertyDescriptor property : properties) {
            Method readMethod = property.getReadMethod();

            if (readMethod != null) {
                if (readMethod.isAnnotationPresent(Field.class)) {
                    result.add(property);
                }
            }
        }
        return result;
    }*/

    protected static List<BasicField> findFields(Map<Object, Schema> root, Class<?> typeClass) {

        List<BasicField> fields = new ArrayList<>();

        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(typeClass);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }

        PropertyDescriptor[] properties = beanInfo.getPropertyDescriptors();

        for (PropertyDescriptor property : properties) {
            Method readMethod = property.getReadMethod();

            java.lang.reflect.Field declaredField = null;
            try {
                declaredField = typeClass.getDeclaredField(property.getName());
            } catch (NoSuchFieldException ignored) {
                //某个字段没取到不影响其他字段的读取
            }

            FieldProperty fieldProperty = new FieldProperty(declaredField, property);

            if (readMethod != null) {
                Field methodField = readMethod.getDeclaredAnnotation(Field.class);
                if (methodField != null) {
                    fillField(root, fields, fieldProperty, methodField);
                    continue;
                }
            }

            if (declaredField != null) {

                Field propertyField = declaredField.getDeclaredAnnotation(Field.class);
                if (propertyField != null) {
                    fillField(root, fields, fieldProperty, propertyField);
                }
            }
        }
        return fields;
    }

    protected static void fillField(Map<Object, Schema> root, List<BasicField> fields, FieldProperty fieldProperty,
        Field field) {
        Class typeClass = fieldProperty.getPropertyDescriptor().getPropertyType();
        Method readMethod = fieldProperty.getPropertyDescriptor().getReadMethod();

        BasicField value;

        if (field.type() == DataType.OBJ || field.type() == DataType.LIST) {
            if (Collection.class.isAssignableFrom(typeClass)) {
                typeClass = (Class)((ParameterizedType)readMethod.getGenericReturnType()).getActualTypeArguments()[0];
            }
            loadSchema(root, typeClass);
            Schema schema = root.get(typeClass.getName());
            value = FieldFactory.create(field, fieldProperty, schema);
            fields.add(value);
        } else {
            value = FieldFactory.create(field, fieldProperty);
            fields.add(value);
        }
    }

    public Object readFrom(Object typeId, ByteBuf input) {
        Schema schema = typeIdSchemaMapping.get(typeId);
        return schema.readFrom(input);
    }

    public void writeTo(Object typeId, ByteBuf output, Object element) {
        Schema schema = typeIdSchemaMapping.get(typeId);
        schema.writeTo(output, element);
    }

    public Schema getSchema(Object typeId) {
        Schema schema = typeIdSchemaMapping.get(typeId);
        return schema;
    }

    public abstract <T> Schema<T> getSchema(Class<T> typeClass);

    protected <T> Schema<T> loadSchema(Map<Object, Schema> root, Object typeId, Class<T> typeClass) {
        Schema<T> schema = typeIdSchemaMapping.get(typeId);
        if (schema == null) {
            schema = loadSchema(root, typeClass);
            typeIdSchemaMapping.put(typeId, schema);
        }
        return schema;
    }
}