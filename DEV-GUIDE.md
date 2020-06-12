### Development Guide

In this section, practical *how-to* steps are presented for anyone interested in making this tool even better.

### Introduction to `Application Generation` feature
     
The following diagram gives us an `Application Generation` overview showing its components interaction:

<p align="center">
    <img src="https://github.com/vlingo/vlingo-xoom-starter/blob/documentation/docs/raw-proj-gen-diagram.png" height="400" />
</p>

As illustrated above, `Application Generation` can be run by [two commands](https://github.com/vlingo/vlingo-xoom-starter/blob/documentation/README.md#application-generation): `xoom gen` or `xoom gui`. Both are alternative ways for a quick start with `vlingo\xoom`, having exactly the same parameters list. The only difference is that the latter reads parameters from a properties file, while the first consumes parameters from web-based UI.

The second half of the diagram shows some tools that perform core actions. [Maven Archetypes](https://maven.apache.org/guides/introduction/introduction-to-archetypes.html) creates the project structure dynamically organizing the directory hierarchy, Maven configuration, also handling  deployment resources as Dockerfile and K8s manifest file. Further, [Apache FreeMarker](https://freemarker.apache.org/) takes care of classes generation by processing preexisting code templates. That said, let's see how to add templates at code level.

### Create / Update Code Templates

The main constituent parts for every auto-generated class are: 
* A Freemarker template file
* A `io.vlingo.xoom.starter.task.template.steps.TemplateProcessingStep` subclass