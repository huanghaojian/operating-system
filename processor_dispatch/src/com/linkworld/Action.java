package com.linkworld;

import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * <p>RR算法处理机调度</p>
 *
 * @author <a href="mailto:hhjian.top@qq.com">hhjian</a>
 * @since 2017.11.21
 */
public class Action {
    /**
     * 等待状态
     */
    public static final int READ_STATUS = 0;
    /**
     * 就绪状态
     */
    public static final int RUN_STATUS = 1;
    /**
     * 完成状态
     */
    public static final int FINISH_STATUS = 2;
    /**
     * 等待队列
     */
    private final LinkedList<Jcb> readQueue = new LinkedList<>();
    /**
     * 就绪队列
     */
    private final LinkedList<Jcb> runQueue = new LinkedList<>();
    /**
     * 完成队列
     */
    private final LinkedList<Jcb> finishQueue = new LinkedList<>();
    /**
     * 时间片
     */
    private int timeSlices = 1;
    /**
     * 当前时间
     */
    private int currentTime = 0;

    /**
     * 初始化
     *
     * @param queue      进程队列
     * @param timeSlices 时间片
     */
    public Action(LinkedList<Jcb> queue, int timeSlices) {
        this.timeSlices = timeSlices;
        queue.forEach(jcb -> {
            if (jcb.getStatus() == RUN_STATUS) {
                runQueue.add(jcb);
            } else {
                readQueue.add(jcb);
            }
        });
    }

    /**
     * RR算法调度
     */
    public void dispatchRR() {
        System.out.println("--------------------- RR算法执行流 时间片为: " + timeSlices + " ----------------------");
        System.out.println("进程名\t\t本次开始时间\t\t本次结束时间");
        // 程序未执行完
        while (!readQueue.isEmpty()) {
            // 是否有新进程进入系统
            pushRunQueue();
            // 就绪队列不为空
            while (!runQueue.isEmpty()) {
                // 是否有新进程进入系统
                pushRunQueue();
                // 取出第一个进程运行
                Jcb jcb = runQueue.pollFirst();
                int stratTime = currentTime;
                int runTime = jcb.getRunTime();
                int serviceTime = jcb.getServiceTime();
                int execTime = serviceTime - runTime;
                if (execTime > timeSlices) {
                    // 时间片用完,重新进入就绪队列,先放新进程进入就绪队列
                    jcb.setRunTime(runTime + timeSlices);
                    currentTime += timeSlices;
                    runQueue.add(jcb);
                    System.out.println(jcb.getId() + "\t\t\t" + stratTime + "\t\t\t\t" + currentTime);
                } else {
                    // 进入完成队列
                    currentTime += execTime;
                    jcb.setRunTime(runTime + execTime);
                    jcb.setStatus(FINISH_STATUS);
                    jcb.setFinishTime(currentTime);
                    finishQueue.add(jcb);
                    System.out.println(jcb.getId() + "\t\t\t" + stratTime + "\t\t\t\t" + currentTime);
                }
            }
        }

        finishPrint();
    }

    /**
     * 加入就绪队列
     */
    private void pushRunQueue() {
        // 新进程插入就绪队列头
        LinkedList linkedList = readQueue.stream()
                .filter(jcb -> currentTime >= jcb.getArrvieTime())
                .collect(Collectors.toCollection(LinkedList::new));
        readQueue.removeAll(linkedList);
        runQueue.addAll(0, linkedList);
    }

    /**
     * 统计出进程的周转时间T和带权周转时间W
     */
    private void finishPrint() {
        System.out.println("----------------------------------------------------------------------");
        System.out.println("进程名\t\t到达时间\t\t服务时间\t\t完成时间\t\t周转时间\t\t带权周转时间");
        finishQueue.forEach(jcb -> {
            int Ta = jcb.getArrvieTime();
            int Ts = jcb.getServiceTime();
            int Tf = jcb.getFinishTime();
            int T = Tf - Ta;
            double W = (double) T / Ts;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(jcb.getId()).append("\t\t\t")
                    .append(Ta).append("\t\t\t")
                    .append(Ts).append("\t\t\t")
                    .append(Tf).append("\t\t\t")
                    .append(T).append("\t\t\t")
                    .append(W).append("\t\t\t");
            System.out.println(stringBuilder.toString());
        });
    }
}
