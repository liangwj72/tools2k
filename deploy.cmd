@echo off
cd /d "%~dp0"

call mvn clean deploy  -Dmaven.test.skip=true
pause