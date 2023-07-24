#FROM harbor.runjian.com/syy-open/ubuntu-jre-11:20.04
FROM iyue/ubuntu-jdk17
ARG DEBIAN_FRONTEND=noninteractive
ENV TZ=Asia/Shanghai

EXPOSE 9000/tcp

ENV LC_ALL zh_CN.UTF-8
RUN apt update && apt install -y language-pack-zh-hans vim
RUN mkdir -pv /opt/auth-gateway/logs/
COPY target/*.jar /opt/auth-gateway
WORKDIR /opt/auth-gateway

ENTRYPOINT ["/bin/bash","-c","java ${JAVA_OPTS} -jar /opt/auth-gateway/*.jar"]
