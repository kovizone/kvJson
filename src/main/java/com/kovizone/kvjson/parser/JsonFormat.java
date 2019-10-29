package com.kovizone.kvjson.parser;

import com.kovizone.kvjson.constant.JsonParserConstant;

import java.util.Stack;

/**
 * JSON格式化
 *
 * @author KoviChen
 * @version 0.0.1 20191028 KoviChen 新建类
 * @version 0.0.2 20191029 KoviChen 修复字符串中存在边界符号时的格式化异常
 * @version 0.0.2 20191029 KoviChen 常量独立
 */
public class JsonFormat {

    private JsonFormat() {
    }

    public static String format(Object json) {
        return format(String.valueOf(json));
    }

    private static void addTab(StringBuilder sb, int tabSize) {
        for (int i = 0; i < tabSize; i++) {
            sb.append(JsonParserConstant.INDENT);
        }
    }

    public static String compress(String json) {
        char[] jsonChars = json.replace("\n", JsonParserConstant.NONE)
                .replace("\r", JsonParserConstant.NONE)
                .replace(JsonParserConstant.LINE_SEPARATOR, JsonParserConstant.NONE)
                .replace(JsonParserConstant.INDENT, JsonParserConstant.NONE)
                .toCharArray();

        Character flag = null;
        StringBuilder sb = new StringBuilder();

        for (char c : jsonChars) {
            if (c == JsonParserConstant.DOUBLE_QUOTATION || c == JsonParserConstant.QUOTATION) {
                if (flag == null) {
                    flag = c;
                } else if (flag == c) {
                    flag = null;
                }
            }
            if (flag != null || c != JsonParserConstant.EMPTY) {
                sb.append(c);
            }
        }

        return sb.toString();

    }

    public static String format(String json) {
        char[] jsonChars = json.toCharArray();

        int tabSize = 0;

        Character flag = null;

        StringBuilder sb = new StringBuilder();
        for (char c : jsonChars) {

            // 引号，为flag填充引号，表示读取字符串
            if (c == JsonParserConstant.DOUBLE_QUOTATION || c == JsonParserConstant.QUOTATION) {
                if (flag == null) {
                    flag = c;
                } else if (flag == c) {
                    flag = null;
                }
                sb.append(c);
            }
            // 栈为空，表非字符串的空格，跳过
            else if (flag == null) {
                //左边界
                if (c == JsonParserConstant.BEGIN_OBJECT || c == JsonParserConstant.BEGIN_ARRAY) {
                    sb.append(c);
                    sb.append(JsonParserConstant.LINE_SEPARATOR);
                    addTab(sb, ++tabSize);
                }
                // 右边界
                else if (c == JsonParserConstant.END_OBJECT || c == JsonParserConstant.END_ARRAY) {
                    sb.append(JsonParserConstant.LINE_SEPARATOR);
                    addTab(sb, --tabSize);
                    sb.append(c);
                }
                // 逗号，换行
                else if (c == JsonParserConstant.SEP_COMMA) {
                    sb.append(JsonParserConstant.SPACE);
                    sb.append(c);
                    sb.append(JsonParserConstant.LINE_SEPARATOR);
                    addTab(sb, tabSize);
                }
                // 分号，左右加空格
                else if (c == JsonParserConstant.SEP_COLON) {
                    sb.append(JsonParserConstant.SPACE);
                    sb.append(c);
                    sb.append(JsonParserConstant.SPACE);
                }
                // 不读取空
                else if (c != JsonParserConstant.EMPTY) {
                    sb.append(c);
                }
            }
            // 栈非空，直接读取
            else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
