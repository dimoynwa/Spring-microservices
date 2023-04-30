FROM openjdk:17-jdk-alpine
VOLUME /tmp
ENV NAME=service-registry-0.0.1.jar
ARG DEPENDENCY=build
RUN echo ${DEPENDENCY}
COPY build/libs/${NAME} /app/lib/${NAME}
ENTRYPOINT ["java","-jar","app/lib/service-registry-0.0.1.jar"]
EXPOSE 8761