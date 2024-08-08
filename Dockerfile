FROM openjdk:17-jdk

ARG JAR_FILE=build/libs/cafe-cok-0.0.1-SNAPSHOT.jar
ARG PINPOINT_VERSION=2.5.1
ARG PINPOINT_AGENT_DOWNLOAD_URL=https://github.com/pinpoint-apm/pinpoint/releases/download/v${PINPOINT_VERSION}/pinpoint-agent-${PINPOINT_VERSION}.tar.gz
ARG COLLECTOR_IP

RUN curl -L ${PINPOINT_AGENT_DOWNLOAD_URL} | tar -xz -C /opt/
ENV PINPOINT_AGENT_PATH=/opt/pinpoint-agent-${PINPOINT_VERSION}

RUN sed -i "s/profiler.transport.grpc.collector.ip=.*/profiler.transport.grpc.collector.ip=${COLLECTOR_IP}/" ${PINPOINT_AGENT_PATH}/pinpoint-root.config

ENV JAVA_OPTS="-javaagent:${PINPOINT_AGENT_PATH}/pinpoint-bootstrap-${PINPOINT_VERSION}.jar -Dpinpoint.agentId=cafe-cok -Dpinpoint.applicationName=cafe-cok-app -Dpinpoint.config=${PINPOINT_AGENT_PATH}/pinpoint-root.config"

COPY ${JAR_FILE} cafe-cok.jar
CMD ["java", "-Dspring.profiles.active=prod", "-Duser.timezone=Asia/Seoul", "${JAVA_OPTS}", "-jar", "cafe-cok.jar"]

