package com.rlc.jdk7.forkjoin;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by renlc on 2017/4/20.
 */
public class ThreadPoolJoinTest {

    /**
     * 线程池join测试
     * <p>
     * junit测试会在主线程结束时退出，因此使用main方法测试
     * <p>
     * 测试情况：4线程的线程池，共16个任务，每个任务周期3S
     * <p>
     * 一：在线程池提交的任务中，执行Thread.currentThread().join()代码
     * <p>
     * 1）：主线程中加入pool.shutdown()    每个线程只执行了一个任务，且线程池不会退出，进入无限等待，pool thread线程处于WAITING状态
     * <p>
     * 2）：主线程不加入pool.shutdown()    同上
     * <p>
     * <p>
     * 二：在线程池提交的任务中，不执行Thread.currentThread().join()
     * <p>
     * 1）：主线程中加入pool.shutdown()    所有在shutdown执行之前提交的任务都执行完毕，shutdown之后pool thread线程关闭，主线程随之退出
     * <p>
     * 2）：主线程不加入pool.shutdown()    所有任务都会执行，执行完毕pool进入等待状态，主线程不退出
     * <p>
     * <p>
     * 结论：
     * 1、无论是否关闭线程池，如果pool thread加入了Thread.currentThread().join()代码，pool thread不会终止，进入僵死状态
     * <p>
     * 2、不能在当前线程执行中加入Thread.join
     * <p>
     * 3、脑子坏掉了才写了这个测试类...怎么会让线程等待自己结束呢。。。。一定是疯掉了
     */
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 16; i++) {
            System.out.println("start one task..." + i);
            final int t = i;
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    //模拟任务周期较长
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //do something
                    System.out.println(Thread.currentThread().getName() + " execute task " + t);
                    //等待线程执行完毕
                    try {
                        Thread.currentThread().join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("aaa");
                }
            });
        }

        System.out.println("all task end....");
        pool.shutdown();
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("main thread exist..");
    }

    /**
     * 在线程执行过程中加入Thread.currentThread().join();
     * <p>
     * Thread不会退出，进入无线等待
     */
    @Test
    public void testJoinSelf() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " execute task");
                try {
                    Thread.currentThread().join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("end join");
            }
        }, "abcd").start();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("main end..");
    }

    /**
     * 不使用线程池，直接测试线程join
     * <p>
     * 1.不加入thread join    线程进入执行状态，但主线程退出，其他线程未执行完毕，跟随退出
     * <p>
     * 2.for循环中加入thread join   创建线程需要等待上一个线程结束，未起到异步作用
     * <p>
     * 3.创建线程加入list，for循环wait加入thread join 等待所有线程执行完毕，主线程才随之退出
     * <p>
     * 结论：
     * <p>
     * 直接使用thread join，主线程会等待该线程终止
     */
    @Test
    public void testJoin() {
        List<Thread> list = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName());
                }
            });
            list.add(thread);
            thread.start();
//            try {
//                thread.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
        for (Thread thread : list) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("main..." + Thread.currentThread().getName());
    }
}
