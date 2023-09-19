#!/bin/sh

############################################################
# 
# ���α׷����� 	: Build(shell)
# ���α׷���	: build.sh
# Version		: 1.0
# �ۼ���		: 2004/06/08
# �ۼ���		: ������
# ������		: 
# ������		: 
# ����			: Exception ����� ������ �Ѵ�.
#
# ������Ʈ��	: ���� ǥ�� ���
#
############################################################

# ȯ�溯���� �����Ѵ�.
VERSION=1.3
PHOME="/crm1/neo/mp/mplib"
JAVA_HOME="/usr/java"
LIB_HOME="$PHOME/component/class/$VERSION"
CPATH1=".:$JAVA_HOME/lib/rt.jar"
CPATH="$CLASSPATH:$LIB_HOME:$CPATH1"
CURPATH=`pwd`
JAVADOC_HOME="$PHOME/javadocs/$VERSION"

# LIB_HOME�� �����Ѵ�.
if [ ! -d $LIB_HOME ] ; then
    mkdir $LIB_HOME
fi

# JAVADOC_HOME �� �����Ѵ�.
if [ ! -d $JAVADOC_HOME ] ; then
    mkdir $JAVADOC_HOME
fi


# Ŭ������ �����Ѵ�.
$JAVA_HOME/bin/javac -d $LIB_HOME -classpath $CPATH Err.java

# javadoc document�� �����Ѵ�.

if [ -d $JAVADOC_HOME/exception ] ; then
	JAVADOC_HOME=$JAVADOC_HOME/exception
else
	mkdir $JAVADOC_HOME/exception
	JAVADOC_HOME=$JAVADOC_HOME/exception
fi

$JAVA_HOME/bin/javadoc -classpath $CPATH -private -d $JAVADOC_HOME Err.java

# jar ȭ���� �����Ͽ� lib ���丮�� �̵��Ѵ�.
cd $LIB_HOME
$JAVA_HOME/bin/jar -cvf $PHOME/lib/mplib_$VERSION.jar com



cd $CURPATH

