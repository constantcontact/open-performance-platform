FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD opp-service-0.1.0.jar opp-service.jar
EXPOSE 8080
RUN sh -c 'touch /opp-service.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/opp-service.jar"]
