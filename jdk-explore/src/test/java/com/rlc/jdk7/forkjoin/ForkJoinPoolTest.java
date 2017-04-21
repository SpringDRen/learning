package com.rlc.jdk7.forkjoin;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by renlc on 2017/4/20.
 */
public class ForkJoinPoolTest {

    /**
     * jdk1.8.0_121
     * <p>测试work steal算法，计算1+2+...+n</p>
     * <p>分别测试普通线程池跟fork join</p>
     * <p>1.sum(1-100000000),任务分割值10000，普通线程池平均100ms,forkjoin平均55ms，总的说来，forkjoin线程池更快</p>
     * <p>2.sum(1-100000000),任务分割值100000，普通线程池平均30ms,forkjoin平均40ms，总的说来，普通线程池更快</p>
     * <p>2.sum(1-100000000),任务分割值1000，普通线程池平均90ms,forkjoin平均120ms+，总的说来，普通线程池更快</p>
     * <p>
     * <p>结论</p>
     * <p>1.任务数较少的时候，普通线程池更快</p>
     * <p>2.单个任务时间较短时，普通线程池更快</p>
     * <p>3.使用fork join要设置合适的阈值，分割适合的任务数</p>
     * <p>4.fork join递归编程效率更高</p>
     * <p>5.当任务的任务量均衡时，选择ThreadPoolExecutor往往更好，反之则选择ForkJoinPool</p>
     */
    @Test
    public void testWorkSteal() throws InterruptedException {
        int startInt = 1;
        int endInt = 100000000;
        ForkJoinPool fjPool = new ForkJoinPool(4);
        try {
            long start = System.currentTimeMillis();
            System.out.println(fjPool.submit(new SumInt(startInt, endInt)).get());
            long end = System.currentTimeMillis();
            System.out.println(end - start);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //============================================
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        long start = System.currentTimeMillis();
        final int THRESHOLD = 1000;
        List<Future<Long>> list = new ArrayList<>();
        for (int i = startInt; i <= endInt; i = i + THRESHOLD + 1) {
            int t_end = i + THRESHOLD;
            if (t_end > endInt) {
                t_end = endInt;
            }
            list.add(executorService.submit(new SumInt3(i, t_end)));
        }
        long result = 0;
        for (Future<Long> future : list) {
            try {
                result += future.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        System.out.println(result);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    /**
     * 普通线程池任务
     */
    class SumInt3 implements Callable<Long> {
        private int start;
        private int end;

        public SumInt3(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public Long call() throws Exception {
            long result = 0;
            for (int i = start; i <= end; i++) {
                result += i;
            }
            return result;
        }
    }

    /**
     * fork join 递归任务
     */
    class SumInt extends RecursiveTask<Long> {

        private int start;
        private int end;
        private final int THRESHOLD = 1000;

        public SumInt(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            if (end - start > THRESHOLD) {
                int middle = (end - start) / 2 + start;
                SumInt left = new SumInt(start, middle);
                SumInt right = new SumInt(middle + 1, end);
                left.fork();
                right.fork();
                long leftResult = left.join();
                long rightResult = right.join();
                return leftResult + rightResult;
            } else {
                long result = 0;
                for (int i = start; i <= end; i++) {
                    result += i;
                }
                return result;
            }
        }
    }
}
