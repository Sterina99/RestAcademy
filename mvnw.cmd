@ECHO OFF
setlocal

set MAVEN_PROJECTBASEDIR=%MAVEN_BASEDIR%
IF NOT "%MAVEN_PROJECTBASEDIR%"=="" goto endDetectBaseDir

set EXEC_DIR=%CD%
set WDIR=%EXEC_DIR%
:findBaseDir
IF EXIST "%WDIR%\mvnw.cmd" (
  set MAVEN_PROJECTBASEDIR=%WDIR%
  goto endDetectBaseDir
)
cd ..
IF "%WDIR%"=="%CD%" goto baseDirNotFound
set WDIR=%CD%
goto findBaseDir

:baseDirNotFound
echo Could not find mvnw.cmd in directory hierarchy, starting from %EXEC_DIR%
exit /B 1

:endDetectBaseDir

set WRAPPER_JAR="%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar"
set WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

set DOWNLOAD_URL="https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar"

FOR %%i IN (java.exe) DO SET JAVA_EXE=%%~$PATH:i
IF NOT EXIST "%JAVA_EXE%" (
  ECHO Java executable not found. Please install Java 17+ and set PATH.
  exit /B 1
)

IF EXIST %WRAPPER_JAR% (
  REM Wrapper jar found, continue
) ELSE (
  ECHO Downloading Maven Wrapper jar from %DOWNLOAD_URL%
  powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; (New-Object Net.WebClient).DownloadFile('%DOWNLOAD_URL%', '%WRAPPER_JAR%')"
)

set MAVEN_JAVA_EXE="%JAVA_EXE%"
set WRAPPER_LAUNCH_CMD="%MAVEN_JAVA_EXE%" -classpath %WRAPPER_JAR% %WRAPPER_LAUNCHER% %MAVEN_CONFIG% %*

%WRAPPER_LAUNCH_CMD%
