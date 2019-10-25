package com.kovizone.kvjson.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 时间解析格式化
 *
 * @author KoviChen
 * @version 0.0.1 20191021 KoviChen 新建类
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateFormat {
    String value() default "yyyy-MM-dd HH:mm:ss";
}
