# vlingo-xoom-starter

[![Build Status](https://travis-ci.org/vlingo/vlingo-xoom-starter.svg?branch=master)](https://travis-ci.org/vlingo/vlingo-xoom-starter) [![Gitter chat](https://badges.gitter.im/gitterHQ/gitter.png)](https://gitter.im/vlingo-platform-java/community/)

The project generator for the VLINGO/XOOM quick start components.

Docs: https://docs.vlingo.io/vlingo-xoom/xoom-starter

## Introduction
`vlingo-xoom-starter` enables project structure generation and components initialization for VLINGO/XOOM services and applications. The primary generation output contains: 
* `vlingo-xoom` initializer class
* Maven build configuration: `pom.xml`
* Default test class

The structure is represented below: 

```
    ├── src
    |   ├── main
    |   |     ├── java 
    |   |           └──[custom-package]
    |   |                    └──  Bootstrap.java
    |   └── test
    |         └── java 
    |              └──[custom-package]
    |                       └──  BootstrapTest.java
    └── pom.xml                
```

## Requirements
* Java 8+
* Maven 3.x.x
* Docker Desktop 18.x

## Installation 

Download the `vlingo-xoom-starter` compressed distribution file via `curl`:

* `zip` file: `curl -L -O https://github.com/vlingo/vlingo-xoom-starter/raw/master/dist/starter.zip`
* `tar` file: `curl -L -O https://github.com/vlingo/vlingo-xoom-starter/raw/master/dist/starter.tar`

Extract the file content, then set an environment variable named `VLINGO_XOOM_STARTER_HOME` indicating the absolute path for the uncompressed folder. Additionally, on Unix-based operating systems, it is necessary to allow _read_ and _execute_ access on executable script, placed under the root folder, as following:

``` 
 $ chmod 755 xoom
```

Ensure it's all set by verifying the `vlingo-xoom-starter` version:

``` 
 $ ./xoom -version
 1.3.0
```

## Application Generation 

`vlingo-xoom-starter` provides an user interface for a real quick application generation. Simply, open terminal, execute `xoom gui` and your preferred browser will be opened with a wizard-fashioned screen. 

![screen-sample-image](https://github.com/vlingo/vlingo-xoom-starter/blob/master/user-interface/src/assets/img/screen-sample.png)
     
Filling up every wizard step, you can readily take advantage of several auto-generated resources:
* A full maven project structure with all required `vlingo` dependencies;
* Aggregates and Domain Events prepared for reactive `vlingo` storage types;
* REST Resources, CQRS Projections, Adapters and Data type;
* Store Providers supporting State Store and Journal;
* Containerization files facilitating Docker / Kubernetes deployment; 

Alternatively, you can also generate applications directly from the terminal through `xoom gen` command. In this case, the project settings have to be informed in a <a href="https://github.com/vlingo/vlingo-xoom-starter/blob/master/dist/starter/vlingo-xoom-starter.properties">properties file</a> under the <code>vlingo-xoom-starter</code> folder. 
See a commented properties file sample below:    

<pre>
<code>
    <strong>#Maven artifact version</strong>
    version=1.0
    <strong>#Maven project group id</strong>
    group.id=com.company
    <strong>#Maven artifact version</strong>
    artifact.id=xoom-application
    <strong>#Base package name</strong>
    package=com.company.business
    <strong>#Absolute path for the project parent folder</strong>
    target.folder=/home/projects
    <strong>#vlingo xoom version</strong>
    vlingo.xoom.server.version=1.3.0
    <strong>#Deployment Type (NONE, DOCKER, KUBERNETES)</strong>
    deployment=DOCKER
    <strong>#Docker Image name, required if deployment type is KUBERNETES or DOCKER</strong>
    docker.image=xoom-app
    <strong>#Published Docker Image, required if deployment type is KUBERNETES</strong>
    k8s.image=xoom-application
    <strong>#Kubernetes POD name, required if deployment type is KUBERNETES</strong>
    k8s.pod.name=xoom-application
    <strong>#Aggregate names (separated by semicolon)</strong>
    aggregates=AggregateName1;AggregateName2;AggregateName3
    <strong>#Domain Events (each events group should be preceded by Aggregate name and separated by semicolon)</strong>
    events=AggregateName1,DomainEvent1,DomainEvent2;AggregateName2,DomainEvent1,DomainEvent2;AggregateName3,DomainEvent1,DomainEvent2
    <strong>#Aggregate Rest Resources</strong>
    rest.resources=AggregateName1;AggregateName2;AggregateName3
    <strong>#Storage Type (STATE_STORE or JOURNAL)</strong>
    storage.type=STATE_STORE
    <strong>#CQRS (true or false)</strong>
    cqrs=true
    <strong>#Projections Type (NONE, EVENT or OPERATION)</strong>
    projections=EVENT
    <strong>#Domain Model Database, required if CQRS is false (IN_MEMORY, POSTGRES, HSQLDB, MYSQL, YUGA_BYTE)</strong>
    database=HSQLDB
    <strong>#Command Model Database, required if CQRS is true or Storage Type is Journal (see database types above)</strong>
    command.model.database=MYSQL
    <strong>#Query Model Database, required if CQRS is true or Storage Type is Journal (see database types above)</strong>
    query.model.database=YUGA_BYTE
</code>
</pre>

Executing `xoom gen` causes the application generation based on the settings above, which are the same available on user interface.  

## Docker Commands

`vlingo-xoom-starter` provides cool shortcuts for interacting with Docker, from the project root folder, as follows:

<table>
    <thead>
        <tr>
            <th align="center">Command</th>
            <th align="center">Description</th>
            <th align="center">Options</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td align="center">xoom docker status</td>
            <td align="left">Shows the container status.</td>
            <td align="center">N/A</td>
        </tr>
        <tr>
            <td align="center">xoom docker package</td>
            <td align="left">Builds / update the Docker image from the application current state.</td>
            <td align="center"><strong>tag</strong>: relate a tag to the current image build. If not informed, the default value is <em>latest</em>. Example: xoom docker package --tag 1.0</td>
        </tr>
        <tr>
            <td align="center">xoom docker push</td>
            <td align="left">Publishes the image into the configured docker repository.</td>
            <td align="center"><strong>tag</strong>: relate the local tag to the remote tag. If not informed, the default value is <em>latest:latest</em>, following the pattern <em>local-tag:remote-tag</em>. Example: xoom docker push --tag 1.0:latest</td>
        </tr>
    </tbody>
</table>

## Development Guide

Our team really appreciates developers collaboration aiming the `vlingo` components evolution. In this section, a list of *how-to* steps are presented in regard to common `vlingo-xoom-starter` development tasks.

> Add / Update Code Generation Template
     
The project generator is a nice feature. If you want to collaborate on that, first take a look at the following diagram showing the `vlingo-xoom-starter` components responsible for project generator:

<img src="https://github.com/vlingo/vlingo-xoom-starter/blob/documentation/docs/starter-project-generator.png" width="870" height="400" />

As illustrated above, which relies on [Maven Archetype](https://maven.apache.org/guides/introduction/introduction-to-archetypes.html) and [Apache FreeMarker](https://freemarker.apache.org/). These two mechanism 