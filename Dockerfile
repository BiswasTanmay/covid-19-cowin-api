FROM maven:3.5-jdk-11 as builder

COPY ./covid-19-cowin-api /covid-19-cowin-api

WORKDIR ./covid-19-cowin-api

RUN mvn clean package install -DskipTests

FROM adoptopenjdk/openjdk11:alpine-slim

# Copy the jar to the production image from the builder stage.
COPY --from=builder ./covid-19-cowin-api/target/Covid-19-Vaccine-Notification.jar /Covid-19-Vaccine-Notification.jar

# Run the web service on container startup.
EXPOSE 8080
CMD java -jar ./Covid-19-Vaccine-Notification.jar



