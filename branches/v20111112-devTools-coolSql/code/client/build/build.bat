@echo off

set OLD_JAVA_HOME=%JAVA_HOME%
set JAVA_HOME=..\corepower\corepower-template\jdk-windows
set OLD_CLASSPATH=%CLASSPATH%
set CLASSPATH=
for %%i in (lib\endorsed\*.jar) do call tools\bin\appendcp.bat %%i

rem ----- Use Ant shipped with Cocoon. Ignore installed in the system Ant
set OLD_ANT_HOME=%ANT_HOME%
set ANT_HOME=..\tools

set OLD_ANT_OPTS=%ANT_OPTS%
set ANT_OPTS="-Djava.endorsed.dirs=lib\endorsed"
call %ANT_HOME%\bin\ant -logger org.apache.tools.ant.NoBannerLogger -emacs %1 %2 %3 %4 %5 %6 %7 %8 %9


rem ----- Restore ANT_HOME and ANT_OPTS
set ANT_HOME=%OLD_ANT_HOME%
set OLD_ANT_HOME=
set ANT_OPTS=%OLD_ANT_OPTS%
set OLD_ANT_OPTS=

rem ----- Restore CLASSPATH
set CLASSPATH=%OLD_CLASSPATH%
set OLD_CLASSPATH=
set JAVA_HOME=%OLD_JAVA_HOME%
set OLD_JAVA_HOME=
