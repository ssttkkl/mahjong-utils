FROM eclipse-temurin:11-jdk AS build-stage
COPY . .
RUN ./gradlew :mahjong-utils-webapi:shadowJar

FROM eclipse-temurin:17-jdk
RUN mkdir /app
COPY --from=build-stage ./mahjong-utils-webapi/build/libs/mahjong-utils-webapi-all.jar /app/app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]
