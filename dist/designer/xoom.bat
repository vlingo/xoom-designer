@ECHO OFF
set "directoryArg=--currentDirectory"
set args=%* %directoryArg% %CD%
call java -jar %VLINGO_XOOM_DESIGNER_HOME%\bin\xoom-designer-dist.jar %args%