FROM eclipse-temurin:latest
WORKDIR /opt/exporter
COPY java_exporter-0.0.1.jar /opt/exporter/java_exporter-0.0.1.jar
EXPOSE 8080/tcp
ENTRYPOINT ["java","-jar","java_exporter-0.0.1.jar","--spring.config.additional-location=/opt/exporter/vsphere.properties"]