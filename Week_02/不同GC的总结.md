GCLogAnalysis.java 运行

一、-Xmx512m -Xms512m 的情况下
java -Xmx512m -Xms512m -XX:+UseSerialGC -XX:+PrintGCDetails GCLogAnalysis
结果：执行结束!共生成对象次数: 7946，执行FullGC 0次，年轻代GC15次 
java -Xmx512m -Xms512m -XX:+UseParallelGC -XX:+PrintGCDetails GCLogAnalysis
结果：执行结束!共生成对象次数: 5918， 执行FullGC 3次，YoungGC 19次
java -Xmx512m -Xms512m -XX:+UseConcMarkSweepGC -XX:+PrintGC GCLogAnalysis
结果：执行结束!共生成对象次数:7236, 执行FullGC 3次， YoungGC 11次

在设置堆大小512m的情况下，串行GC比并行GC效果稍好,产生对象也更多。在堆内存较小的情况下，并行和串行的GC效果相差不大，并行也没有体现出多线程的优势，反而还有线程上下文切换

二、 -Xmx2g -Xms2g 的情况下，由于时间段不会进行fullGC
java -Xmx2g -Xms2g -XX:+UseSerialGC -XX:+PrintGCDetails GCLogAnalysis
串行结果：执行结束!共生成对象次数: 8394，执行YoungGC4次
java -Xmx2g -Xms2g -XX:+PrintGCDetails GCLogAnalysis
并行结果：执行结束!共生成对象次数: 10068， 执行YoungGC 5次
java -Xmx2g -Xms2g -XX:+UseConcMarkSweepGC -XX:+PrintGC GCLogAnalysis
CMS结果：执行结束!共生成对象次数: 9685，执行YoungGC4次

在增大堆大小的情况下，效率均有所提高。并行GC产生对象次数最多。

二、 -Xmx4g -Xms4g 的情况下
java -Xmx4g -Xms4g -XX:+UseSerialGC -XX:+PrintGCDetails GCLogAnalysis
结果：执行结束!共生成对象次数: 8573 执行Young GC 2次
java -Xmx4g -Xms4g -XX:+PrintGCDetails GCLogAnalysis
结果：执行结束!共生成对象次数: 7846， 执行Young GC 2次
java -Xmx4g -Xms4g -XX:+UseConcMarkSweepGC -XX:+PrintGC GCLogAnalysis
结果：执行结束!共生成对象次数: 9334 执行Young GC 4次
java -Xmx4g -Xms4g -XX:+UseG1GC -XX:+PrintGC GCLogAnalysis
结果：G1执行结束!共生成对象次数: 8479

在增大堆到4g之后，CMS GC产生对象数最多，Young GC 次数也最多。

gateway-server-0.0.1-SNAPSHOT.jar 运行

-Xmx2g -Xms2g 情况下
wrk -t 20 -c 100 -d 40s http://localhost:8088/api/hello
并行GC 结果：Requests/sec: 35558.53
串行GC 结果：Requests/sec: 37323.64
CMS 结果：Requests/sec: 30228.89
G1GC 结果：Requests/sec: 28818.06
结果显示，在2g堆的情况下，并行GC反而是吞吐量最低的GC

-Xmx4g -Xms4g 情况下
wrk -t 20 -c 100 -d 40s http://localhost:8088/api/hello
并行GC 结果：Requests/sec: 32955.35
串行GC 结果：Requests/sec: 30733.45
CMS 结果：Requests/sec: 30732.35
G1GC 结果：Requests/sec: 30749.95
结果显示，在4g堆下，基本符合想像中的情况，并行gc在大内存的情况下比串行GC更优化，但中本机CMS和G1与串行GC相差不大。
由于第一次学习及使用以上知识，学习进度较慢，且时间紧急，感觉还未能进行更从容深入到分析。
以上分析参考了revengray的分析思路。
