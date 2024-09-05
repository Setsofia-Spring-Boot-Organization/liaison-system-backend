#build the app
FROM maven:3.8.1-openjdk-17 AS BUILD
WORKDIR /app
COPY . .
RUN mvm clean package -Dskip-Tests

#create the final image
FROM openjdk:17.0.1-slim
WORKDIR /app
COPY --from=BUILD /target/liaison_system-0.0.1-SNAPSHOT.jar liaison.jar

#set the port
EXPOSE 8040
ENTRYPOINT ["java", "-jar", "liaison.jar"]