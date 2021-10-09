FROM alpine:3.13 as jvm

LABEL maintainer="VLINGO XOOM Team <info@vlingo.io>"

ARG XOOM_HOME=/designer
ARG XOOM_PROJECTS_DIR=VLINGO-XOOM
ENV JAVA_HOME=/usr/lib/jvm/default-jvm/
ENV PATH=${JAVA_HOME}/bin:$PATH:$XOOM_HOME:$XOOM_HOME/bin
ENV VLINGO_XOOM_DESIGNER_HOME=$XOOM_HOME
ENV VLINGO_XOOM_DESIGNER_ENV=CONTAINER

RUN addgroup -S xoom && adduser -S -D -s /sbin/nologin -h $XOOM_HOME -G xoom xoom \
 && apk add --no-cache bash openjdk8

WORKDIR $XOOM_HOME

FROM jvm as designer
COPY dist/designer.tar /designer.tar
RUN tar -xf /designer.tar -C / \
 && chmod +x /designer/xoom && find /designer -iname mvnw | xargs chmod +x

FROM jvm
USER xoom
VOLUME $XOOM_HOME/$XOOM_PROJECTS_DIR
COPY --from=designer --chown=xoom:xoom /designer $XOOM_HOME
CMD xoom designer --target zip-download
