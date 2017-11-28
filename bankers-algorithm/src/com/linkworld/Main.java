package com.linkworld;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    /**
     * 可用资源
     */
    private static int[] available;
    /**
     * 最大资源矩阵
     */
    private static int[][] maxMatrix;
    /**
     * 已分配资源矩阵
     */
    private static int[][] allocationMatrix;
    /**
     * 需求矩阵
     */
    private static int[][] needMatrix;
    /**
     * 进程数
     */
    private static int processNum;
    /**
     * 资源数
     */
    private static int resourceNum;
    /**
     * 请求进程序号
     */
    private static int requestProcess;
    /**
     * 请求各资源的个数
     */
    private static int[] requestResource;
    /**
     * 各进程是否已完成
     */
    private static boolean[] isFinishProcesses;
    /**
     * 可用资源
     */
    private static int[] availableWorks;
    /**
     * 处于已完成的进程
     */
    private static List finishProcesses = new ArrayList();

    public static void main(String[] args) throws IOException {
        while (true) {
            readAvailableList();
            readMaxList();
            readAllocationList();
            setNeedList();
            printInfo();
            Scanner scanner = new Scanner(System.in);
            System.out.println("输入发起请求的进程(0-" + (processNum - 1) + "):");
            requestProcess = scanner.nextInt();
            System.out.print("输入请求资源的数目:");
            requestResource = new int[resourceNum];
            for (int i = 0; i < resourceNum; i++) {
                char ch = (char) (65 + i);
                System.out.println("请求资源" + ch + "个数:");
                requestResource[i] = scanner.nextInt();
            }
            runBanker();
            System.out.println("\n\n需要重新运行吗?(继续-y;终止-其他)");
            String choose = scanner.next();
            if (!"y".equalsIgnoreCase(choose)) {
                break;
            }
        }
    }

    /**
     * 执行银行家算法
     */
    private static void runBanker() throws IOException {
        System.out.println("开始执行银行家算法...");

        //检查是否满足条件Request<=Need
        for (int i = 0; i < resourceNum; i++) {
            if (requestResource[i] > needMatrix[requestProcess][i]) {
                System.out.println("第" + i + "个进程请求资源不成功");
                System.out.println("原因：超出该进程尚需的资源的最大数量!");
                return;
            }
        }
        //检查是否满足条件Request<=Available
        for (int i = 0; i < resourceNum; i++) {
            if (requestResource[i] > available[i]) {
                char ch = (char) (65 + i);
                System.out.println("请求资源" + ch + "不成功");
                System.out.println("原因：系统中无足够的资源!");
                return;
            } else {
                //试分配，更新各相关数据结构
                available[i] -= requestResource[i];
                allocationMatrix[requestProcess][i] += requestResource[i];
                needMatrix[requestProcess][i] -= requestResource[i];
            }
        }
        System.out.println("试分配完成...");
        if (testSafety()) {
            //使用安全性算法检查，若满足，则正式分配
            System.out.println("安全性算法检查通过,安全序列:" + finishProcesses);
            allocateSource();
        } else {
            //否则恢复试分配前状态
            recoverTryAllocate();
            System.out.println("安全性算法检查失败,还原状态");
        }
    }

    /**
     * 安全性检验
     */
    private static boolean testSafety() {
        availableWorks = available;
        isFinishProcesses = new boolean[processNum];

        int processIndex = findProcess();
        while (-1 != processIndex) {
            for (int i = 0; i < resourceNum; i++) {
                availableWorks[i] += allocationMatrix[processIndex][i];
                isFinishProcesses[processIndex] = true;
            }
            processIndex = findProcess();
        }
        System.out.println("进程通过情况:" + Arrays.toString(isFinishProcesses));
        for (boolean test : isFinishProcesses) {
            if (!test) {
                return false;
            }
        }
        return true;
    }

    /**
     * 找出符合 FINISH==false; NEED<=Work; 的进程
     *
     * @return 符合要求的进程序号
     */
    private static int findProcess() {
        // 判断请求进程请求资源是否结束
        if (!isFinishProcesses[requestProcess]) {
            boolean flag = Arrays.stream(needMatrix[requestProcess]).filter(need -> need != 0).count() == 0;
            if (flag) {
                for (int i = 0; i < resourceNum; i++) {
                    availableWorks[i] += allocationMatrix[requestProcess][i];
                    isFinishProcesses[requestProcess] = true;
                }
                finishProcesses.add("P" + (requestProcess + 1));
            }
        }
        for (int i = 0; i < processNum; i++) {
            // FINISH==false
            if (!isFinishProcesses[i]) {
                // NEED<=Work
                boolean flag = true;
                for (int j = 0; j < resourceNum; j++) {
                    if (needMatrix[i][j] > availableWorks[j]) {
                        flag = false;
                    }
                }
                if (flag) {
                    finishProcesses.add("P" + (i + 1));
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 开始正式分配资源（修改Allocation_list.txt）
     */
    private static void allocateSource() throws IOException {
        System.out.println("\n开始给第" + requestProcess + "个进程分配资源...");
        System.out.println("模拟写入");
//         为了方便测试,不真实写入!
//        File file = new File("src/com/linkworld/Allocation_list.txt");
//        BufferedWriter writer =
//                new BufferedWriter(new OutputStreamWriter(
//                        new FileOutputStream(file), "utf-8"));
//        for (int i = 0; i < processNum; i++) {
//            for (int j = 0; j < resourceNum; j++) {
//                writer.write(String.valueOf(allocationMatrix[i][j]));
//                writer.write("\t");
//            }
//            writer.newLine();
//        }
//        writer.close();
        System.out.println("分配完成，已更新Allocation_list.txt");
    }

    /**
     * 恢复试分配前状态
     */
    private static void recoverTryAllocate() {
        for (int i = 0; i < resourceNum; i++) {
            available[i] += requestResource[i];
            allocationMatrix[requestProcess][i] -= requestResource[i];
            needMatrix[requestProcess][i] += requestResource[i];
        }
    }

    /**
     * 读入可用资源矩阵
     */
    private static void readAvailableList() throws FileNotFoundException {
        File file = new File("src/com/linkworld/Available_list.txt");
        Scanner scanner = new Scanner(file);
        resourceNum = scanner.nextInt();
        available = new int[resourceNum];
        for (int i = 0; i < resourceNum; i++) {
            available[i] = scanner.nextInt();
        }
    }

    /**
     * 读入最大资源矩阵
     */
    private static void readMaxList() throws FileNotFoundException {
        File file = new File("src/com/linkworld/Max_list.txt");
        Scanner scanner = new Scanner(file);
        processNum = scanner.nextInt();
        maxMatrix = new int[processNum][resourceNum];
        for (int i = 0; i < processNum; i++) {
            for (int j = 0; j < resourceNum; j++) {
                maxMatrix[i][j] = scanner.nextInt();
            }
        }
    }

    /**
     * 读入已分配资源矩阵
     */
    private static void readAllocationList() throws FileNotFoundException {
        File file = new File("src/com/linkworld/Allocation_list.txt");
        Scanner scanner = new Scanner(file);
        allocationMatrix = new int[processNum][resourceNum];
        for (int i = 0; i < processNum; i++) {
            for (int j = 0; j < resourceNum; j++) {
                allocationMatrix[i][j] = scanner.nextInt();
            }
        }
    }

    /**
     * 设置需求资源矩阵
     */
    private static void setNeedList() {
        needMatrix = new int[processNum][resourceNum];
        for (int i = 0; i < processNum; i++) {
            for (int j = 0; j < resourceNum; j++) {
                needMatrix[i][j] = maxMatrix[i][j] - allocationMatrix[i][j];
                available[j] = available[j] - allocationMatrix[i][j];
            }
        }
    }

    /**
     * 输出详细信息
     */
    private static void printInfo() {
        System.out.println("进程数: " + processNum + " 资源个数: " + resourceNum);
        System.out.println("最大需求矩阵Max\t\t已分配矩阵Allocation\t需求矩阵Need\t\t可用资源向量Available");
        for (int i = 0; i < processNum; i++) {
            printArray(maxMatrix[i]);
            printArray(allocationMatrix[i]);
            printArray(needMatrix[i]);
            if (i == 0) {
                printArray(available);
            }
            System.out.println();
        }
    }

    /**
     * 输出整型数组
     *
     * @param target 目标数组
     */
    private static void printArray(int[] target) {
        for (int x : target) {
            System.out.print(x + " ");
        }
        System.out.print("\t\t\t\t");
    }
}
