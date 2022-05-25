FROM openjdk:8-jre-alpine
COPY target/*-jar-with-dependencies.jar /auldfellas.jar
CMD sleep 5; /usr/bin/java -jar /auldfellas.jar ${CONTAINER_NAME}
