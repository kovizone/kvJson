package com.kovizone.kvjson.test;

import com.alibaba.fastjson.JSONObject;
import com.kovizone.kvjson.JsonArray;
import com.kovizone.kvjson.JsonObject;
import com.kovizone.kvjson.parser.JsonParser;
import com.kovizone.kvjson.parser.ObjectParser;
import com.kovizone.kvjson.exception.KvJsonParseException;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;

public class JsonParserTest {

    @Test
    public void test() throws Exception {

        JsonObject jo = JsonObject.parse("{ 'key1':[{'key3':'value3','key4':true},\"test\",111,\"1\"] ,'key8':5,'key9':'9','key7':' value5','flag':false}");
        System.out.println(jo);
        JsonArray ja = jo.getJsonArray("key1");
        System.out.println(ja);
        System.out.println(ja.getString(1));
    }

    @Test
    public void test2() {
        System.out.println(1.00D % 1);
    }

    @Test
    public void test3() throws KvJsonParseException {
        Date date = new Date();

        java.sql.Date date2 = new java.sql.Date(date.getTime());
        Date date3 = new Date();

        System.out.println(date.getClass().isAssignableFrom(date2.getClass()));
        System.out.println(date2.getClass().isAssignableFrom(date.getClass()));
        System.out.println(date.getClass().isAssignableFrom(date3.getClass()));

        int a = 2;
        System.out.println(check(a));
    }

    @Test
    public void test4() throws KvJsonParseException, NoSuchFieldException {
        People people1 = new People();
        people1.setId(1);

        People son = new People();
        son.setId(2);
        people1.setChild(Arrays.asList(son));


        JsonObject jsonObject = (JsonObject) new ObjectParser().parse(people1);
        String json = jsonObject.toString();

        System.out.println(jsonObject);

        People people2 = (People) new JsonParser().parse(jsonObject, People.class);
        System.out.println(people2);
    }

    public boolean check(Object obj) {
        return obj instanceof Integer;
    }


}