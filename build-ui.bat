call RMDIR "src\main\resources\static\xoom-starter" /S /Q
call cd user-interface
call ng build --base-href=/xoom-starter/ --prod
call cd ..
