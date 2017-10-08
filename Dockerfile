FROM ubuntu:16.04

MAINTAINER Denis Stepanov

RUN add-apt-repository ppa:openjdk-r/ppa
RUN apt-get -y update

RUN apt-get install -y openjdk-8-jdk-headless
RUN apt-get install -y maven

ENV WORK /opt
ADD . $WORK/work/
RUN mkdir -p /var/www/html

WORKDIR $WORK/work
RUN mvn package

EXPOSE 80

CMD java -jar $WORK/work/target/server-1.0-SNAPSHOT.jar