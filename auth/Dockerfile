FROM openjdk:11
ADD target/auth*.jar auth.jar
ADD start.sh start.sh
RUN chmod +x start.sh
ENTRYPOINT ["./start.sh"]