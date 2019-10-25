package com.kovizone.kvjson;

import com.kovizone.kvjson.constant.JsonParserConstant;
import com.kovizone.kvjson.exception.KvJsonParseException;
import com.kovizone.kvjson.parser.StringParser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * JSON数组，继承ArrayList
 *
 * @author KoviChen
 * @version 0.0.1 20191018 KoviChen 新建类
 */
public class JsonArray extends ArrayList<Object> {

    public JsonArray() {
        super(16);
    }

    public JsonArray(int initialCapacity) {
        super(initialCapacity);
    }

    public JsonArray(Collection<?> c) {
        super(c);
    }

    public static JsonArray parse(String json) throws KvJsonParseException {
        return (JsonArray) new StringParser().parse(json);
    }

    public JsonObject getJsonObject(int index) {
        return (JsonObject) get(index);
    }

    public JsonArray getJsonArray(int index) {
        return (JsonArray) get(index);
    }

    public String getString(int index) {
        return String.valueOf(get(index));
    }

    public Byte getByte(int index) {
        return Byte.valueOf(String.valueOf(get(index)));
    }

    public Short getShort(int index) {
        return Short.parseShort(String.valueOf(get(index)));
    }

    public Integer getInteger(int index) {
        return Integer.parseInt(String.valueOf(get(index)));
    }

    public Long getLong(int index) {
        return Long.parseLong(String.valueOf(get(index)));
    }


    public Float getFloat(int index) {
        return Float.parseFloat(String.valueOf(get(index)));
    }

    public Double getDouble(int index) {
        return Double.parseDouble(String.valueOf(get(index)));
    }

    public boolean getBoolean(int index) {
        Object value = get(index);
        if (value == null) {
            return false;
        }

        if (value instanceof String) {
            return ((String) value).trim().equalsIgnoreCase(JsonParserConstant.TRUE);
        }
        return (boolean) value;
    }

    @Override
    public String toString() {
        Iterator<Object> it = iterator();
        if (!it.hasNext()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (; ; ) {
            Object e = it.next();
            sb.append(e instanceof String ? String.format("\"%s\"", e) : e);
            if (!it.hasNext()) {
                return sb.append(']').toString();
            }
            sb.append(',');
        }
    }
}