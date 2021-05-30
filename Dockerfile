
FROM openjdk:17-ea-jdk as extract_layers

ARG BUILD_MODULE=hapi-fhir-open-emr

WORKDIR /application

ARG BUILD_MODULE=hapi-fhir-open-emr

COPY . .
RUN echo $(ls -1 /)
RUN java -Djarmode=layertools -jar /application/app.jar extract

FROM openjdk:17-slim-buster
WORKDIR /application
COPY --from=extract_layers application/dependencies/ ./
COPY --from=extract_layers application/spring-boot-loader/ ./
COPY --from=extract_layers application/snapshot-dependencies/ ./
COPY --from=extract_layers application/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]