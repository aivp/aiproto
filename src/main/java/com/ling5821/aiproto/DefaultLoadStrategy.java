package com.ling5821.aiproto;

import com.ling5821.aiproto.annotation.Field;
import com.ling5821.aiproto.annotation.Message;
import com.ling5821.aiproto.field.BasicField;
import com.ling5821.aiproto.schema.RuntimeSchema;
import com.ling5821.aiproto.util.ClassUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author lsj
 * @date 2021/3/24 16:22
 */
public class DefaultLoadStrategy implements LoadStrategy {

    private Map<Object, Map<Integer, Schema<?>>> classSchemaMapping = new HashMap<>(140);

    private Map<Object, Map<Integer, Class<?>>> typeIdClassMapping = new HashMap<>(140);

    public DefaultLoadStrategy() {
    }

    public DefaultLoadStrategy(String basePackage) {
        List<Class<?>> types = ClassUtils.getClassList(basePackage);
        for (Class<?> type : types) {
            Message message = type.getAnnotation(Message.class);
            if (message != null) {
                int[] values = message.value();
                for (int typeId : values) {
                    loadSchema(classSchemaMapping, typeId, type);
                    loadClass(typeIdClassMapping, typeId, type);
                }
            }
        }
        Introspector.flushCaches();
    }

    @Override
    public Map<Object, Map<Integer, Schema<?>>> getClassSchemaMapping() {
        return classSchemaMapping;
    }

    @Override
    public Map<Object, Map<Integer, Class<?>>> getTypeIdClassMapping() {
        return typeIdClassMapping;
    }

    @Override
    public Map<Integer, Schema<?>> loadSchema(Map<Object, Map<Integer, Schema<?>>> root, Class<?> typeClass) {
        Map<Integer, Schema<?>> schemas = root.get(typeClass.getName());
        //不支持循环引用
        if (schemas != null) {
            return schemas;
        }

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

    protected Map<Integer, List<BasicField>> findMultiVersionFields(Map<Object, Map<Integer, Schema<?>>> root,
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

                Field[] methodFields = readMethod.getDeclaredAnnotationsByType(Field.class);
                if (methodFields != null && methodFields.length != 0) {
                    for (Field field : methodFields) {
                        fillField(root, multiVersionFields, fieldProperty, field);
                    }
                    continue;
                }
            }

            if (declaredField != null) {

                Field[] propertyFields = declaredField.getDeclaredAnnotationsByType(Field.class);
                if (propertyFields != null) {
                    for (Field field : propertyFields) {
                        fillField(root, multiVersionFields, fieldProperty, field);
                    }
                }
            }
        }
        return multiVersionFields;
    }

    protected void fillField(Map<Object, Map<Integer, Schema<?>>> root,
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