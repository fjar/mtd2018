FROM openjdk
ADD target/customer-service.jar customer-service.jar
ENTRYPOINT ["java", "-Xmx512m", "-Xms256m", "-jar", "/customer-service.jar"]
EXPOSE 3333
