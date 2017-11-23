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
        LinkedList<JCB> queue = new LinkedList<>();
        JCB jcb1 = new JCB("A", RUN_STATUS, 0, 4);
        queue.add(jcb1);

        JCB jcb2 = new JCB("B", READ_STATUS, 1, 3);
        queue.add(jcb2);

        JCB jcb3 = new JCB("C", READ_STATUS, 2, 4);
        queue.add(jcb3);

        JCB jcb4 = new JCB("D", READ_STATUS, 3, 2);
        queue.add(jcb4);

        JCB jcb5 = new JCB("E", READ_STATUS, 4, 4);
        queue.add(jcb5);

        // 时间片为1的RR算法调度
        Action action_1 = new Action(queue, 1);
        action_1.dispatchRR();

        // 时间片为4的RR算法调度
        Action action_4 = new Action(queue, 4);
        action_4.dispatchRR();
    }
}
