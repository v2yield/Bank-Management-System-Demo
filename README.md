# Bank-Management-System-Demo
**Just for the final project**
## Environment 
- Jdk8
- Mysql
## Build

- Client
``` 
$ javac -encoding utf8 -cp .;./lib/jxl.jar;./lib/itextpdf.jar;./lib/mysql-connector-java-8.0.27.jar *.java
$ java -encoding utf8 -cp .;./lib/jxl.jar;./lib/itextpdf.jar;./lib/mysql-connector-java-8.0.27.jar Test
```

- Server
```
$ javac -encoding utf8 -cp .;./lib/jxl.jar;./lib/itextpdf.jar;./lib/mysql-connector-java-8.0.27.jar *.java
$ java -cp .:./lib/jxl.jar:./lib/mysql-connector-java-8.0.27.jar MonitorClient
```
