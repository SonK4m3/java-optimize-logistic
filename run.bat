@echo off
call mvn clean install -Dmaven.test.skip=true
cd server
call mvn spring-boot:run -DskipTests
cd ..