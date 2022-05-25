FROM openjdk:8-jre-alpine
COPY target/*-jar-with-dependencies.jar /client.jar
CMD sleep 9; /usr/bin/java -jar /client.jar ${CONTAINER_NAME}
