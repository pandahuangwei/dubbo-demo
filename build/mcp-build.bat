@echo off 
rem ENABLEDELAYEDEXPANSION

title build-mcp package

echo if build can not work ,please set JAVA_HOME and  MAVEN_HOME...

rem ---------------------------------------------------------------------------
rem set  JAVA_HOME and  MAVEN_HOME like this :
rem
rem set JAVA_HOME=C:\Program Files\Java\jdk1.7.0_55
rem set MAVEN_HOME=E:\apache-maven-3.3.9
rem set CLASSPATH=.;%JAVA_HOME%\lib\dt.jar;%JAVA_HOME%\lib\tools.jar
rem set Path=%JAVA_HOME%\bin;%JAVA_HOME%\jar\bin;%MAVEN_HOME%\bin
rem
rem ---------------------------------------------------------------------------


rem set JAVA_HOME=C:\Program Files\Java\jdk1.7.0_55
rem set MAVEN_HOME=E:\apache-maven-3.3.9



if not "%JAVA_HOME%" == "" goto javaHomeEnd
echo please set JAVA_HOME...
goto end
:javaHomeEnd
echo Using JAVA_HOME:   "%JAVA_HOME%"


if not "%MAVEN_HOME%" == "" goto mavenHomeEnd
echo please set MAVEN_HOME...
goto end
:mavenHomeEnd
echo Using MAVEN_HOME:   "%MAVEN_HOME%"


if not "%PATH%" == "" goto pathEnd
echo please set MAVEN_HOME...
set PATH=%JAVA_HOME%\bin;%JAVA_HOME%\jar\bin;%MAVEN_HOME%\bin;C:\Windows\System32
goto end
:pathEnd
echo Using PATH:   "%PATH%"

set CLASSPATH=.;%JAVA_HOME%\lib\dt.jar;%JAVA_HOME%\lib\tools.jar
rem set PATH=%JAVA_HOME%\bin;%JAVA_HOME%\jar\bin;%MAVEN_HOME%\bin

echo Using CLASSPATH:   "%CLASSPATH%"

echo= 
echo, 
echo;
echo ===  This build environment is OK. Let's go. ===
echo= 
echo, 
echo;

color 02

echo === Start building mcp... ===

cd ..\code\mcp

call mvn clean install

echo === End build mcp...=== 
echo= 
echo, 
echo;

rem ---------------------------------------------------------------------------
rem  Copy  file 
rem  echo 当前盘符：%~d0
rem  echo 当前盘符和路径：%~dp0
rem  echo 当前盘符和路径的短文件名格式：%~sdp0
rem  echo 当前批处理全路径：%~f0
rem  echo 当前CMD默认目录：%cd%
rem ---------------------------------------------------------------------------

echo === Start copy file to targer ...=== 

cd %~dp0 

rem 开始进行文件目录拷贝
echo del Last build jar, and Copy jar ...
set rootFloder=%~dp0mcp\
echo %rootFloder%



del/s /Q %~dp0mcp\
rd /S /Q %~dp0mcp\

ping -n 2 127.1 >nul

rem 创建根目录
md "%~dp0mcp"
rem 创建根目录下的WEB目录
md "%~dp0mcp\public"

ping -n 1 127.1 >nul
rem 创建根目录下的SERVICE目录
md "%~dp0mcp\local"
rem md "%~dp0mcp\local\service"
rem md "%~dp0mcp\local\service\conf"
rem md "%~dp0mcp\local\service\lib"

md "%~dp0mcp\public\service"
md "%~dp0mcp\public\service\conf"
md "%~dp0mcp\public\service\lib"

cd ../
rem copy %cd%\code\mcp\mcp-common-config\src\main\resources\*.properties %cd%\build\mcp\local\service\conf\
copy %cd%\code\mcp\mcp-common-config\src\main\resources\*.properties %cd%\build\mcp\public\service\conf\

rem 取消监控中心的发布包
rem echo begin copy dubbo-monitor
rem XCOPY "%cd%\build\dubbo-monitor-simple-2.8.4\*.*"  "%cd%\build\mcp\local\service\dubbo-monitor-simple-2.8.4\" /S /E


cd %~dp0

rem 从属性文件中读取配置信息
for /f "delims== tokens=1,*" %%a in ('type mcp-build.properties ^|findstr /i "service.local"') do  set modules="%%b"
rem 从属性文件中读取配置信息
for /f "delims== tokens=1,*" %%a in ('type mcp-build.properties ^|findstr /i "service.public"') do  set modulespublic="%%b"

rem 拷贝的模组，配载在构建属性文件下
echo copy modules:  %modules%

cd ../

set suffix=-assembly

rem 启用变量延迟
SETLOCAL ENABLEDELAYEDEXPANSION

:GOON
for /f "delims=, tokens=1,*" %%i in (%modules%) do (

	   echo %%i
	   set folderName=%%i%suffix%

	   XCOPY "%cd%\code\mcp\%%i\target\!folderName!\*"  "%~dp0mcp\local\service" /S /E /D
	   
	   copy %~dp0mcp\local\service\%%i\lib\*.jar  %~dp0mcp\local\service\lib\
	   del /q %~dp0mcp\local\service\%%i\lib\*.jar
	   
	   copy "%cd%\code\mcp\%%i\target\%%i.jar" %~dp0mcp\local\service\%%i\lib\
	   ping -n 1 127.1 >nul
       set modules="%%j"	  
	   
     goto GOON
)



:GONP
for /f "delims=, tokens=1,*" %%i in (%modulespublic%) do (

	   echo %%i
	   set folderName=%%i%suffix%

	   XCOPY "%cd%\code\mcp\%%i\target\!folderName!\*"  "%~dp0mcp\public\service" /S /E /D
	   
	   copy %~dp0mcp\public\service\%%i\lib\*.jar  %~dp0mcp\public\service\lib\
	   del /q %~dp0mcp\public\service\%%i\lib\*.jar
	   
	   copy "%cd%\code\mcp\%%i\target\%%i.jar" %~dp0mcp\public\service\%%i\lib\
	   ping -n 1 127.1 >nul
       set modulespublic="%%j"	  
	   
     goto GONP
)



  copy  %cd%\code\mcp\web-mcp\target\*.war  %~dp0mcp\public\
   
rem del /q %cd%\build\mcp\local\service\lib\mcp-common-config-1.0.jar
  del /q %cd%\build\mcp\public\service\lib\mcp-common-config-1.0.jar
  
echo Build mcp succeed,Press Space Key to Exit...
:end
pause