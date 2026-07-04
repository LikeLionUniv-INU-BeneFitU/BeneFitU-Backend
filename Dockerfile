FROM eclipse-temurin:21-jre
WORKDIR /app

# GitHub Actions가 빌드해서 컨텍스트에 둔 jar 파일을 바로 복사
COPY build/libs/*.jar app.jar

EXPOSE 8080
ENV TZ=Asia/Seoul

ENTRYPOINT ["java", "-jar", "app.jar"]