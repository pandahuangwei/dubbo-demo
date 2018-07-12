2017-03-07 更新
1、修复序列化因字段不规范引起的问题；
2、增加超时时间以及重试次数属性配置；

2017-03-09 更新
1、调整包结构，代码从fms-service-route 调整到 fms-core-service;

2017-03-15 更新
1、调整报文解析接口传入参数，以及解析报文体返回的错误码;

2017-03-25 更新
1、打包排除resource下的属性文件，避免应用发布的时候读取错误的配置信息;

2017-03-27 更新
1、统一启动脚本，修改增加配置以及输出
2、统一WIN启动脚本  增加启动脚本增加标题;

2017-03-29 更新
1、将从属性文件配置的类型及地址调用，调整内部工厂实现，简单化之后的集群方式;

2017-03-30 更新
1、接口数据中都要增加航班标识(String:flightDt,flightNo,pod,poa,std);

2017-03-31 更新
1、增加报文是否加密配置：service.properties : msgEncrypt=true ,默认(不配置的情况下)为false,
   true :返回加密的报文，false:返回明文,
   Clear:明文,Cipher:密文;
   
2、增加工具类RoutePropertyUtil，配置在  service.properties中的属性，可通过该工具类读取;
3、增加过滤器处理日志，及拦截处理接口结果。


2017-04-01 更新

2017-04-18 更新
1、增加mq封装;
2、属性文件增加mq相关的参数;
3、route写入报文最新状态工具类RouteMsgAMQUtil;
4、调整MQ版本及其依赖;
5、mq测试类TestMqReciver，TestMqSender
6、
