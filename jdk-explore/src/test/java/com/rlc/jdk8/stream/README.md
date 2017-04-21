# jdk8 stream 


## stream 与 ForkJoin
1. 串行stream默认使用当前线程
2. 并行stream默认使用ForkJoinPool中公用的commonPool+当前线程
3. 在ForkJoinPool中使用并行stream，stream使用当前ForkJoinPool的线程