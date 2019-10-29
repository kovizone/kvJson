package com.kovizone.kvjson;

import com.kovizone.kvjson.constant.JsonParserConstant;

import java.util.Stack;

/**
 * JSON格式化
 *
 * @author KoviChen
 * @version 0.0.1 20191028 KoviChen 新建类
 * @version 0.0.2 20191029 KoviChen 修复字符串中存在边界符号时的格式化异常
 */
public class JsonFormat {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private static final String INDENT = "\t";

    private static final String SPACE = " ";

    private JsonFormat() {
    }

    public static String format(Object json) {
        return format(String.valueOf(json));
    }

    private static void addTab(StringBuilder sb, int tabSize) {
        for (int i = 0; i < tabSize; i++) {
            sb.append(INDENT);
        }
    }

    public static String compress(String json) {
        char[] jsonChars = json.replace("\n", "")
                .replace("\r", "")
                .replace(LINE_SEPARATOR, "")
                .replace(INDENT, "")
                .toCharArray();

        Character flag = null;
        StringBuilder sb = new StringBuilder();

        for (char c : jsonChars) {
            if (c == '"' || c == '\'') {
                if (flag == null) {
                    flag = c;
                } else if (flag == c) {
                    flag = null;
                }
            }
            if (flag != null || c != ' ') {
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
            if (c == '"' || c == '\'') {
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
                if (c == '{' || c == '[') {
                    sb.append(c);
                    sb.append(LINE_SEPARATOR);
                    addTab(sb, ++tabSize);
                }
                // 右边界
                else if (c == '}' || c == ']') {
                    sb.append(LINE_SEPARATOR);
                    addTab(sb, --tabSize);
                    sb.append(c);
                }
                // 逗号，换行
                else if (c == ',') {
                    sb.append(SPACE);
                    sb.append(c);
                    sb.append(LINE_SEPARATOR);
                    addTab(sb, tabSize);
                }
                // 分号，左右加空格
                else if (c == ':') {
                    sb.append(SPACE);
                    sb.append(c);
                    sb.append(SPACE);
                }
                // 不读取空
                else if (c != ' ') {
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
