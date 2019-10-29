package com.kovizone.kvjson.test;

import com.alibaba.fastjson.JSONObject;
import com.kovizone.kvjson.JsonArray;
import com.kovizone.kvjson.JsonFormat;
import com.kovizone.kvjson.JsonObject;
import com.kovizone.kvjson.parser.JsonParser;
import com.kovizone.kvjson.parser.ObjectParser;
import com.kovizone.kvjson.exception.KvJsonParseException;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
    public void test4() throws KvJsonParseException, NoSuchFieldException, ClassNotFoundException {
        People people1 = new People();
        people1.setId(1);
        people1.setDate(new Date());
        people1.setName("测试");

        People people2 = new People();
        people2.setId(2);
        people2.setName("测试2");
        people2.setSex('F');

        People people4 = new People();
        people4.setId(4);
        people4.setName("测试4");
        people4.setSex('M');

        People people3 = new People();
        people3.setId(3);
        people3.setChild(Arrays.asList(people1,people2));
        people3.setDidi(people4);

        JsonObject jsonObject = JsonObject.parse(people3);
        System.out.println(jsonObject);


        People people100 = jsonObject.toObject(People.class);
        //System.out.println(people3);

        String json = "{\"di{di\":{\"sex\":\"M\",\"name\":\"测试4\",\"id\":4},\"id\":3,\"child\":[{\"date\":1572255445717,\"name\":\"测试\",\"id\":1},{\"sex\":\"F\",\"name\":\"测试2\",\"id\":2}]}";
        String before = JsonFormat.format(json);
        String after = JsonFormat.compress(before);
        System.out.println("before");
        System.out.println(before);
        System.out.println("after");
        System.out.println(after);

    }

    @Test
    public void test5() throws KvJsonParseException, NoSuchFieldException {
        Type type = People.class.getDeclaredField("child").getGenericType();
        System.out.println(type instanceof ParameterizedType);
    }

    @Test
    public void test6() {
        System.out.println("1" + System.getProperty("line.separator") + "\tn2");
    }

    public boolean check(Object obj) {
        return obj instanceof Integer;
    }


}