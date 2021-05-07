FROM alpine:3.13

LABEL maintainer="VLINGO XOOM Team <info@vlingo.io>"

ENV JAVA_HOME=/usr/lib/jvm/default-jvm/
ENV PATH=${JAVA_HOME}/bin:$PATH:/designer:/designer/bin
ENV VLINGO_XOOM_DESIGNER_HOME=/designer

ADD dist/designer.tar /

RUN addgroup -S xoom && adduser -S -D -s /sbin/nologin -h /designer -G xoom xoom \
 && apk add --no-cache bash openjdk8 \
 && mkdir -p /designer/VLINGO-XOOM \
 && chmod +x /designer/xoom && find /designer -iname mvnw | xargs chmod +x && chown -R xoom:xoom /designer

WORKDIR /designer
VOLUME /designer/VLINGO-XOOM
CMD /designer/xoom gui
USER xoom
