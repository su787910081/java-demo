package com.suyh.aba01.test;

import com.suyh.aba01.ConcurrentStackAba;
import com.suyh.aba01.ConcurrentStackNormal;
import com.suyh.aba01.Node;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

@Slf4j
public class AbaTest {

    /**
     * ABA问题的复现：
     * 首先在我们的一个栈中，有两个元素(A, B)
     * 在线程1 中取出栈顶元素
     * 然后在线程2 中直先取出栈顶元素，然后新压入两个新元素(C, D)，最后将栈顶元素放回栈中。
     * 控制线程1 先执行，但是在取出栈顶元素之后与更新栈顶元素之间的空隙时间片被收回。
     * 使得线程2 在这个间隙执行了。
     * 然后线程1 再回来执行最后的操作。
     * 本来期望栈中元素为：B <- C <- D <- A
     * 但是由于CAS 的ABA 问题的存在，最终导致了结果为：A
     */
    @Test
    public void testAba() throws InterruptedException {
        ConcurrentStackAba stack = new ConcurrentStackAba();
        stack.push(new Node("A"));
        stack.push(new Node("B"));

        new Thread(() -> stack.pop(3), "thread A").start();
        new Thread(() -> {
            Node first = stack.pop(1);
            stack.push(new Node("D"));
            stack.push(new Node("C"));
            stack.push(first);
        }, "thread B").start();

        TimeUnit.SECONDS.sleep(4);
        for (Node node = stack.pop(0); node != null; node = stack.pop(0)) {
            log.info("node current element: {}", node.item);
        }
    }

    /**
     * ABA问题的解决(使用AtomicStampedReference)：
     * 最终的栈中元素为：B <- C <- D <- A
     */
    @Test
    public void testAbaOk() throws InterruptedException {
        ConcurrentStackNormal stack = new ConcurrentStackNormal();
        stack.push(new Node("A"));
        stack.push(new Node("B"));

        new Thread(() -> stack.pop(3), "thread A").start();
        new Thread(() -> {
            Node first = stack.pop(1);
            stack.push(new Node("D"));
            stack.push(new Node("C"));
            stack.push(first);
        }, "thread B").start();

        TimeUnit.SECONDS.sleep(4);
        for (Node node = stack.pop(0); node != null; node = stack.pop(0)) {
            log.info("node current element: {}", node.item);
        }
    }


}
