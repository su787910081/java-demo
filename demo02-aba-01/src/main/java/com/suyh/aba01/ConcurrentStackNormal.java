package com.suyh.aba01;

import lombok.ToString;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 解决了ABA 问题的栈
 */
@ToString
public class ConcurrentStackNormal {
    AtomicStampedReference<Node> top = new AtomicStampedReference<>(null, 1);

    public void push(Node node) {
        Node oldTop;
        int stamp;
        do {
            stamp = top.getStamp();
            oldTop = top.getReference();
            node.next = oldTop;
        } while (!top.compareAndSet(oldTop, node, stamp, stamp + 1));

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
        int stampOldStamp;
        do {
            stampOldStamp = top.getStamp();
            oldTop = top.getReference();
            if (oldTop == null) {
                return null;
            }
            newTop = oldTop.next;
            try {
                TimeUnit.SECONDS.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!top.compareAndSet(oldTop, newTop, stampOldStamp, stampOldStamp + 1));
        return oldTop;
    }
}
