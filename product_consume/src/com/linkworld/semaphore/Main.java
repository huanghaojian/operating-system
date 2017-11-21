package com.linkworld.semaphore;

/**
 * <p>用信号量机制实现进程同步,实现生产者消费者问题</p>
 *
 * @author <a href="mailto:hhjian.top@qq.com">hhjian</a>
 * @since 2017.11.20
 */
public class Main {
    /**
     * 生产者数量
     */
    private static final int PRODUCER_SIZE = 5;
    /**
     * 消费者数量
     */
    private static final int CONSUMER_SIZE = 4;
    /**
     * 仓库
     */
    private static Repository repository = new Repository();

    public static void main(String[] args) {
        // 启动消费者线程
        for (int i = 0; i < CONSUMER_SIZE; i++) {
            new Thread(() -> {
                System.out.println("启动消费者" + Thread.currentThread().getName());
                while (true) {
                    try {
                        repository.consume();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        // 启动生产者线程
        for (int i = 0; i < PRODUCER_SIZE; i++) {
            new Thread(() -> {
                System.out.println("启动生产者" + Thread.currentThread().getName());
                while (true) {
                    try {
                        String product = "产品";
                        repository.produce(product);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
