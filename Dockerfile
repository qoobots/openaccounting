# 多阶段构建 - 总账模块
FROM maven:3.9.0-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY openaccounting-common ./openaccounting-common
COPY openaccounting-system ./openaccounting-system
COPY openaccounting-ledger ./openaccounting-ledger
RUN mvn clean package -DskipTests -pl openaccounting-ledger -am

# 运行时镜像
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/openaccounting-ledger/target/openaccounting-ledger-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
