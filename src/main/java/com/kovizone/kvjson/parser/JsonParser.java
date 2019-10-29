package com.kovizone.kvjson.parser;

import com.kovizone.kvjson.JsonArray;
import com.kovizone.kvjson.JsonObject;
import com.kovizone.kvjson.annotation.DateFormat;
import com.kovizone.kvjson.constant.JsonParserConstant;
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

    /**
     * 侵入式读取对象参数
     */
    private boolean intrusive = false;

    public JsonParser(boolean intrusive) {
        this.intrusive = intrusive;
    }

    public JsonParser() {

    }

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
        return parseObject(((JsonObject) value), clazz);
    }

    private Map<?, ?> parseMap(JsonObject jsonObject, Class<?> keyClass, Class<?> valueClass) throws KvJsonParseException {
        Map<Object, Object> map;
        try {
            map = new HashMap<>(16);

            Set<Map.Entry<Object, Object>> entrySet = map.entrySet();

            for (Map.Entry<Object, Object> entry : entrySet) {
                Object key = entry.getKey();
                Object value = jsonObject.get(String.valueOf(key));
                if (value != null) {
                    map.put(parse(key, keyClass), parse(value, valueClass));
                }
            }

        } catch (Exception e) {
            throw new KvJsonParseException(e.getMessage());
        }

        return map;
    }

    private List<?> parseCollection(JsonArray jsonArray, Class<?> clazz) throws KvJsonParseException {
        List<Object> ts;

        try {
            ts = new ArrayList<>();
            for (Object json : jsonArray) {
                ts.add(parse(json, clazz));
            }
        } catch (Exception e) {
            throw new KvJsonParseException(e.getMessage());
        }

        return ts;
    }

    private Collection<?> parseArray(JsonArray jsonArray, Class<?> arrayClass) throws KvJsonParseException {
        Collection<Object> ts;
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

    private void parseField(Field field, Object object, Object value) throws KvJsonParseException {
        try {
            Class<?> parameterType = field.getType();
            Object setValue;
            // Date类型
            if (parameterType.equals(Date.class)) {
                // 属性存在DateFormat注解
                if (field.isAnnotationPresent(DateFormat.class)) {
                    setValue = new SimpleDateFormat(field.getAnnotation(DateFormat.class).value()).parse(String.valueOf(value));
                }
                // 默认值为时间戳
                else {
                    setValue = new Date(Long.parseLong(String.valueOf(value)));
                }
            }
            // 集合
            else if (Collection.class.isAssignableFrom(parameterType)) {
                Class<?> parameterizedTypeClass = Object.class;
                Type type = field.getGenericType();
                // 有泛型
                if (type instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) type;
                    Type[] types = parameterizedType.getActualTypeArguments();
                    String typeName = types[0].getTypeName();
                    if (!typeName.equals(String.valueOf(JsonParserConstant.QUESTION))) {
                        parameterizedTypeClass = Class.forName(typeName);
                    }
                }
                setValue = parseCollection((JsonArray) value, parameterizedTypeClass);
            }

            // Map
            else if (Map.class.isAssignableFrom(parameterType)) {
                Type type = field.getGenericType();
                Class<?> keyClass = String.class;
                Class<?> valueClass = Object.class;
                // 有泛型
                if (type instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) type;
                    Type[] types = parameterizedType.getActualTypeArguments();

                    String keyTypeName = types[0].getTypeName();
                    if (!keyTypeName.equals(String.valueOf(JsonParserConstant.QUESTION))) {
                        keyClass = Class.forName(keyTypeName);
                    }

                    String valueTypeName = types[1].getTypeName();
                    if (!valueTypeName.equals(String.valueOf(JsonParserConstant.QUESTION))) {
                        valueClass = Class.forName(valueTypeName);
                    }

                }
                setValue = parseMap((JsonObject) value, keyClass, valueClass);
            }

            // 其他
            else {
                setValue = parse(value, parameterType);
            }


            // 直接注入
            if (intrusive) {
                field.setAccessible(true);
                field.set(object, setValue);
            }

            // 调用setter方法
            else {
                String fieldName = field.getName();
                String setterMethodName = String.format("set%s", GeneralUtils.upperCase(fieldName));
                Method method = object.getClass().getDeclaredMethod(setterMethodName, field.getType());
                method.invoke(object, setValue);
            }
        } catch (Exception e) {
            throw new KvJsonParseException(e.getMessage());
        }
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

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            Object value = jsonObject.get(fieldName);
            if (value != null) {
                parseField(field, object, value);
            }
        }
        return object;
    }
}
