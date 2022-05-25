FROM openjdk:8-jre-alpine
ENV SERVER_HOST=auldfellas
COPY target/*-jar-with-dependencies.jar /auldfellas.jar
CMD /usr/bin/java -jar /auldfellas.jar $SERVER_HOST