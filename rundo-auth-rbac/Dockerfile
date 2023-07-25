#FROM harbor.runjian.com/syy-open/ubuntu-jre-11:20.04
FROM cfbstudio/ubuntu-jdk17:v1
ARG DEBIAN_FRONTEND=noninteractive
ENV TZ=Asia/Shanghai

EXPOSE 9000/tcp

ENV LC_ALL zh_CN.UTF-8
RUN apt update && apt install -y language-pack-zh-hans vim
RUN mkdir -pv /opt/auth-rbac/logs/
COPY target/*.jar /opt/auth-rbac
WORKDIR /opt/auth-rbac

ENTRYPOINT ["/bin/bash","-c","java ${JAVA_OPTS} -jar /opt/auth-rbac/*.jar"]
