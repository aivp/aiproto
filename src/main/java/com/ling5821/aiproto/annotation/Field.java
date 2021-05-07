package com.ling5821.aiproto.annotation;

import com.ling5821.aiproto.DataType;

import java.lang.annotation.*;

/**
 * @author lsj
 * @date 2021/1/25 12:17
 */
@Repeatable(Fields.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Field {

    int index() default -1;

    int length() default -1;

    int lengthSize() default -1;

    DataType type() default DataType.BYTE;

    String charset() default "UTF-8";

    byte pad() default 0x00;

    String desc() default "";

    int[] version() default {1};
}
