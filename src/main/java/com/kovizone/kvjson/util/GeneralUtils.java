package com.kovizone.kvjson.util;

import com.kovizone.kvjson.constant.JsonParserConstant;

/**
 * 通用工具
 *
 * @author KoviChen
 * @version 0.0.1 20191021 KoviChen 新建类
 */
public class GeneralUtils {

    private GeneralUtils() {
    }

    /**
     * 首字母大写
     * @param str
     * @return
     */
    public static String upperCase(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= JsonParserConstant.LOWER_A && ch[0] <= JsonParserConstant.LOWER_Z) {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }
}
