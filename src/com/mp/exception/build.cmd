@REM 컴파일 한다.

set PHOME=c:\project\dev_stand
set JAVA_HOME=c:\j2sdk1.4.1_01\
set LIB_HOME=%PHOME%\component\class
set CPATH1=%PHOME%\lib\weblogic_8.1.jar;%PHOME%\lib\gauce.jar;
set CPATH=%LIB_HOME%;%CPATH1%

%JAVA_HOME%\bin\javac -d %LIB_HOME% -classpath %CPATH% Err.java

set JAVADOC_HOME=%PHOME%\javadocs\exception\
%JAVA_HOME%\bin\javadoc -private -d %JAVADOC_HOME% -classpath %CPATH% Err.java


