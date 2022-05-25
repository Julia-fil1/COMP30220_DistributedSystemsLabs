FROM openjdk:8-jre-alpine
COPY target/*-jar-with-dependencies.jar /girlpower.jar
CMD sleep 5; /usr/bin/java -jar /girlpower.jar ${CONTAINER_NAME}
