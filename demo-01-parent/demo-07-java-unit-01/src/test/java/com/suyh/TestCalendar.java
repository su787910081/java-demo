package com.suyh;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TestCalendar {
    @Test
    public void test01() {
        // 指定日期上加N天
        String resDay = "";
        resDay = addDay("1970-01-01", 1);
        System.out.println(resDay);
        resDay = addDay("1970-01-01", 40);
        System.out.println(resDay);
    }

    @Test
    public void test02() {
        // 在今天的基础上加N天
        Calendar now = Calendar.getInstance();  // 根据系统来创建匹配的日历

//        String s1 = now.getTime().toString();
//        String s2 = new Date().toString();
//        System.out.println("s1: " + s1);
//        System.out.println("s2: " + s2);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String day = df.format(now.getTime());
        System.out.println(day);
        try {
            now.setTime(df.parse(day));
            System.out.println(now.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String addDay(String s, int n) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Calendar cd = Calendar.getInstance();
            cd.setTime(sdf.parse(s));
            cd.add(Calendar.DATE, n);//增加一天
            //cd.add(Calendar.MONTH, n);//增加一个月
            return sdf.format(cd.getTime());

        } catch (Exception e) {
            return null;
        }

    }
}
