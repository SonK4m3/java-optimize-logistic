@echo off
call mvn clean install -P dev
cd server
call mvn spring-boot:run
cd ..