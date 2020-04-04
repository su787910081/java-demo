package com.suyh;

import org.junit.Test;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TestOther {
    @Test
    public void test01() {
        int num = 0XF0000001;
        System.out.println(num + "");
        System.out.println("Integer.toBinaryString(num): " + Integer.toBinaryString(num));
        System.out.println("Integer.toHexString(num): " + Integer.toHexString(num));

        System.out.println("Integer.toHexString(num << 1): " + Integer.toHexString(num << 1));
        System.out.println("Integer.toBinaryString(num << 1):" + Integer.toBinaryString(num << 1));

        System.out.println("Integer.toHexString(num >> 1): " + Integer.toHexString(num >> 1));
        System.out.println("Integer.toHexString(num >>> 1): " + Integer.toHexString(num >>> 1));
        System.out.println("Integer.toBinaryString(num >> 1): " + Integer.toBinaryString(num >> 1));
        System.out.println("Integer.toBinaryString(num >>> 1): " + Integer.toBinaryString(num >>> 1));
    }

    @Test
    public void test02() {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");

//        Jedis jedis = null;
//        try {
//            jedis = redisConnection.getJedis();
//            jedis.select(dbIndex);
//            Calendar now = new GregorianCalendar();
//
//            String day = df.format(now.getTime());
//            //新的一天，通过新 key 获取值，每天都能从1开始获取
//            String key = "";
//            key = key + "_" + day;
//            Long num = jedis.incr(key);
//            //设置 key 过期时间
//            if (num == 1) {
//                jedis.expire(key, (24 - now.get(Calendar.HOUR_OF_DAY)) * 3600 + 1800);
//            }
//            if (haveDay) {
//                return createUUID(num, day, length);
//            } else {
//                return num;
//            }
//        } finally {
//            if (jedis != null) {
//                jedis.close();
//            }
//        }


    }

    private Long createUUID(Long num, String day, Integer length) {
        String id = String.valueOf(num);
        if (id.length() < length) {
            NumberFormat nf = NumberFormat.getInstance();
            nf.setGroupingUsed(false);
            nf.setMaximumIntegerDigits(length);
            nf.setMinimumIntegerDigits(length);
            id = nf.format(num);
        }
        return Long.parseLong(day + id);
    }
}
