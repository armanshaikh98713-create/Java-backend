# Stage 1: Build the WAR using Maven + Java 11
FROM maven:3.9-eclipse-temurin-11 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests 
# This create a WAR FILE

# Stage 2: Deploy the WAR on Tomcat 9 + Java 11
FROM tomcat:9.0-jdk11

RUN rm -rf /usr/local/tomcat/webapps/*

COPY --from=build /app/target/hello-world.war /usr/local/tomcat/webapps/hello-world.war

EXPOSE 8080

CMD ["catalina.sh", "run"]