FROM openjdk:17-jdk-slim

ARG JAR_FILE=./target/transaction-limits-service-1.0.0.jar
COPY ${JAR_FILE} finance.jar
ENTRYPOINT ["java","-jar","/finance.jar"]