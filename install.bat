setlocal
echo Setting JAVA_HOME to %JAVA6_HOME%.
set JAVA_HOME=%JAVA6_HOME%

echo Performing a short clean build.
pause
call mvn clean install -DperformRelease
echo Short clean build completed.
pause

echo Performing a full clean build.
pause
call mvn clean install -DperformRelease -Ptests,demos,samples
echo Full clean build completed.
pause

endlocal