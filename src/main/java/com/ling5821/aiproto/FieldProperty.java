package com.ling5821.aiproto;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

/**
 * @author lsj
 * @date 2021/3/31 13:49
 */
public class FieldProperty {
    private Field declaredField;
    private PropertyDescriptor propertyDescriptor;

    public FieldProperty(Field declaredField, PropertyDescriptor propertyDescriptor) {
        this.declaredField = declaredField;
        this.propertyDescriptor = propertyDescriptor;
    }

    public Field getDeclaredField() {
        return declaredField;
    }

    public void setDeclaredField(Field declaredField) {
        this.declaredField = declaredField;
    }

    public PropertyDescriptor getPropertyDescriptor() {
        return propertyDescriptor;
    }

    public void setPropertyDescriptor(PropertyDescriptor propertyDescriptor) {
        this.propertyDescriptor = propertyDescriptor;
    }
}
