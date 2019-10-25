package com.kovizone.kvjson.parser;

import com.alibaba.fastjson.JSONObject;
import com.kovizone.kvjson.JsonArray;
import com.kovizone.kvjson.JsonObject;
import com.kovizone.kvjson.constant.JsonParserConstant;
import com.kovizone.kvjson.exception.KvJsonParseException;

/**
 * JSON字符串解析器
 *
 * @author KoviChen
 * @version 0.0.1 20191016 KoviChen 新建类
 * @version 0.0.2 20191018 KoviChen 替换Exception为自定义异常
 * @version 0.0.2 20191018 KoviChen 将常量独立成常量类
 * @version 0.0.2 20191018 KoviChen 添加部分注释
 */
public class StringParser {

    /**
     * JSON字符串转char数组缓存
     */
    private char[] str;

    /**
     * 遍历char数组索引
     */
    private int index = 0;

    public Object parse(String json) throws KvJsonParseException {
        this.str = json.toCharArray();
        this.index = 0;
        return parseValue();
    }

    private Object parseValue() throws KvJsonParseException {
        switch (str[index]) {
            case JsonParserConstant.BEGIN_OBJECT:
                return parseObject();
            case JsonParserConstant.BEGIN_ARRAY:
                return parseArray();
            case JsonParserConstant.STRING_TYPE_1:
            case JsonParserConstant.STRING_TYPE_2:
                return parseString();
            case JsonParserConstant.BEGIN_NULL:
                return parseNull();
            case JsonParserConstant.BEGIN_TRUE:
                return parseTrue();
            case JsonParserConstant.BEGIN_FALSE:
                return parseFalse();
            default:
                return parseNumber();
        }
    }

    private JsonObject parseObject() throws KvJsonParseException {
        JsonObject object = new JsonObject();
        index++;
        while (true) {
            while (str[index] == JsonParserConstant.EMPTY || str[index] == JsonParserConstant.SEP_COMMA) {
                index++;
            }
            // 遇到边缘
            if (str[index] == JsonParserConstant.END_OBJECT) {
                index++;
                return object;
            }
            String key = parseString();
            while (str[index] == JsonParserConstant.EMPTY || str[index] == JsonParserConstant.SEP_COLON) {
                index++;
            }
            object.put(key, parseValue());
        }
    }

    private JsonArray parseArray() throws KvJsonParseException {
        index++;
        JsonArray array = new JsonArray();
        while (true) {
            if (str[index] == JsonParserConstant.END_ARRAY) {
                index++;
                return array;
            }
            if (str[index] != JsonParserConstant.EMPTY && str[index] != JsonParserConstant.SEP_COMMA) {
                array.add(parseValue());
            } else {
                index++;
            }
        }
    }

    private String parseString() {
        char flag;
        if (str[index] == JsonParserConstant.STRING_TYPE_1) {
            flag = JsonParserConstant.STRING_TYPE_1;
            index++;
        } else if (str[index] == JsonParserConstant.STRING_TYPE_2) {
            flag = JsonParserConstant.STRING_TYPE_2;
            index++;
        } else {
            flag = JsonParserConstant.SEP_COLON;
        }
        StringBuilder sb = new StringBuilder();
        while (true) {
            if (str[index] == flag) {
                if (flag == JsonParserConstant.SEP_COLON) {
                    return sb.toString().trim();
                }
                index++;
                return sb.toString();
            }
            sb.append(str[index]);
            index++;
        }
    }

    private boolean parseFalse() throws KvJsonParseException {

        char[] charArray = JsonParserConstant.FALSE.toCharArray();
        for (char c : charArray) {
            if (str[index] != c) {
                throw new KvJsonParseException("parse 'false' error (" + index + ")");
            }
            index++;
        }
        return false;
    }

    private boolean parseTrue() throws KvJsonParseException {
        char[] charArray = JsonParserConstant.TRUE.toCharArray();
        for (char c : charArray) {
            if (str[index] != c) {
                throw new KvJsonParseException("parse 'true' error (" + index + ")");
            }
            index++;
        }
        return true;
    }

    private Object parseNull() throws KvJsonParseException {

        char[] charArray = JsonParserConstant.NULL.toCharArray();
        for (char c : charArray) {
            if (str[index] != c) {
                throw new KvJsonParseException("parse 'null' error (" + index + ")");
            }
            index++;
        }
        return null;
    }

    /**
     * 解析出数字
     * @return 数字
     */
    private Object parseNumber() {
        while (!isNumber(str[index])) {
            index++;
        }
        StringBuilder sb = new StringBuilder();

        while (isNumber(str[index])) {
            sb.append(str[index]);
            index++;
        }
        double d = Double.parseDouble(sb.toString());

        if (d % 1 != 0) {
            return d;
        }
        if (d > Integer.MAX_VALUE) {
            return (long) d;
        }
        return (int) d;
    }

    /**
     * 判断字符是否是数字或组成数字的符号
     * 0~9、e、E、+、-
     * @param c 字符
     * @return 是或否
     */
    private boolean isNumber(char c) {

        if (c >= JsonParserConstant.NUMBER_ZERO && c <= JsonParserConstant.NUMBER_NINE) {
            return true;
        }
        switch (c) {
            case JsonParserConstant.POSITIVE:
            case JsonParserConstant.NEGATIVE:
            case JsonParserConstant.DECIMAL_POINT:
            case JsonParserConstant.E:
            case JsonParserConstant.E2:
                return true;
            default:
                return false;
        }
    }

}
