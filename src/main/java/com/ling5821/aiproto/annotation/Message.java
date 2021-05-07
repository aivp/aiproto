package com.ling5821.aiproto.annotation;

import java.lang.annotation.*;

/**
 * @author lsj
 * @date 2021/1/25 16:04
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Message {

    int[] value() default {};

    String desc() default "";
}
