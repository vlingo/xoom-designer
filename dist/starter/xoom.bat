@ECHO OFF
SET "args="
:loop
SET args=%args% %1
shift
if not "%~1"=="" goto loop
call java -jar %VLINGO_XOOM_STARTER_HOME%\bin\vlingo-xoom-starter-dist.jar %args% --currentDirectory %CD%