FROM adoptopenjdk:11-jre-hotspot
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} api.jar
CMD ["java", "-jar", "/api.jar"]