package com.linkworld;

/**
 * <p>进程块</p>
 *
 * @author <a href="mailto:hhjian.top@qq.com">hhjian</a>
 * @since 2017.11.21
 */
public class Jcb {
    /**
     * 进程号
     */
    private String id;
    /**
     * 进程状态 0-Ready 1-Run 2-Finish
     */
    private int status;
    /**
     * 进程到达时间
     */
    private int arrvieTime;
    /**
     * 服务时间
     */
    private int serviceTime;
    /**
     * 已服务时间
     */
    private int runTime;
    /**
     * 完成时间
     */
    private int finishTime;

    public Jcb() {
    }

    public Jcb(String id, int status, int arrvieTime, int serviceTime) {
        this.id = id;
        this.status = status;
        this.arrvieTime = arrvieTime;
        this.serviceTime = serviceTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getArrvieTime() {
        return arrvieTime;
    }

    public void setArrvieTime(int arrvieTime) {
        this.arrvieTime = arrvieTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public int getRunTime() {
        return runTime;
    }

    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    public int getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }
}
