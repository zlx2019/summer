#！/bin/sh

#Jar文件
JAR=zeus-music-v1.0.jar
#环境
ENV=aliyun

echo '------终止'$JAR'程序---------'
PROCESS=`ps -ef |grep java |grep -v grep|grep $JAR|awk '{print $2}'`
for i in $PROCESS
do
    echo "Kill the $1 process [ $i ]"
    kill -9 $i
done

echo '------启动'$JAR'程序---------'
nohup java -jar  -XX:MetaspaceSize=512m -XX:MaxMetaspaceSize=512m -Xms1024m -Xmx1024m -Xmn256m -Xss256k -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC -XX:+HeapDumpOnOutOfMemoryError  $JAR --spring.profiles.active=$ENV  >/dev/null 2>&1 &
echo 'script execute success!'