# ---- Build stage (includes Maven) ----
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

# copy only files needed to resolve deps first (better caching)
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# now copy sources and build
COPY src ./src
RUN mvn -q -DskipTests package

# ---- Runtime stage (JRE only) ----
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# copy the built jar
COPY --from=build /app/target/*.jar app.jar

RUN addgroup -S app && adduser -S app -G app \
  && chown -R app:app /app
USER app

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]