package com.suyh;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(PowerMockRunner.class)
// 1. 如果想要对某个类的静态方法进行mock，则必须在PrepareForTest后面加上相应的类名
@PrepareForTest({Utility.class})
public class UtilityHelperTest {
    private UtilityHelper utilityHelper;
    private List<Integer> dataList;

    @Before
    public void setUp() {
        // 2. 在对该类的某个方法进行mock之前，必须先对整个类进行mock
        PowerMockito.mockStatic(Utility.class);

        // 平常写时，可以紧跟在mockStatic方法后
        // PowerMockito.spy(Utility.class);

        dataList = new ArrayList<Integer>();
        dataList.add(1);
        dataList.add(2);
        dataList.add(3);

        utilityHelper = new UtilityHelper();
    }

    @Test
    public void testSum_1() {
        // mock 调用这个静态方法时总是返回true
        PowerMockito.when(Utility.listIsNullOrEmpty(Mockito.anyList())).thenReturn(true);

        // 因为上面mock 了那个静态方法，所以这里返回的结果是0
        int sum = utilityHelper.sum(dataList);
        Assert.assertEquals(0, sum);

        // PowerMockito.verifyStatic(Mockito.times(1));
        // 校验mock 的那个类只进行了一次交互
        // 这里的这个类只有一个方法mock 了，所以也就只有这个方法被调用了一次的校验。
        PowerMockito.verifyStatic(Utility.class, Mockito.times(1));
        // 这一行必须有，如果没有那么前一行的 PowerMockito.verifyStatic(...) 将会报错。
        // 这里的意思是：要验证的是哪个已经mock的静态方法
        Utility.listIsNullOrEmpty(Mockito.anyList());
    }

    @Test
    public void testSum_2() {
        PowerMockito.when(Utility.listIsNullOrEmpty(Mockito.anyList())).thenReturn(false);

        int sum = utilityHelper.sum(dataList);

        Assert.assertEquals(1 + 2 + 3, sum);

        // 这里为什么出错呢？ 我只是把上面的那一句代码拷贝下来而以。
        // 答：这里还需要后跟一行，指出mock 的是哪个静态方法。现在就没有问题了。
        PowerMockito.verifyStatic(Utility.class, Mockito.atLeastOnce());
        Utility.listIsNullOrEmpty(Mockito.anyList());
    }

    @Test
    public void testProduct_1() {
        /**
         * 我们可以看到并没有对product中调用的listIsNotNullOrEmpty进行mock，那么为什么返回值是 1 呢？
         * 这个主要是因为我们在setup方法中对使用mockStatic方法对Utility.class进行了mock，
         * 那么此时该类中的所有方法实际上都已经被mock了，如果没有对某个方法进行具体mock返回值，
         * 则调用该方法时，会直接返回对应返回类型的默认值，并不会执行真正的方法。
         * 此例对于listIsNotNullOrEmpty方法来说，其返回类型为boolean型，其默认值为false，所以product方法返回 1 。
         */
        int product = utilityHelper.product(dataList);

        Assert.assertEquals(1, product);
    }

    /**
     * 被mock 的方法怎么调用原来的实际方法
     * 方法一: spy()
     */
    @Test
    public void testProduct_2() {
        /**
         * 因为在setup 方法中 mockStatic对Utility.class进行了mock，即：该类的所有方法都被mock了。
         * 如果我们想对已经mock的类的某个方法调用真实的方法，而不是调用mock方法，那么该如何处理呢？
         * 方法一：使用spy
         *      平常写时，可以紧跟在mockStatic方法后
         */
        PowerMockito.spy(Utility.class);

        // 使用了spy 之后，这里将会调用实际的方法，而非mock 之后的方法。
        int product = utilityHelper.product(dataList);

        Assert.assertEquals(6, product);
    }

    /**
     * 被mock 的方法怎么调用原来的实际方法
     * 方法二: thenCallRealMethod()
     */
    @Test
    public void testProduct_3() {
        /**
         * 方法二：调用 thenCallRealMethod() 方法
         */
        PowerMockito.when(Utility.listIsNotNullOrEmpty(Mockito.anyList())).thenCallRealMethod();

        int product = utilityHelper.product(dataList);

        Assert.assertEquals(6, product);
    }
}
