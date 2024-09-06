#build the app
FROM maven:3.8.1-openjdk-17 AS BUILD
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

#create the final image
FROM openjdk:17.0.1-slim
WORKDIR /app
COPY --from=BUILD /app/target/liaison_system-0.0.1-SNAPSHOT.jar liaison.jar

#define the cors urls
ENV DEPLOYED_FRONT_END_URL='http://localhost:4200'
ENV LOCAL_FRONT_END_URL='http://localhost:4200'

#jwt signin key
ENV AUTHENTICATION_KEY='PUEkc0jrtVJcdgh3YW2Zj0annhr1xly9Z0z6HiTiY0o='

#databse config
ENV DATABASE_URL='jdbc:postgresql://localhost:5432/postgres'
ENV DATABASE_USERNAME='postgres'
ENV DATABASE_PASSWORD='@mAj3StiCw8n101'

#set the port
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "liaison.jar"]
