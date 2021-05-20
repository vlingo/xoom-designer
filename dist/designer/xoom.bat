@ECHO OFF
if "%VLINGO_XOOM_DESIGNER_HOME%"=="" set VLINGO_XOOM_DESIGNER_HOME=%~dp0
set directoryArg=--currentDirectory
set args=%* %directoryArg% %CD%
call java -jar %VLINGO_XOOM_DESIGNER_HOME%\bin\xoom-designer-dist.jar %args%
