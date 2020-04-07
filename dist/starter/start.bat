@ECHO OFF
SET CURRENT_DIR=%CD%
setx VLINGO_STARTER_HOME "%CURRENT_DIR%"
@ECHO ON
call java -jar bin\vlingo-xoom-starter-dist.jar