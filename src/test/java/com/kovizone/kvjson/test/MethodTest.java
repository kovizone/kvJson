package com.kovizone.kvjson.test;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 方法反射测试
 *
 * @author KoviChen
 */
public class MethodTest {

    @Test
    public void test() {
        Method[] methods = this.getClass().getDeclaredMethods();
        System.out.println(methods.length);
        for (Method m : methods) {
            if (m.getName().equals("testParamType")) {
                System.out.println(m.getParameterTypes()[0].getName());
            }
        }
    }

    @Test
    public void test2() {
        Class<?> a = Collection.class;

        Class<?> b = List.class;
        Class<?> c = ArrayList.class;

        System.out.println(c.isAssignableFrom(a));
        System.out.println(a.isAssignableFrom(c));
        System.out.println(a.isAssignableFrom(b));

    }

    public void testParamType(String test) {

    }

}
