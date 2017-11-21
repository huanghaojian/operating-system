package com.linkworld.semaphore;

import java.util.concurrent.Semaphore;

/**
 * <p>产品仓库</p>
 *
 * @author <a href="mailto:hhjian.top@qq.com">hhjian</a>
 * @since 2017.11.20
 */
public class Repository {
    /**
     * 仓库容量
     */
    private static final int REPOSITORY_SIZE = 10;
    /**
     * 仓库缓冲区
     */
    private final Object[] items = new Object[REPOSITORY_SIZE];
    /**
     * 非满锁,当仓库空时迫使消费者等待
     */
    private final Semaphore notFull = new Semaphore(REPOSITORY_SIZE);
    /**
     * 非空锁,当仓库满时迫使生产者等待
     */
    private final Semaphore notEmpty = new Semaphore(0);
    /**
     * 核心锁,用于线程间的互斥
     */
    private final Semaphore mutex = new Semaphore(1);
    /**
     * 产品进仓库的索引,(生产索引)
     */
    private int putStr = 0;
    /**
     * 产品出仓库的索引,(消费索引)
     */
    private int takeptr = 0;
    /**
     * 仓库当前容量
     */
    private int count = 0;
    /**
     * 产品ID
     */
    private int productId = 0;

    /**
     * 生产商品,保存到仓库中
     *
     * @param object
     * @throws InterruptedException
     */
    public void produce(Object object) throws InterruptedException {
        try {
            // 获取非满锁,保证非满
            if (notFull.tryAcquire()) {
                // 获取核心锁,保证不冲突
                mutex.acquire();
                int produceIndex = putStr + 1;
                items[putStr] = object.toString() + (++productId);
                if (++putStr == items.length) {
                    putStr = 0;
                }
                ++count;
                producePrintRepository(object, produceIndex);
            } else {
                System.out.println("仓库已满!,生产者等待");
            }
        } finally {
            Thread.sleep(1000);
            // 释放核心锁
            mutex.release();
            // 增加非空信号量,允许消费者消费产品
            notEmpty.release();
        }
    }

    /**
     * 消费商品,从仓库中取出
     *
     * @return
     * @throws InterruptedException
     */
    public Object consume() throws InterruptedException {
        try {
            // 获取非空锁,保证仓库非空
            if (notEmpty.tryAcquire()) {
                // 获取核心锁,保证不冲突
                mutex.acquire();
                int produceIndex = takeptr + 1;
                Object object = items[takeptr];
                items[takeptr] = null;
                if (++takeptr == items.length) {
                    takeptr = 0;
                }
                --count;
                consumePrintRepository(object, produceIndex);
                return object;
            } else {
                System.out.println("仓库为空!,消费者等待");
            }
        } finally {
            Thread.sleep(1000);
            // 释放核心锁
            mutex.release();
            // 增加非满信号量,允许生产者生产产品
            notFull.release();
        }
        return null;
    }

    /**
     * 记录生产活动,输出仓库当前情况
     *
     * @param object       被生产的产品
     * @param produceIndex 产品所放的位置
     */
    private synchronized void producePrintRepository(Object object, int produceIndex) {
        System.out.println("------------生产产品[" + object + productId + "]到仓库的第 [" + produceIndex + "] 个位置" +
                " 生产者进程[" + Thread.currentThread().getName() + "]--------------");
        printRepository();
    }

    /**
     * 记录生产活动,输出仓库当前情况
     *
     * @param object       被消费的产品
     * @param consumeIndex 取出产品的位置
     */
    private synchronized void consumePrintRepository(Object object, int consumeIndex) {
        System.out.println("------------消费仓库的第 [" + consumeIndex + "] 个位置的产品[" + object + "]" +
                " 消费者进程[" + Thread.currentThread().getName() + "]--------------");
        printRepository();
    }

    /**
     * 输出仓库当前情况
     */
    private synchronized void printRepository() {
        for (int i = 0; i < REPOSITORY_SIZE; i++) {
            System.out.print((i + 1) + ": " + items[i]);
            if (putStr == i) {
                System.out.print(" <-- 存放下一个产品位置");
            }
            if (takeptr == i) {
                System.out.print(" <-- 下一个要被消费的产品位置");
            }
            System.out.println();
        }
        System.out.println();
    }
}
