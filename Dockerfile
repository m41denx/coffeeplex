FROM tomcat:11
LABEL authors="m41den"

COPY target/coffeeplex-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/app.war
#COPY mysql-connector-j-9.1.0.jar /usr/local/tomcat/lib/mysql-connector-j-9.1.0.jar