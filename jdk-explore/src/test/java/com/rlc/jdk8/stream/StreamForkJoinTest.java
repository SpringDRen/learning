package com.rlc.jdk8.stream;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * Created by renlc on 2017/4/20.
 */
public class StreamForkJoinTest {


    /**
     * <p>1.stream不使用parallel时，使用的是当前线程</p>
     * <p>2.parallel 时，当前线程+ ForkJoinPool 中的commonPool，commonPool的线程数默认是（处理器-1）</p>
     * <p>3.ForkJoinPool 静态代码块调用 makeCommonPool 方法构造一个ForkJoin的线程池</p>
     * <p>4.设置系统属性java.util.concurrent.ForkJoinPool.common.parallelism可以改变默然线程数量，但是必须在
     * 静态代码块执行前设置才可以，换句话就是ForkJoinPool初始化之前。可以在启动参数中加 -D...。
     * 如例子中：放开位置1的注释，可以影响线程数量，但如果在位置2出设置，因为之前已经初始化了，所以并不起作用</p>
     * <p>5.默认并行流共用forkjoinpool 的 commonPool ,也就是说多个任务会互相影响</p>
     */
    @Test
    public void testStreamWithDefaultPool() {
        //位置1：System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "12");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> list = Lists.newArrayList("one", "two", "three", "four", "five", "six", "seven", "eight");
                System.out.println("start one job....");
                long s = System.currentTimeMillis();
                list.stream().parallel().forEach(str -> {
                    System.out.println(Thread.currentThread().getName() + " " + str);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                System.out.println("one job tooktime = " + (System.currentTimeMillis() - s) / 1000);
            }
        });
        thread.start();
        //位置2：System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "12");
        System.out.println("start another job...");
        List<String> list = Lists.newArrayList("first", "second", "thrid", "forth", "fifth", "sixth", "seventh", "eighth");
        long s = System.currentTimeMillis();
        list.stream().parallel().forEach(str -> {
            System.out.println(Thread.currentThread().getName() + " " + str);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println("second job tooktime = " + (System.currentTimeMillis() - s) / 1000);
        //防止第一个线程退出
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("end...");
    }

    /**
     * <p>在ForkJoinPool的任务中使用并行stream，不再使用默认的commonPool，而是使用的当前的ForkJoinPool</p>
     */
    @Test
    public void testForkJoin() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> list = Lists.newArrayList("one", "two", "three", "four", "five", "six", "seven", "eight");
                System.out.println("start one job....");
                long s = System.currentTimeMillis();
                list.stream().parallel().forEach(str -> {
                    System.out.println(Thread.currentThread().getName() + " " + str);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                System.out.println("one job tooktime = " + (System.currentTimeMillis() - s) / 1000);
            }
        });
        thread.start();

        ForkJoinPool pool = new ForkJoinPool(8);
        pool.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("start another job...");
                List<String> list = Lists.newArrayList("first", "second", "thrid", "forth", "fifth", "sixth", "seventh", "eighth");
                long s = System.currentTimeMillis();
                list.stream().parallel().forEach(str -> {
                    System.out.println(Thread.currentThread().getName() + " " + str);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                System.out.println("second job tooktime = " + (System.currentTimeMillis() - s) / 1000);
            }
        });

        pool.shutdown();
        //防止第一个线程退出
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            pool.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
