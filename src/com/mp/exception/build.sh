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
# 설명			: Exception 모듈을 컴파일 한다.
#
# 프로젝트명	: 개발 표준 모듈
#
############################################################

# 환경변수를 설정한다.
VERSION=1.3
PHOME="/crm1/neo/mp/mplib"
JAVA_HOME="/usr/java"
LIB_HOME="$PHOME/component/class/$VERSION"
CPATH1=".:$JAVA_HOME/lib/rt.jar"
CPATH="$CLASSPATH:$LIB_HOME:$CPATH1"
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
$JAVA_HOME/bin/javac -d $LIB_HOME -classpath $CPATH Err.java

# javadoc document를 생성한다.

if [ -d $JAVADOC_HOME/exception ] ; then
	JAVADOC_HOME=$JAVADOC_HOME/exception
else
	mkdir $JAVADOC_HOME/exception
	JAVADOC_HOME=$JAVADOC_HOME/exception
fi

$JAVA_HOME/bin/javadoc -classpath $CPATH -private -d $JAVADOC_HOME Err.java

# jar 화일을 생성하여 lib 디렉토리로 이동한다.
cd $LIB_HOME
$JAVA_HOME/bin/jar -cvf $PHOME/lib/mplib_$VERSION.jar com



cd $CURPATH

