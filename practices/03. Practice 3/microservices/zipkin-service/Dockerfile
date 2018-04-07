FROM openjdk
ADD target/zipkin-service.jar zipkin-service.jar
ENTRYPOINT ["java", "-Xmx512m", "-Xms256m", "-jar", "/zipkin-service.jar"]
EXPOSE 9411
