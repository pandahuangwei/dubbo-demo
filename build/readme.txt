1、环境变量 JAVA_HOME,MAVEN_HOME,PATH,CLASSPATH需配置正确；

2、将需要发布的服务配置到build.properties 文件中，以逗号分隔开，中间不要留空格；
    内部服务
    service.local=fms-service-route,fms-service-flightimport,fms-service-foc,fms-service-fpn
    外网服务
    service.public=fms-service-charg
   

3、执行(双击)build-all.bat脚本,将会执行maven打包与拷贝等动作；

4、会在当前目录新建目录结构
    fms 
       -- local                        
                --service            --服务列表
                          -- conf    --共用配置文件
                          -- lib     --共用的jar
                          -- fms-service-*
                                           -- bin     --启动脚本(linux下，需要授权(chmod 755 *.sh),如果出现 _bash^M,说明该文件是windows格式)
                                                         需要执行步骤（编辑-修改文件格式)如： vi start.sh -> Esc ->:set ff=unix ->Esc ->:wq
                                           -- conf    --私有配置文件
                                           -- lib     --私有jar

       --public --待续
               
                 

5、如果出现构建中断，先把fms目录手动删除，再重新构建.

6、需要检查一下linux部署环境是否安装了 nc，如果没有的话可以从rpm 目录下取安装包。

7、dubbo-monitor-simple-2.8.4 启动dubbo的监控中心