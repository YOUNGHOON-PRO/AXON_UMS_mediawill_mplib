#!/bin/sh

############################################################
# 
# 프로그램유형 	: Build(shell)
# 프로그램명	: build.sh
# Version		: 1.0
# 작성일		: 2004/06/08
# 작성자		: 오범석
# 수정일		: 
# 수정자		: 
# 설명			: UTIL 모듈을 컴파일 한다.  
#
# 프로젝트명	: 개발 표준 모듈
#
############################################################

# 환경변수를 설정한다.
VERSION=1.3
PHOME="/crm1/neo/mp/mplib"
JAVA_HOME="/usr/java"
LIB_HOME="$PHOME/component/class/$VERSION"
CPATH1=".:$JAVA_HOME/lib/dt.jar:$PHOME/lib/jsdk23.jar:$PHOME/lib/weblogic_8.1.jar:$PHOME/lib/gauce.jar:$PHOME/lib/activation.jar:$PHOME/lib/jecf.jar:$PHOME/lib/mail.jar"
CPATH="$CLASSPATH:$CPATH1:$LIB_HOME"
CURPATH=`pwd`
JAVADOC_HOME="$PHOME/javadocs/$VERSION"

# LIB_HOME을 생성한다.
if [ ! -d $LIB_HOME ] ; then
	mkdir $LIB_HOME
fi

# JAVADOC_HOME 을 생성한다.
if [ ! -d $JAVADOC_HOME ] ; then
	mkdir $JAVADOC_HOME
fi


# 클래스를 생성한다.
$JAVA_HOME/bin/javac -d $LIB_HOME -classpath $CPATH  Code.java DBUtil.java Env.java ExecTest.java FileDist.java FileDistFtp.java FileDistLog.java FtpUtil.java GauceUtil.java InitApp.java InitAppCall.java Jcrypt.java Log.java MailLog.java MailSendThread.java MailUtil.java ParamType.java Upload.java Util.java WebUtil.java DataSet.java DataSetImpl.java InitAppWebLogic.java StatData.java StatOper.java Admin.java UILang.java RespLog.java RespThread.java TimeThread.java TmRun.java TmWorkThread.java RespProcess.java CampLogProcess.java DNSLook.java DNSLookup.java

# javadoc document를 생성한다.
if [ -d $JAVADOC_HOME/util ] ; then
    JAVADOC_HOME=$JAVADOC_HOME/util
else
    mkdir $JAVADOC_HOME/util
    JAVADOC_HOME=$JAVADOC_HOME/util
fi

$JAVA_HOME/bin/javadoc -classpath $CPATH -private -d $JAVADOC_HOME Code.java DBUtil.java Env.java ExecTest.java FileDist.java FileDistFtp.java FileDistLog.java FtpUtil.java GauceUtil.java InitApp.java InitAppCall.java Jcrypt.java Log.java MailLog.java MailSendThread.java MailUtil.java ParamType.java Upload.java Util.java WebUtil.java DataSet.java DataSetImpl.java InitAppWebLogic.java StatData.java StatOper.java Admin.java UILang.java RespLog.java RespThread.java TimeThread.java TmRun.java TmWorkThread.java RespProcess.java CampLogProcess.java DNSLook.java DNSLookup.java

# jar 화일을 생성하여 lib 디렉토리로 이동한다.
cd $LIB_HOME
$JAVA_HOME/bin/jar -cvf $PHOME/lib/mplib_$VERSION.jar com

cd $CURPATH



