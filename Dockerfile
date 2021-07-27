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
 && chmod +x /designer/xoom && find /designer -iname mvnw | xargs chmod +x \
 && cd /designer/resources/archetypes \
 && chmod +x mvnw \
 && ./mvnw install -f ./kubernetes-archetype/pom.xml \
 && ./mvnw archetype:generate -B \
           -DarchetypeCatalog=internal \
           -DarchetypeGroupId=io.vlingo.xoom \
           -DarchetypeArtifactId=xoom-turbo-kubernetes-archetype \
           -DarchetypeVersion=1.0 \
           -Dversion=1.0 \
           -DgroupId=io.vlingo \
           -DartifactId=designer-example \
           -DmainClass=io.vlingo.designerexample.infrastructure.Bootstrap \
           -Dpackage=io.vlingo.designerexample \
           -DvlingoXoomVersion=1.8.2 \
           -DdockerImage=designer-example-image \
           -Dk8sPodName=designer-example-pod \
           -Dk8sImage=designer-example-image

FROM jvm
USER xoom
VOLUME $XOOM_HOME/$XOOM_PROJECTS_DIR
COPY --from=designer --chown=xoom:xoom /designer $XOOM_HOME
CMD xoom gui --target zip-download


