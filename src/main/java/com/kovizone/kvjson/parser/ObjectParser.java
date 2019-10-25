package com.kovizone.kvjson.parser;

import com.kovizone.kvjson.util.GeneralUtils;
import com.kovizone.kvjson.JsonArray;
import com.kovizone.kvjson.JsonObject;
import com.kovizone.kvjson.annotation.DateFormat;
import com.kovizone.kvjson.exception.KvJsonParseException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 对象解析器
 *
 * @author KoviChen
 * @version 0.0.1 20191018 KoviChen 新建类
 */
public class ObjectParser {

    /**
     * 侵入式读取对象参数
     */
    private boolean intrusive = false;

    public void setIntrusive(boolean intrusive) {
        this.intrusive = intrusive;
    }

    public ObjectParser(boolean intrusive) {
        this.intrusive = intrusive;
    }

    public ObjectParser() {
        super();
    }

    public Object parse(Object object) throws KvJsonParseException {
        if (object == null) {
            return null;
        }

        // Date类型
        if (object instanceof Date) {
            return ((Date) object).getTime();
        }

        // 基本数据类型
        if (object instanceof Byte
                || object instanceof Short
                || object instanceof Integer
                || object instanceof Long
                || object instanceof Float
                || object instanceof Double
                || object instanceof Boolean) {
            return object;
        }

        // 字符串
        if (object instanceof CharSequence || object instanceof Character) {
            return String.valueOf(object);
        }

        // 数组、List、Set
        if (object instanceof byte[]
                || object instanceof short[]
                || object instanceof int[]
                || object instanceof long[]
                || object instanceof float[]
                || object instanceof double[]
                || object instanceof boolean[]
                || object instanceof char[]
                || object instanceof Object[]
                || object instanceof Collection) {
            return parseArray(object);
        }

        // Map和对象
        return parseObject(object);
    }


    private JsonArray parseArray(Object object) throws KvJsonParseException {
        if (object instanceof Collection) {
            JsonArray jsonArray = new JsonArray();
            for (Object o : (Collection) object) {
                jsonArray.add(parse(o));
            }
            return jsonArray;
        }
        if (object instanceof Object[]) {
            JsonArray jsonArray = new JsonArray();
            for (Object o : (Object[]) object) {
                jsonArray.add(parse(o));
            }
            return jsonArray;
        }
        if (object instanceof byte[]
                || object instanceof short[]
                || object instanceof int[]
                || object instanceof long[]
                || object instanceof float[]
                || object instanceof double[]
                || object instanceof boolean[]
                || object instanceof char[]) {
            return new JsonArray(Collections.singletonList(object));
        }

        throw new KvJsonParseException("parse error: " + object.getClass().getName());
    }

    private JsonObject parseObject(Object object) throws KvJsonParseException {

        if (object instanceof Map) {
            JsonObject jsonObject = new JsonObject();
            Map map = (Map) object;
            Set keySet = map.keySet();
            for (Object key : keySet) {
                jsonObject.put(String.valueOf(key), parse(map.get(key)));
            }
            return jsonObject;
        }

        Class<?> clazz = object.getClass();
        JsonObject jsonObject = new JsonObject();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {

            if (!Modifier.isStatic(field.getModifiers())) {

                Object value = null;
                String fieldName = field.getName();
                if (intrusive) {
                    field.setAccessible(true);
                    try {
                        value = field.get(object);
                    } catch (Exception ex) {
                        value = null;
                    }
                } else {
                    String upperFieldName = GeneralUtils.upperCase(fieldName);

                    Method method;
                    try {
                        method = clazz.getMethod(String.format("get%s", upperFieldName));
                    } catch (NoSuchMethodException e) {

                        try {
                            method = clazz.getMethod(String.format("is%s", upperFieldName));
                        } catch (Exception ex) {
                            method = null;
                        }
                    }
                    if (method != null) {
                        try {
                            value = method.invoke(object);
                        } catch (Exception ignored) {
                        }
                    }
                }
                if (value != null) {
                    if (field.getType().equals(Date.class) && field.isAnnotationPresent(DateFormat.class)) {
                        jsonObject.put(fieldName, new SimpleDateFormat(field.getAnnotation(DateFormat.class).value()).format(value));
                    } else {
                        jsonObject.put(fieldName, parse(value));
                    }
                }
            }
        }
        return jsonObject;
    }
}
