FROM hseeberger/scala-sbt as builder

WORKDIR /root
ENV JAVA_OPTS="$JAVA_OPTS -Dhttp.proxyHost=10.193.21.110 -Dhttp.proxyPort=8080 -Dhttps.proxyHost=10.193.21.110 -Dhttps.proxyPort=8080"

COPY . .

VOLUME /root:/tmp
RUN sbt run