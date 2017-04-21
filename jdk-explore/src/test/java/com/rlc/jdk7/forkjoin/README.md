# 测试jdk7的fork/join  


1. fork/join适合将大任务分割为子任务，递归执行；ExecutorService适合并行任务  
2. 当任务的任务量均衡时，选择ThreadPoolExecutor往往更好，反之则选择ForkJoinPool  
3. 使用fork/join切割任务时，要设定合理的阈值，对效率影响很大
4. forkjoinpool的ThreadFactory无法改变线程名称，workerNamePrefix字段决定了线程前缀