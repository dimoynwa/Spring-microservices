FROM openjdk:17-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=build
RUN echo ${DEPENDENCY}
COPY build/libs/api-gateway-0.0.1.jar /app/lib/api-gateway-0.0.1.jar
ENTRYPOINT ["java","-jar","/app/lib/api-gateway-0.0.1.jar"]
EXPOSE 8888