1���������� JAVA_HOME,MAVEN_HOME,PATH,CLASSPATH��������ȷ��

2������Ҫ�����ķ������õ�build.properties �ļ��У��Զ��ŷָ������м䲻Ҫ���ո�
    �ڲ�����
    service.local=fms-service-route,fms-service-flightimport,fms-service-foc,fms-service-fpn
    ��������
    service.public=fms-service-charg
   

3��ִ��(˫��)build-all.bat�ű�,����ִ��maven����뿽���ȶ�����

4�����ڵ�ǰĿ¼�½�Ŀ¼�ṹ
    fms 
       -- local                        
                --service            --�����б�
                          -- conf    --���������ļ�
                          -- lib     --���õ�jar
                          -- fms-service-*
                                           -- bin     --�����ű�(linux�£���Ҫ��Ȩ(chmod 755 *.sh),������� _bash^M,˵�����ļ���windows��ʽ)
                                                         ��Ҫִ�в��裨�༭-�޸��ļ���ʽ)�磺 vi start.sh -> Esc ->:set ff=unix ->Esc ->:wq
                                           -- conf    --˽�������ļ�
                                           -- lib     --˽��jar

       --public --����
               
                 

5��������ֹ����жϣ��Ȱ�fmsĿ¼�ֶ�ɾ���������¹���.

6����Ҫ���һ��linux���𻷾��Ƿ�װ�� nc�����û�еĻ����Դ�rpm Ŀ¼��ȡ��װ����

7��dubbo-monitor-simple-2.8.4 ����dubbo�ļ������