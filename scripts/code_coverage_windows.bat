cd /d "%~dp0.."
call mvn test
call mvn jacoco:report
start chrome "%~dp0..\target\site\jacoco\index.html"