package com.rlc.jdk7.forkjoin;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * Created by renlc on 2017/4/20.
 */
public class ForkJoinPoolFactoryTest {

    /**
     * jdk1.8.0_121
     * <p>尝试改变fork join pool的线程名称，就像一般的ThreadFactory那样</p>
     * <p>查看ForkJoinPool构造方法源码，最终是调用一个private级别的构造方法，workerNamePrefix的字段影响了线程名称
     * <pre>final String workerNamePrefix;       // to create worker name string</pre>
     * 方法 final WorkQueue registerWorker(ForkJoinWorkerThread wt) 中设定了线程名称。
     * 换句话说，虽然提供了带ThreadFactory接口的构造方法，但并不能改变线程名称
     * </p>
     * <p>这里改变一下思路，直接使用反射改变 workerNamePrefix 这个字段的值，可以改变线程名称</p>
     * <p>意外发现：final 修饰的成员变量并不是不能改变的...，起码反射可以改变</p>
     */
    @Test
    public void threadFactoryTest() {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        try {
            Field field = forkJoinPool.getClass().getDeclaredField("workerNamePrefix");
            field.setAccessible(true);
            field.set(forkJoinPool,"mythread-");
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
        for (int i = 0; i < 20; i++) {
            final int t = i;
            forkJoinPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " " + t);
                }
            });
        }
        forkJoinPool.shutdown();
        try {
            forkJoinPool.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
