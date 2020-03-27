package com.suyh;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestTime {
    @Test
    public void test01() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date d1 = new Date();
        long cur_time = d1.getTime();
        System.out.println("cur_time: 0X" + Long.toHexString(cur_time));
        System.out.println("cur time: " + d1.toString());
        System.out.println("d1: " + df.format(d1));
        long other_time = cur_time & 0X00000000FFFFFFFFL;
        Date d2 = new Date(other_time);
        System.out.println("d2: " + df.format(d2));

        long other_time_02 = cur_time >>> 16;
        Date d3 = new Date(other_time_02);
        System.out.println("d3: " + df.format(d3));
    }
}
