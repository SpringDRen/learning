package com.rlc.jdk8.collection;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by renlc on 2017/4/27.
 */
public class MapTest {

    /**
     * 测试map.putIfAbsent(), getOrDefault()
     * <pre>jdk1.8.0_121</pre>
     */
    @Test
    public void mapNewFuncTest() {
        Map<String, String> map = new HashMap<>();
        //getOrDefault 默认值
        System.out.println(map.getOrDefault("aaa", "222"));
        //putIfAbsent 不存在才放入map
        map.put("aaa", "ccc");
        map.putIfAbsent("aaa", "111");
        map.putIfAbsent("bbb", "222");
        System.out.println(map);
    }

    /**
     * 测试map.forEach()
     * <pre>jdk1.8.0_121</pre>
     * <p>1.内部实现for (Map.Entry<K, V> entry : entrySet())，效率一样，代码更简洁</p>
     * <p>2.如果方法中传入了局部变量，要求为final类型，但并不是多线程</p>
     */
    @Test
    public void mapForEachTest() {
        Map<String, String> map = new HashMap<>();
        map.put("aaaa", "111");
        map.put("bbbb", "222");
        final String test = "test";
        map.forEach((k, v) -> {
            System.out.println(Thread.currentThread().getName());
            System.out.println(test + " " + k + " " + v);
        });
    }

    /**
     * 测试map.merge()
     * <pre>jdk1.8.0_121</pre>
     * <p>1.要merge的value不能null，否则返回空指针异常</p>
     * <p>2.如果map里的值为null，或者不含有该key，则将执行map.put(key,value)，不触发传入的function</p>
     * <p>3.merge func会抛出异常</p>
     */
    @Test
    public void mapMergeTest() {
        String key = "abcd";
        String value = "2";
        Map<String, String> map = new HashMap<>();
//        map.put(key, "1");
//        map.put(key, "1a");
//        map.put(key, null);
        //key：要merge的key ，value：要merge到map里的值
        //oldvalue：map中的value，newvalue：value
        System.out.println(map);
        map.merge(key, value, (oldvalue, newvalue) -> {
            System.out.println(oldvalue + " " + newvalue);
            return String.valueOf(Integer.valueOf(oldvalue) + Integer.valueOf(newvalue));
        });
        System.out.println(map);
    }

    /**
     * 测试compute系列方法
     * <pre>jdk1.8.0_121</pre>
     */
    @Test
    public void mapComputeTest() {
        Map<String, Integer> map = new HashMap<>();
        //compute不存在的值
        System.out.println("compute不存在的值-----------------");
        try {
//            Integer val = map.compute("first", (k, v) -> v * 10);
            //操作不存在的值会报空指针
            Integer val = map.compute("first", (k, v) -> 10);
            //计算不存在的值，不使用原来的值，不会报错，且会又返回值，会更新map
            System.out.println(val);
        } catch (Exception e) {
            System.out.println("compute null ..." + e.getMessage());
        }
        System.out.println(map);
        //compute一个已存在的值
        System.out.println("compute一个已存在的值-----------------");
        map.put("first", 1);
        Integer val1 = map.compute("first", (k, v) -> v * 10);
        System.out.println(val1);
        System.out.println(map);

        //computeIfAbsent 不存在的值
        System.out.println("computeIfAbsent 不存在的值-----------------");
        Integer val2 = map.computeIfAbsent("second", (k) -> 0);
        System.out.println(val2);
        System.out.println(map);
        //computeIfAbsent 存在的值
        System.out.println("computeIfAbsent 存在的值-----------------");
        val1 = map.computeIfAbsent("first", (k) -> -1);
        System.out.println(val1);
        System.out.println(map);

        //computeIfPresent 存在的值
        System.out.println("computeIfPresent 存在的值-----------------");
        Integer val3 = map.computeIfPresent("third", (k, v) -> v * 10);
        System.out.println(val3);
        System.out.println(map);
        //computeIfPresent 不存在的值
        System.out.println("computeIfPresent 不存在的值-----------------");
        Integer val4 = map.computeIfPresent("forth", (k, v) -> v * 10);
        System.out.println(val4);
        System.out.println(map);
    }
}
