package com.rlc.jdk8.stream;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * Created by renlc on 2017/4/19.
 */
public class MapReduceTest {

    /**
     * 简单 map reduce测试
     * <pre>jdk1.8.0_121</pre>
     */
    @Test
    public void mapReduceTest() {
        //简单的map reduce测试
        //统计list中所有单词的长度
        List<String> list = Lists.newArrayList("hello", "world!", "aaaabbb", "ccccdddd", "eeeefffff");
        int len = list.parallelStream()
                .map(String::length)
                .reduce(0, (integer, integer2) -> {
                    //第一次reduce，两两合并，第一个值为第一个参数传入的默认值，第二个值为map返回的所有对象，多余的元素合并至下一次reduce
                    //后续reduce，合并计算结果，直至只有一个元素
                    //reduce之前，list经过stream后，共有n个元素，要reduce的次数： n + (n-1) 次reduce  第一个n为所有元素跟默认元素进行第一次reduce，第二个n-1是正常一个批次的reduce数
                    System.out.println(integer + " " + integer2);
                    return integer + integer2;
                });
        System.out.println(len);
        Assert.assertEquals(len, 35);
    }

    class MyObj {
        int a;
        int b;

        public MyObj(int a, int b) {
            this.a = a;
            this.b = b;
        }
    }

    /**
     * 引用对象map reduce测试
     */
    @Test
    public void mapReduceObjectTest() {
        List<MyObj> list = new ArrayList<>();
        list.add(new MyObj(1, 2));
        list.add(new MyObj(1, 2));
        list.add(new MyObj(1, 2));
        list.add(new MyObj(1, 2));
        list.add(new MyObj(1, 2));
        list.add(new MyObj(1, 2));
        list.add(new MyObj(1, 2));
        list.add(new MyObj(1, 2));
        list.add(new MyObj(1, 2));
        list.add(new MyObj(1, 2));
        MyObj resultObj = list.parallelStream()
                //reduce 返回值必须是新对象，否则会重使用引用值，list、array、map等引用同理
                .reduce(new MyObj(0, 0), (result, element) -> new MyObj(result.a + element.a, result.b + element.b));
        //错误的写法
//                .reduce(new MyObj(0, 0), (result, element) -> {
//                            result.a += element.a;
//                            result.b += element.b;
//                            return result;
//                        }
//                );
        System.out.println(resultObj.a + " " + resultObj.b);
    }
}
