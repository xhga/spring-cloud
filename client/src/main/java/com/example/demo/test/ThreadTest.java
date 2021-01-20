package com.example.demo.test;

import com.example.demo.model.pojo.ResizableCapacityLinkedBlockingQueue;
import com.example.demo.util.ThreadUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ThreadTest {

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

    public static void main(String[] args) throws InterruptedException {
        /*ThreadPoolExecutor threadPoolExecutor = ThreadUtil.createThreadPoolExecutor(
                2,
                5,
                10,
                TimeUnit.SECONDS,
                3);
        testThread(threadPoolExecutor);*/
        ThreadPoolExecutor threadPoolExecutor = ThreadUtil.createThreadPoolExecutor(
                1,
                2,
                10,
                TimeUnit.SECONDS,
                4);
        testThread2(threadPoolExecutor);

    }
    private static void testThread2(ThreadPoolExecutor threadPoolExecutor) throws InterruptedException{
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            threadPoolExecutor.execute(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    Thread.sleep(2000);
                    DateFormat dateInstance = new SimpleDateFormat("HH:mm:ss sss");
                    System.out.println(dateInstance.format(new Date())+"第1次设置线程: i="+ finalI);
                    threadPoolStatus(threadPoolExecutor);
                }
            });
            Thread.currentThread().setName("main-1");
            threadPoolStatus(threadPoolExecutor);
        }
        Thread.sleep(3100);
        System.out.println("-----------------------");
        threadPoolStatus(threadPoolExecutor);
        ThreadUtil.setQueueCapacity(threadPoolExecutor, 1, true);
        //setMaximumPoolSize(threadPoolExecutor, 1);
        Thread.sleep(10000);
        threadPoolExecutor.shutdown();
    }
    private static void testThread(ThreadPoolExecutor threadPoolExecutor) throws InterruptedException{
        System.out.println("----------线程test:-----------");
        for (int i = 0; i < 2; i++) {
            int finalI = i;
            threadPoolExecutor.execute(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    Thread.sleep(2000);
                    //System.out.println("第1次设置线程: i="+ finalI);
                    //threadPoolStatus(threadPoolExecutor);
                }
            });
            Thread.currentThread().setName("main-1");
            threadPoolStatus(threadPoolExecutor);
        }
        Thread.sleep(3000);
        for (int i = 0; i < 3; i++) {
            int finalI = i;
            threadPoolExecutor.execute(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    Thread.sleep(2000);
                    System.out.println("第2次设置线程: i="+ finalI);
                    //threadPoolStatus(threadPoolExecutor);
                }
            });
            Thread.currentThread().setName("main-2");
            threadPoolStatus(threadPoolExecutor);
        }
        Thread.sleep(4000);
        Thread.currentThread().setName("main-2_after");
        threadPoolStatus(threadPoolExecutor);
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            try {
                threadPoolExecutor.execute(new Runnable() {
                    @SneakyThrows
                    @Override
                    public void run() {
                        Thread.sleep(2000);
                        //System.out.println("第3次设置线程: i="+ finalI);
                        //threadPoolStatus(threadPoolExecutor);
                    }
                });
            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println("修改线程池参数：");
                ThreadUtil.setQueueCapacity(threadPoolExecutor, 1, true);
                ThreadUtil.setMaximumPoolSize(threadPoolExecutor, 9);
            }
            Thread.currentThread().setName("main-3");
            threadPoolStatus(threadPoolExecutor);
        }

        Thread.sleep(5000);
        threadPoolExecutor.shutdown();
    }
}
