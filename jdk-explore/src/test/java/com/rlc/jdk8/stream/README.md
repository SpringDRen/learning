# jdk8 stream 

## [map/reduce](MapReduceTest.java)
### 简单map/reduce测试 `mapReduceTest`

### 引用对象测试 `mapReduceObjectTest`
> reduce 返回值必须是新对象，否则会重使用引用值，list、array、map等引用同理

## [stream 与 ForkJoin](StreamForkJoinTest.java)

### 串行stream & 默认线程池 `testStreamWithDefaultPool`
1. 串行stream默认使用当前线程
2. 并行stream默认使用ForkJoinPool中公用的commonPool+当前线程  

### 在forkjoinpool中使用stream `testForkJoin`
3. 在ForkJoinPool中使用并行stream，stream使用当前ForkJoinPool的线程