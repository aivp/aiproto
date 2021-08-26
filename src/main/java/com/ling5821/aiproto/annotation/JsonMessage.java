package com.ling5821.aiproto.annotation;

import java.lang.annotation.*;

/**
 * @author lsj
 * @date 2021/1/25 16:04
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Message
public @interface JsonMessage {

    String[] value() default {};

    int[] version() default {1};

    String desc() default "";
}
