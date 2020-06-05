call cd user-interface
call ng build --base-href=/xoom-starter/ --prod
call cd ..
call RMDIR "src\main\resources\xoom-starter" /S /Q
call Xcopy /E /I user-interface\dist\user-interface src\main\resources\xoom-starter