package com.linkworld;

import java.util.LinkedList;

import static com.linkworld.Action.READ_STATUS;
import static com.linkworld.Action.RUN_STATUS;

/**
 * <p>主函数</p>
 *
 * @author <a href="mailto:hhjian.top@qq.com">hhjian</a>
 * @since 2017.11.21
 */
public class Main {

    public static void main(String[] args) {
        LinkedList<Jcb> queue = new LinkedList<>();
        Jcb jcb1 = new Jcb("A", RUN_STATUS, 0, 4);
        queue.add(jcb1);

        Jcb jcb2 = new Jcb("B", READ_STATUS, 1, 3);
        queue.add(jcb2);

        Jcb jcb3 = new Jcb("C", READ_STATUS, 2, 4);
        queue.add(jcb3);

        Jcb jcb4 = new Jcb("D", READ_STATUS, 3, 2);
        queue.add(jcb4);

        Jcb jcb5 = new Jcb("E", READ_STATUS, 4, 4);
        queue.add(jcb5);

        // 时间片为1的RR算法调度
        Action action1 = new Action(queue, 1);
        action1.dispatchRR();

        // 时间片为4的RR算法调度
        Action action4 = new Action(queue, 4);
        action4.dispatchRR();
    }
}
