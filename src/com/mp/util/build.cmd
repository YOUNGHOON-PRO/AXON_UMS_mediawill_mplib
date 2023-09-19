@REM ------------------------------------------------
@REM --
@REM -- 프로그램유형 	: Build(shell)
@REM -- 프로그램명		: build.cmd
@REM -- Version			: 1.0
@REM -- 작성일			: 2003/11/23
@REM -- 작성자			: 오범석
@REM -- 수정일			:
@REM -- 수정자			: 
@REM -- 설명				: 표준 콤포넌트 컴파일
@REM --
@REM -- 프로젝트명		: 표준 콤포넌트
@REM --
@REM ------------------------------------------------


@REM 컴파일 한다.

@ 현재 디렉토리 경로 저장
set CURDIR=%CD%

set PHOME=c:\project\dev_stand
set JAVA_HOME=c:\j2sdk1.4.2_02\
set LIB_HOME=%PHOME%\component\class
set CPATH1=%PHOME%\lib\jsdk23.jar;%PHOME%\lib\jndi.jar;%PHOME%\lib\gauce.jar
set CPATH=%LIB_HOME%;%CPATH1%

%JAVA_HOME%\bin\javac -d %LIB_HOME% -classpath %CPATH% Code.java DBUtil.java Env.java ExecTest.java FileDist.java FileDistFtp.java FileDistLog.java FtpUtil.java GauceUtil.java InitApp.java InitAppCall.java Jcrypt.java Log.java MailLog.java MailSendThread.java MailUtil.java ParamType.java Upload.java Util.java WebUtil.java DataSet.java DataSetImpl.java
@REM InitAppWebLogic.java

set JAVADOC_HOME=%PHOME%\javadocs\util\
%JAVA_HOME%\bin\javadoc -classpath %CPATH% -private -d %JAVADOC_HOME% Code.java DBUtil.java Env.java ExecTest.java FileDist.java FileDistFtp.java FileDistLog.java FtpUtil.java GauceUtil.java InitApp.java InitAppCall.java Jcrypt.java Log.java MailLog.java MailSendThread.java MailUtil.java ParamType.java Upload.java Util.java WebUtil.java DataSet.java DataSetImpl.java InitAppWebLogic.java

@REM JAR 파일을 생성한다.

cd %PHOME%\component\class

%JAVA_HOME%\bin\jar -cvf %PHOME%\lib\dev_stand.jar com\daco\util com\daco\exception

cd %CURDIR%
