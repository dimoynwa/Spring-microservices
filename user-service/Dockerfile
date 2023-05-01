FROM openjdk:17-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=build
RUN echo ${DEPENDENCY}
COPY build/libs/user-service-0.0.2.jar /app/lib/user-service-0.0.2.jar
ENTRYPOINT ["java","-jar","/app/lib/user-service-0.0.2.jar"]
EXPOSE 8022