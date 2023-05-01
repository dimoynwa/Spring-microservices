FROM openjdk:17-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=build
RUN echo ${DEPENDENCY}
COPY build/libs/api-gateway-0.0.2.jar /app/lib/api-gateway-0.0.2.jar
ENTRYPOINT ["java","-jar","/app/lib/api-gateway-0.0.2.jar"]
EXPOSE 8888