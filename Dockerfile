FROM alpine:latest

RUN apk add openjdk21

RUN wget https://dlcdn.apache.org/tomcat/tomcat-10/v10.1.44/bin/apache-tomcat-10.1.44.tar.gz \
    && tar xvzf apache-tomcat-10.1.44.tar.gz
RUN rm apache-tomcat-10.1.44.tar.gz

WORKDIR /apache-tomcat-10.1.44

RUN rm -rf webapps/ROOT*
COPY target/weatherTrackingApp-1.0-SNAPSHOT.war /webapps/ROOT.war

RUN mkdir -p /webapps/ROOT \
    && unzip /webapps/ROOT.war -d webapps/ROOT \
    && rm /webapps/ROOT.war


EXPOSE 8080

ENTRYPOINT ["bin/catalina.sh", "run"]