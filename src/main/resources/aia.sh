link=`dirname $0`
cd $link;
service_path="./Service/"
java_process="VRAbatchAIA"




start(){
	 nohup java -jar $service_path$java_process.jar auto > /dev/null 2>&1 &
         sleep 2s
         ps -ef | grep $java_process | grep -v grep
}


stop(){
	status $java_process 0
	
	if [ $? -gt 0 ];then
	  ps -ef | grep $java_process | grep -v grep | awk '{print $2}'  | xargs kill
        fi
}


restart(){
 stop
 start
}

status(){
	 count=`ps -ef | grep $java_process | grep -v grep |wc -l`
	 if [ "$count" -gt "0"  ];then
	     if [ "$2" == "1" ] ; then
		      echo "$1 is running ..."
	     fi	     
             return 1
   else
	     if [ "$2" == "1" ];then
		      echo "$1 is not running ..."
	     fi
           return 0
       fi
  
}

executeJava(){
 java -Dfile.encoding=utf-8 -Dpsae.showmodel=false  -jar $service_path$java_process.jar $1 $2 $3
}

case $1 in
  status)
	       status "postgres" 1
         status $java_process 1
	 ;;
  start) start  ;;
  stop) stop ;;
  restart) restart ;;
  upload)  executeJava $1 $2 ;;  
  encrypt) executeJava $1 $2;;
  violation) executeJava $1 $2;;
  feedBackToAds) executeJava $1 $2 $3;;
  feedBackToPqa) executeJava $1 $2 $3;;
  *) echo "unknow commnad(status|start|stop|restart|upload|encrypt|feedBackToAds(feedBackToPqa) yyyyMMddHHmm yyyyMMddHHmm|violation yyyyMMdd)";;
esac
