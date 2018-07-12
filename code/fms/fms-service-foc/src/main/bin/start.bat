@echo off & setlocal enabledelayedexpansion
title ${project.build.finalName}
set MAIN_JAR=;..\lib\${project.build.finalName}.jar

set LIB_JARS=""

cd ..\..\lib

for %%i in (*) do set LIB_JARS=!LIB_JARS!;..\..\lib\%%i


cd %~dp0
cd ..\bin

if ""%1"" == ""debug"" goto debug
if ""%1"" == ""jmx"" goto jmx


rem echo ff "%1"
rem  aa %LIB_JARS%
rem  cur path  %cd%

set ALL_LIB_JAR=%LIB_JARS%%MAIN_JAR%

echo ALL_LIB_JAR %ALL_LIB_JAR%

java -Xms64m -Xmx1024m -XX:MaxPermSize=64M -classpath ..\..\conf;%ALL_LIB_JAR%;..\conf com.alibaba.dubbo.container.Main

goto end
pause
:debug
java -Xms64m -Xmx1024m -XX:MaxPermSize=64M -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n -classpath ..\..\conf;..\conf;%LIB_JARS% com.alibaba.dubbo.container.Main
goto end

:jmx
java -Xms64m -Xmx1024m -XX:MaxPermSize=64M -Dcom.sun.management.jmxremote.port=1099 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -classpath ..\..\conf;..\conf;%LIB_JARS% com.alibaba.dubbo.container.Main

:end
pause