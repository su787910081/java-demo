package com.suyh.aba01;

import lombok.ToString;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 有ABA 问题的栈
 */
@ToString
public class ConcurrentStackAba {
    AtomicReference<Node> top = new AtomicReference<>();

    public void push(Node node) {
        Node oldTop;
        do {
            oldTop = top.get();
            node.next = oldTop;
        }
        while (!top.compareAndSet(oldTop, node));
    }

    /**
     * 取出栈顶元素，并将栈顶的下一个元素作为新的栈顶元素，然后将栈顶元素返回
     *
     * @param time 这里模拟线程刚好被CPU 切换
     * @return 栈顶元素
     */
    public Node pop(int time) {
        Node newTop;
        Node oldTop;
        do {
            oldTop = top.get();
            if (oldTop == null) {
                return null;
            }
            newTop = oldTop.next;
            try {
                TimeUnit.SECONDS.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!top.compareAndSet(oldTop, newTop));
        return oldTop;
    }
}
