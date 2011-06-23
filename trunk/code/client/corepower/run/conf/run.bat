echo off
title ERP Client
rem -------------------------------------------------------------------------
rem COREPOWER Client Bootstrap Script for Win32
rem -------------------------------------------------------------------------

@if not "%ECHO%" == ""  echo %ECHO%
@if "%OS%" == "Windows_NT"  setlocal

set DIRNAME=.

if "%OS%" == "Windows_NT" set DIRNAME=%~dp0%
set PROGNAME=run.bat
if "%OS%" == "Windows_NT" set PROGNAME=%~nx0%

if exist "%DIRNAME%\set-jvm-param.bat" call "%DIRNAME%\set-jvm-param.bat"

rem Read all command line arguments

set JUSTECHO=n
set ARGS=
:loop
if [%1] == [] goto endloop
if not [%1] == [-username] goto :password
        set JAVA_OPTS=%JAVA_OPTS% -Dcorepower.mainframe.login.username=%2
	shift
	shift
	goto loop
	
:password	
if not [%1] == [-password] goto :serverip
      set JAVA_OPTS=%JAVA_OPTS% -Dcorepower.mainframe.login.password=%2
      shift
      shift
      goto loop
      
:serverip      
if not [%1] == [-serverip] goto :clientview
	set JAVA_OPTS=%JAVA_OPTS% -Dcorepower.mainframe.login.serverip=%2
	shift
	shift
	goto loop
	
:clientview	
if not [%1] == [-clientview] goto :justechojava
	set JAVA_OPTS=%JAVA_OPTS% -Dcorepower.mainframe.single.clientview=%2
	shift
	shift
	goto loop
	
:justechojava	
if not [%1] == [-justecho] goto :pidpath
	set JUSTECHO=%2
	shift
	shift
	goto loop	

:pidpath	
if not [%1] == [-pidpath] goto :normal
	set JAVA_OPTS=%JAVA_OPTS% -Dcorepower.psl.systemsupport.pid.path=%2
	shift
	shift
	goto loop
	
:normal	
        echo Fail to start erp client. Invalid argument, please check!
        goto allend
:endloop


set ERP_HOME=%DIRNAME%..
set ERP_LIB=%ERP_HOME%\lib
set ERP_BOOT_LIB=%DIRNAME%boot_lib
set COREPOWER_HOME=%DIRNAME%..\platform
set KERNEL_HOME=%DIRNAME%..\kernel
set JAVA_HOME=%DIRNAME%..\..\jdk-windows
set KERNEL_BIN=%KERNEL_HOME%\bin
set KERNEL_LIB=%KERNEL_HOME%\lib
set KERNEL_DEFAULT_LIB=%KERNEL_HOME%\server\default\lib

rem ---add PATH for java native method
set PATH=%JAVA_HOME%\jre\bin;%PATH%;%ERP_LIB%


if not "%JAVA_HOME%" == "" goto ADD_TOOLS

set JAVA=java

echo JAVA_HOME is not set.  Unexpected results may occur.
echo Set JAVA_HOME to the directory of your local JDK to avoid this message.
goto SKIP_TOOLS

:ADD_TOOLS

set JAVA=%JAVA_HOME%\bin\java

if exist "%JAVA_HOME%\lib\tools.jar" goto SKIP_TOOLS
echo Could not locate %JAVA_HOME%\lib\tools.jar. Unexpected results may occur.
echo Make sure that JAVA_HOME points to a JDK and not a JRE.

:SKIP_TOOLS

rem Include the JDK javac compiler for JSP pages. The default is for a Sun JDK
rem compatible distribution to which JAVA_HOME points

set JAVAC_JAR=%JAVA_HOME%\lib\tools.jar
set ERP_CLASSPATH=%JAVAC_JAR%;%DIRNAME%boost.jar;%ERP_HOME%\deploy

set JAVA_OPTS=%JAVA_OPTS% -client

if not "%JVM_MX%"=="" goto exist_script_conf
set JVM_MX=-Xmx256m

:exist_script_conf
set JAVA_OPTS=%JAVA_OPTS% %JVM_MX% %JVM_PRAR%
set JAVA_OPTS=%JAVA_OPTS% -Dant.home="%ERP_HOME%"
set JAVA_OPTS=%JAVA_OPTS% -Djava.library.path="%ERP_LIB%"
set JAVA_OPTS=%JAVA_OPTS% -Dprogram.name=%PROGNAME%
set JAVA_OPTS=%JAVA_OPTS% -Djava.rmi.server.useCodebaseOnly=true
set JAVA_OPTS=%JAVA_OPTS% -Djava.rmi.server.RMIClassLoaderSpi=com.chinaviponline.erp.corepower.psl.systemsupport.provider.RmiSpiProvider
set JAVA_OPTS=%JAVA_OPTS% -Dsun.lang.ClassLoader.allowArraySyntax=true
set ERP_XCLASSPATH=%ERP_HOME%\..\jdk-ext\windows\corepower-prert.jar

rem JPDA options. Uncomment and modify as appropriate to enable remote debugging.
    set JAVA_OPTS=-classic -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=4280,server=y,suspend=y %JAVA_OPTS%

if not %JUSTECHO%==y goto excutejava
echo %JAVA% %JAVA_OPTS%   -Xbootclasspath/p:"%ERP_XCLASSPATH%"  -classpath " %ERP_CLASSPATH%" com.chinaviponline.erp.BoostMain %ARGS%
goto allend

:excutejava
echo .
echo .
echo   =============================================================================
echo     *      *      * * * * *   *         * * *      * *     *       *   * * * *
echo      *     *     *  *         *        *         *     *   * *   * *   * 
echo       *   * *   *   * * * *   *        *         *     *   *  * *  *   * * * *
echo        * *   * *    *         *        *         *     *   *   *   *   *
echo         *     *     * * * *   * * * *   * * *      * *     *   *   *   * * * *
echo   =============================================================================
echo .
echo .
echo                        ChinaVipOnline Management System
echo .
echo .



rem echo===============================================================================
rem echo .
rem echo   ERP Client Bootstrap Environment
rem echo .
rem echo   ERP_HOME: %ERP_HOME%
rem echo.
rem echo   JAVA: %JAVA%
rem echo .
rem echo   JAVA_OPTS: %JAVA_OPTS%
rem echo .
rem echo   CLASSPATH: %ERP_CLASSPATH%
rem echo .
rem echo . ANT_HOME:  %ERP_HOME%
rem echo ===============================================================================
rem echo .

cd /d "%ERP_HOME%"

echo @WORK_DIR@ %ERP_HOME%
echo @JAVA_HOME@ %JAVA_HOME%
echo @JAVA_CMD@
"%JAVA%" %JAVA_OPTS% -Xbootclasspath/p:"%ERP_XCLASSPATH%" -classpath "%ERP_CLASSPATH%" com.chinaviponline.erp.BoostMain %ARGS%
:allend
