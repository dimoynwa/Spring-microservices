FROM openjdk:17-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=build
RUN echo ${DEPENDENCY}
COPY build/libs/department-service-0.0.1.jar /app/lib/department-service-0.0.1.jar
ENTRYPOINT ["java","-jar","/app/lib/department-service-0.0.1.jar"]
EXPOSE 8021