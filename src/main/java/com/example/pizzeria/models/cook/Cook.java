package com.example.pizzeria.models.cook;

import com.example.pizzeria.models.task.ICookTask;
import lombok.Getter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class Cook extends Thread {
    private static AtomicInteger ID_GENERATOR = new AtomicInteger();
    private final Integer cookId;
    private final String cookName;
    private CookStatus status;
    private final BlockingQueue<ICookTask> tasksQueue;

    public Cook(){
        this.cookId = ID_GENERATOR.getAndIncrement();
        this.cookName = "Cook " + cookId;
        this.status = CookStatus.FREE;
        this.tasksQueue = new LinkedBlockingQueue<>();
    }

    public Cook(String cookName) {
        this.cookId = ID_GENERATOR.getAndIncrement();
        this.cookName = cookName;
        this.status = CookStatus.FREE;
        this.tasksQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public void run() {
        while (true) {
            try {
                ICookTask task = tasksQueue.take();
                task.execute(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addTask(ICookTask task) {
        tasksQueue.add(task);
    }

    public void pauseCook() {
        this.status = CookStatus.PAUSED;
    }

    public void resumeCook() {
        this.status = CookStatus.FREE;
    }

    public void setBusy() {
        if(this.status.equals(CookStatus.FREE)) {
            this.status = CookStatus.BUSY;
        }
    }

    public void setFree() {
         if(this.status.equals(CookStatus.BUSY)) {
             this.status = CookStatus.FREE;
         }
    }
}
