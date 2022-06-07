FROM maven:3.6-jdk-11 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn -e -B dependency:resolve
COPY src ./src
RUN mvn -e -B package

FROM openjdk:11-jre-slim
COPY --from=builder /app/target/myapp.jar /
EXPOSE 9090
CMD ["java", "-jar", "/myapp.jar"]