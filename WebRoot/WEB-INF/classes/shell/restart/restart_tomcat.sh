#!/bin/bash
# Filename: restart_tomcat.sh
# Description: Restart tomcat
# Date: 2018-02-06
# Author: songwj
# Description: Add kill all remaining processes
# Date: 2018-03-08
# Author: songwj
# Version: v1

export JAVA_HOME=/usr/java/jdk1.7.0_79
export JRE_HOME=$JAVA_HOME/jre
export CATALINA_BASE=/usr/local/apache-tomcat-7.0.79
export PATH=$JAVA_HOME/bin:$JRE_HOME/bin:$CATALINA_BASE/bin:$PATH
export CLASSPATH=.:$JAVA_HOME/lib:$JRE_HOME/lib:$CLASSPATH

echo "[$(date +'%F %T')] >>> Tomcat begin to restart."
$CATALINA_BASE/bin/shutdown.sh

# Kill all remaining processes
pidList=$(ps aux | grep /usr/java/jdk1.7.0_79/jre/bin/java | grep -v grep | awk '{print $2}')
for pid in $pidList; do
	kill -9 $pid
	echo "[$(date +'%F %T')] >>> Kill the process [$pid] successfully."
done

$CATALINA_BASE/bin/startup.sh
echo "[$(date +'%F %T')] >>> Tomcat restart complete."

