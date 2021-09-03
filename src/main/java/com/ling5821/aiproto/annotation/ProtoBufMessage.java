package com.ling5821.aiproto.annotation;

import java.lang.annotation.*;

/**
 * @author lsj
 * @date 2021/9/1 10:01
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Message
public @interface ProtoBufMessage {
    String[] value() default {};

    String desc() default "";
}
