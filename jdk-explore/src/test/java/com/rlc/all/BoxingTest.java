package com.rlc.all;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by renlc on 2017/4/27.
 */
public class BoxingTest {

    /**
     * 自动装箱、拆箱测试
     * <pre>jdk1.8.0_121</pre>
     * <p>自动装箱int类型，会略为损失一定的性能，但差别十分小，如果不是数量级十分大（千万？）基本可以忽略</p>
     * <p>自动拆箱没有性能损失</p>
     */
    @Test
    public void boxingIntTest() {
        //测试5次，分别使用自动装箱、直接装入对象
        for (int j = 0; j < 5; j++) {
            List<Integer> list = new ArrayList<>();
            //自动装箱测试
            long s = System.currentTimeMillis();
            for (int i = 0; i < 10000000; i++) {
                //手动
                list.add(new Integer(i));
                //自动装箱
//                list.add(i);
            }
            System.out.println(System.currentTimeMillis() - s);
            //自动拆箱测试
            s = System.currentTimeMillis();
            int all = 0;
            //自动
            for (int element : list) {
                all += element;
            }
            //手动
//            for(Integer element : list){
//                all+= element.intValue();
//            }
            System.out.println("all = " + all);
            System.out.println(System.currentTimeMillis() - s);
        }
    }
}
