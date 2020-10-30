FROM adoptopenjdk/openjdk11:alpine

ENTRYPOINT ["java", "-Xms6144m", "-Xmx6144m", "-agentlib:jdwp=transport=dt_socket,server=y,address=*:8083,suspend=n", "-javaagent:/opt/newrelic/newrelic.jar", "-jar", "/opt/app.jar", "-Djava.net.preferIPv4Stack=true", "-Dnewrelic.config.distributed_tracing.enabled=true"]
ARG JAR_FILE
ARG API_VERSION
RUN apk --no-cache add curl unzip && \
         mkdir -p /tmp /opt/newrelic/logs && \
         curl -o /tmp/newrelic-java.zip -fSL \
		 https://download.newrelic.com/newrelic/java-agent/newrelic-agent/5.6.0/newrelic-java.zip && \
	unzip /tmp/newrelic-java.zip newrelic/newrelic.jar -d /opt && \
	rm /tmp/newrelic-java.zip && \
	chmod a+r /opt/newrelic/newrelic.jar && \
	chmod a+rw /opt/newrelic/logs && \
	apk del curl unzip          
EXPOSE 8083
ADD target/${JAR_FILE} /opt/app.jar
