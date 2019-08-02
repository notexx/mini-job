# mini-job 任务调度

# 启动
运行 `MiniJobApplication` 类, 在游览器中 输入 http://localhost:8080/index 进入调度任务界面
 
![](https://ws1.sinaimg.cn/large/64202e18gy1g5ld6xsyzkj22kv0x8doj.jpg)

## 新增单个任务
![](https://ws1.sinaimg.cn/large/64202e18gy1g5ld9dhw84j21am0ki0tz.jpg)
## 执行单个任务
![](https://ws1.sinaimg.cn/large/64202e18gy1g5lda6bzx6j22ks0afwg8.jpg)

执行结果
![](https://ws1.sinaimg.cn/large/64202e18gy1g5ldb01cq6j22ko06w75n.jpg)

## 模拟运行批量任务
输入运行的任务数量，点击批量执行按钮即可
执行结果如下
![](https://ws1.sinaimg.cn/large/64202e18gy1g5lddmhg7yj22l613kn7t.jpg)


# 算法
任务调度使用了三种算法：一致性hash算法、随机权重算法、轮询算法
## 一致性hash算法
> 优点：可以对相同的类型的任务分配个同一个任务通道执行 

> 缺点：有可能大量的任务都集中某几个节点上面，导致圆环 不平衡

## 随机权重算法
> 优点： RandomLoadBalance 的算法思想比较简单，在经过多次请求后，能够将调用请求按照权重值进行“均匀”分配。

> 缺点：当调用次数比较少时，Random 产生的随机数可能会比较集中，此时多数请求会落到同一台服务器上

## 轮询算法
> 优点：依次遍历，保证没一个通道都能被调用

> 缺点：每个执行通道性能不一样，可能导致性能差的执行缓慢，而性能好的，处于未饱和状态
