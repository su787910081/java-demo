package com.suyh;

import org.junit.Test;

import java.math.BigDecimal;

/**
 * BigDecimal Demo
 * <p>
 * 阿里巴巴手册也提到，禁止使用 BigDecimal(double) 的构造方法来进行浮点数的构造。
 * 因为它存在精度丢失的风险。
 * 正例：优先推荐入参为String 的构造方法，或使用BigDecimal 的valueOf 方法，
 * 此方法内部其实执行了Double 的toString, 而Double 的toString 按double 的实际能表达的精度对尾数进行了截断。
 * BigDecimal recommend1 = new BigDecimal("0.1");  // BigDecimal(String) 构造方法
 * BigDecimal recommend2 = BigDecimal.valueOf(0.1);    // BigDecimal.valueOf(double) 静态方法
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
     * <p>
     * BigDecimal.ROUND_DOWN: 直接省略多余的小数，比如1.28如果保留1位小数，得到的就是1.2
     * BigDecimal.ROUND_UP: 直接进位，比如1.21如果保留1位小数，得到的就是1.3
     * BigDecimal.ROUND_HALF_UP: 四舍五入，2.35保留1位，变成2.4 (常规意义上的四舍五入)
     * BigDecimal.ROUND_HALF_DOWN: 四舍五入，2.35保留1位，变成2.3
     * 后边两种的区别就是如果保留的位数的后一位如果正好是5的时候，一个舍弃掉，一个进位。
     */
    @Test
    public void testScale01() {
        BigDecimal m = new BigDecimal("1.255433");
        // 保留3 位小数
        BigDecimal n = m.setScale(3, BigDecimal.ROUND_HALF_DOWN);
        System.out.println(n);// 1.255

        // 特殊地：下一位刚好且仅为5 的情况
        BigDecimal bd1 = new BigDecimal("1.255");
        BigDecimal bdm11 = bd1.setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal bdm12 = bd1.setScale(2, BigDecimal.ROUND_HALF_DOWN);
        System.out.println("bdm1: " + bdm11);
        System.out.println("bdm2: " + bdm12);

        // 下一位刚好是5，但不仅为5。实际为 55
        BigDecimal bd2 = new BigDecimal("1.255");
        BigDecimal bdm21 = bd1.setScale(1, BigDecimal.ROUND_HALF_UP);
        BigDecimal bdm22 = bd1.setScale(1, BigDecimal.ROUND_HALF_DOWN);
        System.out.println("bdm1: " + bdm21);
        System.out.println("bdm2: " + bdm22);
    }
}
