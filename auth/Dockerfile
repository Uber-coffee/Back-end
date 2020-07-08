FROM openjdk:11
ADD target/auth*.jar auth.jar
ENTRYPOINT ["java", "-jar", "auth.jar"]