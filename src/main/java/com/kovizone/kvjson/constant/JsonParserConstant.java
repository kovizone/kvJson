package com.kovizone.kvjson.constant;

/**
 * 解析器常量
 *
 * @author KoviChen
 * @version 0.0.1 20191018 KoviChen 新建类
 */
public class JsonParserConstant {
    private JsonParserConstant() {
    }

    public static final char BEGIN_OBJECT = '{';
    public static final char BEGIN_ARRAY = '[';
    public static final char END_OBJECT = '}';
    public static final char END_ARRAY = ']';

    public static final char STRING_TYPE_1 = '"';
    public static final char STRING_TYPE_2 = '\'';

    public static final String NULL = "null";
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final char BEGIN_NULL = 'n';
    public static final char BEGIN_TRUE = 't';
    public static final char BEGIN_FALSE = 'f';

    public static final char SEP_COLON = ':';
    public static final char SEP_COMMA = ',';

    public static final char NUMBER_ZERO = '0';
    public static final char NUMBER_NINE = '9';
    public static final char POSITIVE = '+';
    public static final char NEGATIVE = '-';
    public static final char DECIMAL_POINT = '.';
    public static final char E = 'E';
    public static final char E2 = 'e';

    public static final char LOWER_A = 'a';
    public static final char LOWER_Z = 'z';

    public static final char EMPTY = ' ';
    public static final String NONE = "";

    public static final char QUESTION = '?';

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static final String INDENT = "\t";

    public static final String SPACE = " ";

    public static final char QUOTATION = '\'';
    
    public static final char DOUBLE_QUOTATION = '"';

}
