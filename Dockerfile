FROM alpine:latest

RUN apk add openjdk21

RUN wget https://dlcdn.apache.org/tomcat/tomcat-10/v10.1.44/bin/apache-tomcat-10.1.44.tar.gz \
    && tar xvzf apache-tomcat-10.1.44.tar.gz
RUN rm apache-tomcat-10.1.44.tar.gz

COPY target/weatherTrackingApp-1.0-SNAPSHOT.war apache-tomcat-10.1.44/webapps/weather-viewer.war
EXPOSE 8080

ENTRYPOINT ["apache-tomcat-10.1.44/bin/catalina.sh", "run"]