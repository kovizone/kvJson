package com.kovizone.kvjson.parser;

import com.kovizone.kvjson.JsonArray;
import com.kovizone.kvjson.JsonObject;
import com.kovizone.kvjson.annotation.DateFormat;
import com.kovizone.kvjson.exception.KvJsonParseException;
import com.kovizone.kvjson.util.GeneralUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * JsonObject或JsonArray对象解析器
 *
 * @author KoviChen
 * @version 0.0.1 20191021 KoviChen 新建类
 */
public class JsonParser {

    public Object parse(Object value, Class<?> clazz) throws KvJsonParseException {

        try {
            if (clazz.equals(byte.class) || clazz.equals(Byte.class)) {
                return Byte.parseByte(String.valueOf(value));
            }
            if (clazz.equals(short.class) || clazz.equals(Short.class)) {
                return Short.parseShort(String.valueOf(value));
            }
            if (clazz.equals(int.class) || clazz.equals(Integer.class)) {
                return Integer.parseInt(String.valueOf(value));
            }
            if (clazz.equals(long.class) || clazz.equals(Long.class)) {
                return Long.parseLong(String.valueOf(value));
            }
            if (clazz.equals(float.class) || clazz.equals(Float.class)) {
                return Float.parseFloat(String.valueOf(value));
            }
            if (clazz.equals(double.class) || clazz.equals(Double.class)) {
                return Double.parseDouble(String.valueOf(value));
            }
            if (clazz.equals(char.class) || clazz.equals(Character.class)) {
                String str = String.valueOf(value);
                if (str.length() == 1) {
                    return str.charAt(0);
                } else {
                    throw new KvJsonParseException("String cast char error");
                }
            }
            if (clazz.equals(String.class)) {
                return String.valueOf(value);
            }
        } catch (Exception e) {
            throw new KvJsonParseException(e.getMessage());
        }

        if (clazz.equals(byte[].class)
                || clazz.equals(short[].class)
                || clazz.equals(int[].class)
                || clazz.equals(long[].class)
                || clazz.equals(float[].class)
                || clazz.equals(double[].class)
                || clazz.equals(char[].class)
                || clazz.equals(Object[].class)
                || Collection.class.isAssignableFrom(clazz)) {
            return parseArray(((JsonArray) value), clazz);
        }
        if (value instanceof Collection) {
            return parseCollection(((JsonArray) value), clazz);
        }
        return parseObject(((JsonObject) value), clazz);
    }

    private List<?> parseCollection(JsonArray jsonArray, Class<?> clazz) throws KvJsonParseException {
        List<Object> ts;
        try {
            ts = new ArrayList<>();
        } catch (Exception e) {
            throw new KvJsonParseException(e.getMessage());
        }

        try {
            for (Object json : jsonArray) {
                ts.add(parse(json, clazz));
            }
        } catch (Exception e) {
            throw new KvJsonParseException(e.getMessage());
        }

        return ts;
    }

    private List<?> parseArray(JsonArray jsonArray, Class<?> arrayClass) throws KvJsonParseException {
        List<Object> ts;
        try {
            ts = new ArrayList<>();
        } catch (Exception e) {
            throw new KvJsonParseException(e.getMessage());
        }

        Class<?> clazz = null;
        if (arrayClass.equals(byte[].class) || arrayClass.equals(Byte[].class)) {
            clazz = Byte.class;
        }
        if (arrayClass.equals(short[].class) || arrayClass.equals(Short[].class)) {
            clazz = Short.class;
        }
        if (arrayClass.equals(int[].class) || arrayClass.equals(Integer[].class)) {
            clazz = Integer.class;
        }
        if (arrayClass.equals(long[].class) || arrayClass.equals(Long[].class)) {
            clazz = Long.class;
        }
        if (arrayClass.equals(float[].class) || arrayClass.equals(Float[].class)) {
            clazz = Float.class;
        }
        if (arrayClass.equals(double[].class) || arrayClass.equals(Double[].class)) {
            clazz = Double.class;
        }
        if (arrayClass.equals(char[].class) || arrayClass.equals(Character[].class)) {
            clazz = Character.class;
        }
        if (clazz != null) {
            for (Object json : jsonArray) {
                ts.add(parse(json, clazz));
            }
        }

        return ts;
    }

    private Object parseObject(JsonObject jsonObject, Class<?> clazz) throws KvJsonParseException {
        if (clazz.equals(Object.class)) {
            return jsonObject;
        }
        Object object;
        try {
            object = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new KvJsonParseException(e.getMessage());
        }
        Method[] methods = clazz.getDeclaredMethods();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();

            Object value = jsonObject.get(fieldName);
            if (value != null) {
                String setterMethodName = String.format("set%s", GeneralUtils.upperCase(fieldName));

                // 遍历所有方法
                for (int i = 0; i < methods.length; i++) {
                    Method method = methods[i];

                    // 查找Setter方法
                    if (method != null && method.getName().equals(setterMethodName)) {

                        // 方法参数只有一个
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        if (parameterTypes.length == 1) {
                            Class<?> parameterType = parameterTypes[0];

                            try {
                                // Date类型
                                if (parameterType.equals(Date.class)) {
                                    if (field.isAnnotationPresent(DateFormat.class)) {
                                        method.invoke(object, new SimpleDateFormat(field.getAnnotation(DateFormat.class).value()).parse(String.valueOf(value)));
                                    } else {
                                        method.invoke(object, new Date(Long.parseLong(String.valueOf(value))));
                                    }
                                } else {

                                    // 集合
                                    if (Collection.class.isAssignableFrom(parameterType)) {
                                        try {
                                            // 有泛型
                                            ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                                            Type[] types = parameterizedType.getActualTypeArguments();
                                            method.invoke(object, parse(value, Class.forName(types[0].getTypeName())));
                                        } catch (Exception e2) {
                                            // 无泛型
                                            method.invoke(object, parse(value, Object.class));
                                        }
                                    } else {
                                        method.invoke(object, parse(value, parameterType));
                                    }
                                    // 避免重复遍历
                                }
                                methods[i] = null;
                                break;
                            } catch (Exception e) {
                                //执行方法发生异常时，不会break
                            }
                        }
                    }
                }
            }

        }
        return object;
    }
}
