package com.kovizone.kvjson;

import com.kovizone.kvjson.constant.JsonParserConstant;
import com.kovizone.kvjson.exception.KvJsonParseException;
import com.kovizone.kvjson.parser.StringParser;

import java.util.HashMap;
import java.util.Iterator;

/**
 * JSON对象，继承HashMap
 *
 * @author KoviChen
 * @version 0.0.1 20191018 KoviChen 新建类
 */
public class JsonObject extends HashMap<String, Object> {

    public JsonObject() {
        super(16);
    }

    public JsonObject(int initialCapacity) {
        super(initialCapacity);
    }

    public static JsonObject parse(String json) throws KvJsonParseException {
        return (JsonObject) new StringParser().parse(json);
    }

    public JsonArray getJsonArray(String key) {
        return (JsonArray) get(key);
    }

    public JsonObject getJsonObject(String key) {
        return (JsonObject) get(key);
    }

    public String getString(String key) {
        return String.valueOf(get(key));
    }

    public Byte getByte(String key) {
        return Byte.valueOf(String.valueOf(get(key)));
    }

    public Short getShort(String key) {
        return Short.parseShort(String.valueOf(get(key)));
    }

    public Integer getInteger(String key) {
        return Integer.parseInt(String.valueOf(get(key)));
    }

    public Long getLong(String key) {
        return Long.parseLong(String.valueOf(get(key)));
    }

    public Float getFloat(String key) {
        return Float.parseFloat(String.valueOf(get(key)));
    }

    public Double getDouble(String key) {
        return Double.parseDouble(String.valueOf(get(key)));
    }

    public boolean getBoolean(String key) {
        Object value = get(key);
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
        Iterator<Entry<String, Object>> i = entrySet().iterator();
        if (!i.hasNext()) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (; ; ) {
            Entry<String, Object> e = i.next();
            String key = e.getKey();
            Object value = e.getValue();
            sb.append(String.format("\"%s\"", key));
            sb.append(':');
            sb.append(value instanceof String ? String.format("\"%s\"", value) : value);
            if (!i.hasNext()) {
                return sb.append('}').toString();
            }
            sb.append(',');
        }
    }
}