# vlingo-xoom-starter

[![Build Status](https://travis-ci.org/vlingo/vlingo-xoom-starter.svg?branch=master)](https://travis-ci.org/vlingo/vlingo-xoom-starter) [![Gitter chat](https://badges.gitter.im/gitterHQ/gitter.png)](https://gitter.im/vlingo-platform-java/community/)

Be guided into the Reactive DDD world by the project generator for the VLINGO/XOOM components.

Docs: https://docs.vlingo.io/vlingo-xoom/xoom-starter

## Introduction

A common reality of unfamiliarity exists for many developers either when they try to develop reactive applications or if the dilemma is about how to implement DDD properly, aiming for an accurate concretization from what has been strategically captured to a well-crafted domain model. Facing these obstacles, some choose to shoot in the dark while others may give up on using concepts and paradigms that are proven to be a strong foundation for building real-world, robust, modularized, and scalable applications. 

Now, the question is: can VLINGO/PLATFORM help developers surrounded by this challenging context and looking forward to having a successful combination of Actor Model + Reactive DDD? The correct answer is "that's the reason why VLINGO/PLATFORM exists". So, yes, we can really help you out on this journey in so many ways. One of them is to put VLINGO/XOOM Starter in your hands, a useful tool that generates a VLINGO/XOOM project and gives you an instant experience of start programming very quickly after a few minutes of setup steps. Also, it fits very well when you are having your first steps in the VLINGO components and want to learn how to start a DDD-friendly project powered by an Actor Model ecosystem. 

The next lines show you how to run VLINGO/XOOM Starter in your local environment. Let's get started!

## Installation 

The installation process is short. Before you start, just check if you have these tools already installed:

* Java 8+
* Maven 3.x.x
* Docker Desktop 18.x

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
 1.4.0
```

### Application Generation 

In addition to the command-line interface (see below), `vlingo-xoom-starter` provides a web/graphical user interface for a rapid application generation. Simply open a terminal window, execute `xoom gui` and your preferred browser will be opened with a wizard-fashioned screen, consisting of 5 steps.

**Context Step**

![screen-sample-image](https://gblobscdn.gitbook.com/assets%2F-LLB-V2sJmANuWISDmBf%2F-MM0sShPfweroMLy17IM%2F-MM159JUDGJ54jMcKZX5%2Fimage.png)

The first step has to do with the project build settings, including the project base package. So far, only Maven is supported, but you can easily convert the generated project using [Gradle's conversion version task](https://docs.gradle.org/current/userguide/migrating_from_maven.html#migmvn:automatic_conversion).

**Model - Aggregate Step**

![screen-sample-image](https://gblobscdn.gitbook.com/assets%2F-LLB-V2sJmANuWISDmBf%2F-MM1ED3Unz8WBDfG1Taa%2F-MM1FX_DCts7bJHJ5aLX%2Fimage.png)

The *Model - Aggregate step* informs the project generator of how your domain model must be generated. Here you are able to define:
* Aggregates (name, fields, methods, and its emitted events)
* Domain Events (name and fields)
* Rest Resources (HTTP method, path, and related aggregate method) 
Further on, you can keep track of each aggregate you've created and other relevant details on the *Model - Aggregate* review section. It shows an interesting angle of your model through an *Event Storming* perspective.

![screen-sample-image](https://gblobscdn.gitbook.com/assets%2F-LLB-V2sJmANuWISDmBf%2F-MM1IvPWmZlz3V0QdMPM%2F-MM1KFlltcy6E7Fo7DQ_%2Fimage.png?alt=media&token=3f9a7722-c1ef-4c3a-a84b-b61ba67926be)

**Model - Persistence Step**

![screen-sample-image](https://gblobscdn.gitbook.com/assets%2F-LLB-V2sJmANuWISDmBf%2F-MM1IvPWmZlz3V0QdMPM%2F-MM1OXroZiao0hr-lz0m%2Fimage.png)

This step displays some persistence preferences where you can pick the configuration that fits best for your project including the [storage type](https://docs.vlingo.io/vlingo-symbio), CQRS usage, database vendors, and, when applicable, how events are going to projected to your query model.

**Deployment Step**

![screen-sample-image](https://gblobscdn.gitbook.com/assets%2F-LLB-V2sJmANuWISDmBf%2F-MM1OnfL6SZVQVnR5BAn%2F-MM1Rr473DOo9d6ijq4N%2Fimage.png)

The project generator can also include containerization files facilitating Docker and Kubernetes deployment. Filling in the *Deployment Step*, you can choose different deployment types according to your needs. 

**Generation Step**

![screen-sample-image](https://gblobscdn.gitbook.com/assets%2F-LLB-V2sJmANuWISDmBf%2F-MM1SB3vLO4INqIJkXOx%2F-MM1UeV0LJIuciHmQ8ax%2Fimage.png)

In the last step, you just need to inform your parent folder of your project. In addition, here you can decide whether VLINGO/XOOM annotations and auto-dispatch are preferred or not. 
Once these 5 steps are completed, you can take advantage of the power of VLINGO/XOOM components on your new project. Go ahead! Also, count on VLINGO/PLATFORM comprehensive [documentation](https://docs.vlingo.io/) and its live and collaborative [community](https://gitter.im/vlingo-platform-java/community) willing to support developers on their journey.
     
### CLI

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
    <strong>#Storage Type (STATE_STORE or JOURNAL)</strong>
    storage.type=STATE_STORE
    <strong>#CQRS (true or false)</strong>
    cqrs=true
    <strong>#Projections Type (NONE, EVENT_BASED or OPERATION_BASED)</strong>
    projections=EVENT_BASED
    <strong>#Domain Model Database, required if CQRS is false (IN_MEMORY, POSTGRES, HSQLDB, MYSQL, YUGA_BYTE)</strong>
    database=HSQLDB
    <strong>#Command Model Database, required if CQRS is true or Storage Type is Journal (see database types above)</strong>
    command.model.database=MYSQL
    <strong>#Query Model Database, required if CQRS is true or Storage Type is Journal (see database types above)</strong>
    query.model.database=YUGA_BYTE
</code>
</pre>

Executing `xoom gen` causes the application generation based on the settings above, which are the same available on user interface.  

### Docker Commands

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

## Collaboration

Our team really appreciates collaboration, not only because it boosts VLINGO to greater value, but also for the fact that the more viewpoints we have the more competent and mature the VLINGO community will become. If you want to be a catalyst for moving the platform forward, take a tour of our [development guide](https://github.com/vlingo/vlingo-xoom-starter/blob/master/DEV-GUIDE.md). 
