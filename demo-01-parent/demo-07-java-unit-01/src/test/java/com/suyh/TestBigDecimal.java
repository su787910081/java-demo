package com.suyh;

import org.junit.Test;

import java.math.BigDecimal;

/**
 * BigDecimal Demo
 *
 * 阿里巴巴手册也提到，禁止使用 BigDecimal(double) 的构造方法来进行浮点数的构造。
 * 因为它存在精度丢失的风险。
 * 正例：优先推荐入参为String 的构造方法，或使用BigDecimal 的valueOf 方法，
 * 此方法内部其实执行了Double 的toString, 而Double 的toString 按double 的实际能表达的精度对尾数进行了截断。
 *      BigDecimal recommend1 = new BigDecimal("0.1");  // BigDecimal(String) 构造方法
 *      BigDecimal recommend2 = BigDecimal.valueOf(0.1);    // BigDecimal.valueOf(double) 静态方法
 */
public class TestBigDecimal {
    /**
     * 浮点数的相等判断
     */
    @Test
    public void testEquals01() {
        BigDecimal a = new BigDecimal("1.0");
        BigDecimal b = new BigDecimal("0.9");
        BigDecimal c = new BigDecimal("0.8");
        BigDecimal x = a.subtract(b);// 0.1
        BigDecimal y = b.subtract(c);// 0.1
        System.out.println(x.equals(y));// true
    }

    /**
     * 浮点数的大小比较
     * a.compareTo(b) : 返回 -1 表示小于，0 表示 等于， 1表示 大于。
     */
    @Test
    public void testCompare01() {
        BigDecimal a = new BigDecimal("1.0");
        BigDecimal b = new BigDecimal("0.9");
        System.out.println(a.compareTo(b));
    }

    /**
     * 通过 setScale方法设置保留几位小数以及保留规则。
     */
    @Test
    public void testScale01() {
        BigDecimal m = new BigDecimal("1.255433");
        BigDecimal n = m.setScale(3,BigDecimal.ROUND_HALF_DOWN);
        System.out.println(n);// 1.255
    }
}
