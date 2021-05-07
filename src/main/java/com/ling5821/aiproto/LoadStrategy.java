package com.ling5821.aiproto;

import com.ling5821.aiproto.annotation.Field;
import com.ling5821.aiproto.annotation.Fields;
import com.ling5821.aiproto.field.BasicField;
import com.ling5821.aiproto.schema.RuntimeSchema;

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
public abstract class LoadStrategy {

    protected Map<Object, Map<Integer, Schema<?>>> typeIdMapping = new HashMap<>(64);

    public abstract <T> Map<Integer, Schema<T>> getSchema(Class<T> typeClass);

    public abstract <T> Schema<T> getSchema(Class<T> typeClass, Integer version);

    public Schema getSchema(Object typeId, Integer version) {
        Map<Integer, Schema<?>> schemaMap = typeIdMapping.get(typeId);
        if (schemaMap == null) {
            return null;
        }
        return schemaMap.get(version);
    }

    protected void loadSchema(Map<String, Map<Integer, Schema<?>>> root, Object typeId, Class<?> typeClass) {
        Map<Integer, Schema<?>> schemas = typeIdMapping.get(typeId);
        if (schemas == null) {
            schemas = loadSchema(root, typeClass);
            typeIdMapping.put(typeId, schemas);
        }
    }

    protected Map<Integer, Schema<?>> loadSchema(Map<String, Map<Integer, Schema<?>>> root, Class<?> typeClass) {
        Map<Integer, Schema<?>> schemas = root.get(typeClass.getName());
        //不支持循环引用
        if (schemas != null) {
            return schemas;
        }

        /*List<PropertyDescriptor> properties = findFieldProperties(typeClass);
        if (properties.isEmpty()) {
            return null;
        }*/

        root.put(typeClass.getName(), schemas = new HashMap<>(4));

        Map<Integer, List<BasicField>> multiVersionFields = findMultiVersionFields(root, typeClass);
        for (Map.Entry<Integer, List<BasicField>> entry : multiVersionFields.entrySet()) {

            Integer version = entry.getKey();
            List<BasicField> fieldList = entry.getValue();

            BasicField[] fields = fieldList.toArray(new BasicField[fieldList.size()]);
            Arrays.sort(fields);

            Schema schema = new RuntimeSchema<>(typeClass, version, fields);
            schemas.put(version, schema);
        }
        return schemas;
    }

    /*protected List<PropertyDescriptor> findFieldProperties(Class<?> typeClass) {
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
                if (readMethod.isAnnotationPresent(Fields.class) || readMethod.isAnnotationPresent(Field.class)) {
                    result.add(property);
                }
            }
        }
        return result;
    }*/

    protected Map<Integer, List<BasicField>> findMultiVersionFields(Map<String, Map<Integer, Schema<?>>> root,
        Class<?> typeClass) {
        Map<Integer, List<BasicField>> multiVersionFields = new TreeMap<Integer, List<BasicField>>() {
            @Override
            public List<BasicField> get(Object key) {
                List<BasicField> result = super.get(key);
                if (result == null) {
                    super.put((Integer)key, result = new ArrayList<>());
                }
                return result;
            }
        };

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
                Fields methodFields = readMethod.getDeclaredAnnotation(Fields.class);
                if (methodField != null) {
                    fillField(root, multiVersionFields, fieldProperty, methodField);
                    continue;
                } else if (methodFields != null) {
                    for (Field field : methodFields.value()) {
                        fillField(root, multiVersionFields, fieldProperty, field);
                    }
                    continue;
                }
            }

            if (declaredField != null) {

                Field propertyField = declaredField.getDeclaredAnnotation(Field.class);
                Fields propertyFields = declaredField.getDeclaredAnnotation(Fields.class);
                if (propertyField != null) {
                    fillField(root, multiVersionFields, fieldProperty, propertyField);
                } else if (propertyFields != null) {
                    for (Field field : propertyFields.value()) {
                        fillField(root, multiVersionFields, fieldProperty, field);
                    }
                }
            }
        }
        return multiVersionFields;
    }

    protected void fillField(Map<String, Map<Integer, Schema<?>>> root,
        Map<Integer, List<BasicField>> multiVersionFields, FieldProperty fieldProperty, Field field) {
        Class<?> typeClass = fieldProperty.getPropertyDescriptor().getPropertyType();
        Method readMethod = fieldProperty.getPropertyDescriptor().getReadMethod();

        BasicField value;
        int[] versions = field.version();

        if (field.type() == DataType.OBJ || field.type() == DataType.LIST) {
            if (Collection.class.isAssignableFrom(typeClass)) {
                typeClass =
                    (Class<?>)((ParameterizedType)readMethod.getGenericReturnType()).getActualTypeArguments()[0];
            }
            loadSchema(root, typeClass);
            for (int ver : versions) {
                Map<Integer, Schema<?>> schemaMap = root.getOrDefault(typeClass.getName(), Collections.EMPTY_MAP);
                Schema schema = schemaMap.get(ver);
                value = FieldFactory.create(field, fieldProperty, schema);
                multiVersionFields.get(ver).add(value);
            }
        } else {
            value = FieldFactory.create(field, fieldProperty);
            for (int ver : versions) {
                multiVersionFields.get(ver).add(value);
            }
        }
    }
}