package com.suyh.simple;

import static org.mockito.Mockito.*;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.LinkedList;
import java.util.List;

public class SimpleTest01 {
    @Test
    public void test01() {
        // mock creation 创建mock对象
        List mockedList = mock(List.class);

        //using mock object 使用mock对象
        mockedList.add("one");
        mockedList.clear();

        //verification 验证
        verify(mockedList).add("one");
        verify(mockedList).clear();
    }

    /**
     * 如何做一些测试桩(Stub)
     */
    @Test
    public void test02() {
        // 你可以mock 具体的类型，不仅只是接口
        LinkedList mockedList = mock(LinkedList.class);

        // 测试桩(stubbing)
        when(mockedList.get(0)).thenReturn("first");
        when(mockedList.get(1)).thenThrow(new RuntimeException());

        // first
        System.out.println(mockedList.get(0));

        try {
            // 这里将会抛出异常
            mockedList.get(1);
        } catch (RuntimeException e) {
            System.out.println("get(1)");
        }

        // 999 没有打桩，因此这里输入null
        System.out.println(mockedList.get(999));

        // 验证get(0) 被调用的次数
        // TODO: 这个怎么看呀，没理解
        verify(mockedList).get(0);

        // 参数匹配器
        // 使用内置的参数匹配器, 这里使用任何int 类型来调用get 方法都只会返回字符串 "element"
        when(mockedList.get(anyInt())).thenReturn("element");

        // TODO: 不懂，这个isValid() 方法怎么实现
        // 使用自定义的参数匹配器(在isValid() 中返回你自己的匹配实现)
        // when(mockedList.contains(argThat(isValid()))).thenReturn("element");

        System.out.println(mockedList.get(999));

        // TODO: 这里为什么出错了？？？
        // 答：这里的verify 如果不指定第二个参数，那么默认为1 次。因为前面调用了多次，所以出错了。这里改为4 就对了。
        // 你也可以验证参数匹配器
        // verify(mockedList).get(anyInt());  // 默认校验是一次调用
        verify(mockedList, times(4)).get(anyInt());

        // TODO: 这个是什么意思？
        // 使用参数匹配器的注意点：如果你使用参数匹配器，所有参数都必须由匹配器提供。
        // 答：也就是说某个方法的参数都是参数匹配器(如下面的: anyInt()、anyString()、eq("third argument") 这些都是参数匹配器)
        //      参数匹配器的意思就是匹配实际传入的参数是什么，或者匹配到这个参数也可以。
        // verify 的校验方式，首先指定校验哪一个mock 对象，然后指定校验哪个方法。以及校验传入的参数
        // TODO: 正确的示例如下:
        // verify(mockedList).get(anyInt(), anyString(), eq("third argument"));
        // TODO: 错误的示例如下：
        // 因为第三个是字符串不是参数匹配器
        // verify(mockedList).get(anyInt(), anyString(), "third argument");
    }

    /**
     * 验证函数的确切、最少、从未调用次数
     */
    @Test
    public void test03() {
        LinkedList mockedList = mock(LinkedList.class);

        mockedList.add("once");

        mockedList.add("twice");
        mockedList.add("twice");


        mockedList.add("three times");
        mockedList.add("three times");
        mockedList.add("three times");

        mockedList.add("five times");

        // 下面两个验证的效果是一样的，因为verify 默认验证的就是times(1)
        verify(mockedList).add("once");
        verify(mockedList, times(1)).add("once");

        // 验证具体的执行次数
        // 补充：这里就没有使用参数匹配器，而是绝对匹配("twice" "three times" 的值)
        verify(mockedList, times(2)).add("twice");
        verify(mockedList, times(3)).add("three times");

        // 使用never() 进行难，never 相当于times(0)
        verify(mockedList, never()).add("never happened");

        // 使用atLeast() / atMost() 调用次数的范围校验
        // add() 方法参数为"three times" 时，有最少1 次的调用
        verify(mockedList, atLeastOnce()).add("three times");
        // add() 方法的参数为"three times" 时，有最多5 次的调用
        verify(mockedList, atMost(5)).add("three times");
        // add() 方法的参数为"five times" 时，有最少2 次的调用  当前代码的结果是失败
        // verify(mockedList, atLeast(2)).add("five times");
    }

    /**
     * 为返回值为void 的函数通过打桩(Stub)抛出异常
     * 最初，stubVoid(Object) 函数用于为无返回值的函数打桩，现在它已经过时。
     */
    @Test
    public void test04() {
        LinkedList mockedList = mock(LinkedList.class);

        doThrow(new RuntimeException("Exception message")).when(mockedList).clear();

        try {
            // 调用这句代码时会抛出异常
            mockedList.clear();
        } catch (RuntimeException e) {
            System.out.println("You are right.");
        }
    }

    /**
     * 验证执行顺序
     */
    @Test
    public void test05() {
        // A. 验证mock 一个对象的函数执行顺序
        List singleMock = mock(List.class);

        singleMock.add("was added first");
        singleMock.add("was added second");

        // 为该mock 对象创建一个inOrder 对象
        InOrder inOrderSingle = inOrder(singleMock);

        // 确保add 函数首先执行的是add("was added first") 然后才是add("was added second")
        inOrderSingle.verify(singleMock).add("was added first");
        inOrderSingle.verify(singleMock).add("was added second");

        // B. 验证多个mock 对象的函数执行顺序
        List firstMock = mock(List.class);
        List secondMock = mock(List.class);

        firstMock.add("was called first");
        secondMock.add("was called second");

        // 为这两个Mock 对象创建inOrder 对象
        InOrder inOrderMore = inOrder(firstMock, secondMock);

        // 验证它们的执行顺序
        inOrderMore.verify(firstMock).add("was called first");
        inOrderMore.verify(secondMock).add("was called second");
    }

    /**
     * 难保交互(interaction) 操作不会执行在mock 对象上
     */
    @Test
    public void test06() {
        List mockOne = mock(List.class);
        List mockTwo = mock(List.class);
        List mockThree = mock(List.class);

        mockOne.add("one");

        // 普通验证
        verify(mockOne).add("one");

        // 从未被执行的交互
        verify(mockOne, never()).add("two");

        // mockTwo.clear();

        // 难mock 对象没有交互过
        // 这里好像指的是这个mock 对象没有做任何的方法调用
        verifyZeroInteractions(mockTwo, mockThree);
    }

    /**
     * 查询冗余的调用
     * 这个似乎不太推荐呢，因为有一个更好可读性更好的 never()。
     */
    @Test
    public void test07() {
        LinkedList mockedList = mock(LinkedList.class);
        mockedList.add("one");
        mockedList.add("two");

        verify(mockedList).add("one");

        // 下面的验证将会失败。不推荐使用！！！
        // verifyNoMoreInteractions(mockedList);
    }

    /**
     * 为连续调用做测试桩
     */
    @Test
    public void test08() {
        List mockList = mock(List.class);

        when(mockList.add(any()))
                .thenThrow(new RuntimeException("first called"))
                .thenReturn(false);

        try {
            // 第一次调用将会抛出异常
            mockList.add("first");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        // 从第二次调用开始，全部都是返回false
        for (int i = 0; i < 10; i++) {
            Assert.assertFalse("之后的调用都返回false", mockList.add("second called"));
        }

        // 连续调用有另一种简短版本
        when(mockList.get(anyInt()))
                .thenReturn("one", "two", "three");
    }

    /**
     * 为回调做测试桩
     * 这个似乎可以使用 thenReturn() 和thenThrow() 来处理，它是一个有争议的特征
     */
    @Test
    public void test09() {
        // when(mock.someMethod(anyString())).thenAnswer(new Answer() {

        //     @Override
        //     public Object answer(InvocationOnMock invocation) throws Throwable {
        //         Object[] args = invocation.getArguments();
        //         Object mock = invocation.getMock();

        //         return "called with arguments: " + args;
        //     }
        // });

        // // 输出: "called with arguments: foo"
        // System.out.println(mock.someMethod("foo"));
    }

    /**
     * 为返回值为void 的函数打桩
     * doReturn()
     * doThrow()
     * doAnswer()
     * doNothing()
     * doCallRealMethod()
     *
     * 测试void 函数
     * 在受监控的对象上测试函数
     * 不只一次的测试为同一个函数，在测试过程中改变mock 对象的行为
     */
    @Test
    public void test10() {
        LinkedList mockedList = mock(LinkedList.class);

        doThrow(new RuntimeException("clear exception")).when(mockedList).clear();

        try {
            // 这里将会抛出异常
            mockedList.clear();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 监控真实对象 spy
     * 看了下说明，似乎这个并不实用，也不需要用的感觉。暂时先不管了。
     */
    @Test
    public void test11() {

    }

    /**
     * 自定义验证失败信息(Since 2.0.0)
     */
    @Test
    public void test12() {
        List mock = mock(List.class);
        mock.add("a");
        mock.get(0);
        mock.get(0);
        verify(mock, times(2).description("至少调用两次")).get(anyInt());
    }

}
