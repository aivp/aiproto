package com.ling5821.aiproto.annotation;

import com.ling5821.aiproto.converter.Converter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lsj
 * @date 2021/1/25 16:01
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Convert {

    Class<? extends Converter> converter();
}
