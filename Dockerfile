# 构建阶段：用 Maven + JDK21 打包
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
# 先拷贝 pom.xml 拉取依赖（利用缓存）
COPY pom.xml .
RUN mvn dependency:go-offline -B
# 再拷贝源码并打包
COPY src ./src
RUN mvn clean package -DskipTests

# 运行阶段：轻量 JRE21 镜像
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
