package com.example.demo.util;

import com.example.demo.model.pojo.ResizableCapacityLinkedBlockingQueue;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ThreadUtil {

    public static ThreadPoolExecutor createThreadPoolExecutor(int corePoolSize,
                                                              int maxPoolSize,
                                                              long keepAliveTime,
                                                              TimeUnit timeUnit,
                                                              int queueCapacity) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                keepAliveTime,
                timeUnit,
                new ResizableCapacityLinkedBlockingQueue<>(queueCapacity),
                new ThreadPoolExecutor.AbortPolicy());
        return executor;
    }

    public static void setKeepAliveTime(ThreadPoolExecutor executor, long keepAliveTime, TimeUnit timeUnit) {
        executor.setKeepAliveTime(keepAliveTime, timeUnit);
    }
    public static void setPoolSize(ThreadPoolExecutor executor, int corePoolSize, int maximumPoolSize) {
        executor.setCorePoolSize(corePoolSize);
        if (maximumPoolSize > corePoolSize) {
            executor.setMaximumPoolSize(maximumPoolSize);
        }
    }
    public static void setCorePoolSize(ThreadPoolExecutor executor, int corePoolSize) {
        executor.setCorePoolSize(corePoolSize);
    }
    public static void setMaximumPoolSize(ThreadPoolExecutor executor, int maximumPoolSize) {
        executor.setMaximumPoolSize(maximumPoolSize);
    }

    /**
     * 设置队列容量
     * @param executor       线程池
     * @param queueCapacity  队列容量
     * @param handle         当队列剩余大小小于0时: 是否处理队列中的线程
     */
    public static void setQueueCapacity(ThreadPoolExecutor executor, int queueCapacity, boolean handle) {
        boolean isResizableQueue = executor.getQueue() instanceof ResizableCapacityLinkedBlockingQueue;
        if (! isResizableQueue) {
            return;
        }
        ResizableCapacityLinkedBlockingQueue<Runnable> queue = (ResizableCapacityLinkedBlockingQueue) executor.getQueue();
        queue.setCapacity(queueCapacity);
        if (handle) {
            Thread.currentThread().setName("setQueueCapacity");
            threadPoolStatus(executor);
            while (queue.remainingCapacity() < 0) {
                try {
                    Runnable r = queue.take();
                    executor.execute(r);
                } catch (Exception e) {
                    log.info("执行线程异常: {}", e.getLocalizedMessage());
                    e.printStackTrace();
                    return;
                }
            }
        }
    }
    private static void threadPoolStatus(ThreadPoolExecutor executor) {
        BlockingQueue<Runnable> queue = executor.getQueue();
        System.out.println(Thread.currentThread().getName() + ": " +
                "核心线程数：" + executor.getCorePoolSize() +
                " 活动线程数：" + executor.getActiveCount() +
                " 最大线程数：" + executor.getMaximumPoolSize() +
                " 线程池活跃度：" + divide(executor.getActiveCount(), executor.getMaximumPoolSize()) +
                " 任务完成数：" + executor.getCompletedTaskCount() +
                " 队列大小：" + (queue.size() + queue.remainingCapacity()) +
                " 当前排队线程数：" + queue.size() +
                " 队列剩余大小：" + queue.remainingCapacity() +
                " 队列使用度：" + divide(queue.size(), queue.size() + queue.remainingCapacity())
        );

        executor.getLargestPoolSize();
    }

    private static String divide(int num1, int num2) {
        return String.format("%1.2f%%",
                Double.parseDouble(num1 + "") / Double.parseDouble(num2 + "") * 100);
    }

}
