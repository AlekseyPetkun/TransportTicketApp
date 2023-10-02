FROM openjdk:17-jdk-slim-buster
WORKDIR /app

COPY . ./app

WORKDIR /app/build
EXPOSE 8070
ENTRYPOINT ["java","-jar","transportTicketsMicroservicesApp.jar"]

#java -jar TransportTicketsMicroservicesApp.jar