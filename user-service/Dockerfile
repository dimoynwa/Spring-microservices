FROM openjdk:17-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=build
RUN echo ${DEPENDENCY}
COPY build/libs/user-service-0.0.3.jar /app/lib/user-service-0.0.3.jar
ENTRYPOINT ["java","-jar","/app/lib/user-service-0.0.3.jar"]
EXPOSE 8022