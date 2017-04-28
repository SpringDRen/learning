# jdk8 集合测试

## [Map测试](MapTest.java)

### 一些新方法 `mapNewFuncTest`
- 带默认方法的取值：`V getOrDefault(Object key, V defaultValue)`
- map中不存在key才put：`V putIfAbsent(K key, V value)`

### forEach测试 `mapForEachTest`
map内部实现的forEach，lambda表达式形式，使代码更简洁  
1. 内部实现for (Map.Entry<K, V> entry : entrySet())，效率一样，代码更简洁  
2. 如果方法中传入了局部变量，要求为final类型，但并不是多线程

### merge测试 `mapMergeTest`
将一个值合并进map  
1. 要merge的value不能null，否则返回空指针异常
2. 如果map里的值为null，或者不含有该key，则将执行map.put(key,value)，不触发传入的function
3. merge func会抛出异常

### compute测试 `mapComputeTest`
对map里的值进行一定的计算，并更新值，如果新值为null，则移除原来的元素  
- `compute`  
- `computeIfAbsent`  如果不存在
- `computeIfPresent`  如果存在