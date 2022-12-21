# Archura.io Router Application

Archura.io Router application with filters and reloadable configuration.

# Installation

The following steps explain, build, compile to native, create docker image, and run load test.

```shell
# use Graalvm Java 19 version
sdk install java 22.3.r19-grl

# install native image command line tool
gu install native-image

# build project
mvn clean install

# you can run the following command to generate native-image related configurations
java --enable-preview -agentlib:native-image-agent=config-output-dir=src/main/resources/META-INF/native-image -jar target/io.archura.router.ArchuraRouter.jar

# create native image from executable Jar
native-image --enable-preview --no-fallback -H:+ReportExceptionStackTraces -jar target/io.archura.router.ArchuraRouter.jar

# build docker image
docker build -t archura-router:0.0.1 .

# run docker image
docker run --rm -it --memory="32MB" --cpus="0.5" -p 8080:8080 --name archura-router archura-router:0.0.1

# you can follow the CPU and Memory usage of the container
docker stats

# you can run k6 as an alternative load test
k6 run --no-usage-report --vus 100 --duration 60s --summary-export out.json script.js

```
