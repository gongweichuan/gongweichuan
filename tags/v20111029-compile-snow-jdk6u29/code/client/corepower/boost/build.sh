#!/bin/sh

#linux下的命令行脚本 UTF－8编码

chmod u+x  ../../tools/bin/antRun
chmod u+x  ../../tools/bin/ant

# ----- Verify and Set Required Environment Variables -------------------------

if [ "$TERM" = "cygwin" ] ; then
  S=';'
else
  S=':'
fi
#需要设置JAVA_HOME和消除PATH的影响

#JAVA_HOME
OLD_JAVA_HOME="$JAVA_HOME"
unset JAVA_HOME
JAVA_HOME="/usr/lib/jvm/java-6-sun"
export JAVA_HOME

#PATH
OLD_PATH="$PATH"
unset PATH
PATH="$JAVA_HOME/bin"
export PATH

# ----- Ignore system CLASSPATH variable
OLD_CLASSPATH="$CLASSPATH"
unset CLASSPATH
#CLASSPATH="`echo lib/*.jar | tr ' ' $S`"
#CLASSPATH="`echo lib/*.jar`"
export CLASSPATH

# ----- Use Ant shipped with Lenya. Ignore installed in the system Ant
OLD_ANT_HOME="$ANT_HOME"
ANT_HOME=../../tools
OLD_ANT_OPTS="$ANT_OPTS"
ANT_OPTS="-Xms32M -Xmx512M "
export ANT_HOME ANT_OPTS
echo "$ANT_HOME/bin/ant"
"$ANT_HOME/bin/ant" -logger org.apache.tools.ant.NoBannerLogger -emacs  $@

# ----- Restore ANT_HOME and ANT_OPTS
ANT_HOME="$OLD_ANT_HOME"
ANT_OPTS="$OLD_ANT_OPTS"
export ANT_HOME ANT_OPTS
unset OLD_ANT_HOME
unset OLD_ANT_OPTS

# ----- Restore CLASSPATH
CLASSPATH="$OLD_CLASSPATH"
export CLASSPATH
unset OLD_CLASSPATH

#Restore PATH
PATH="$OLD_PATH"
export PATH
unset OLD_PATH

#Restore JAVA_HOME
JAVA_HOME="$OLD_JAVA_HOME"
export JAVA_HOME
unset OLD_JAVA_HOME