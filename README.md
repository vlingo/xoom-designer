# XOOM Designer

[![Build](https://github.com/vlingo/xoom-designer/workflows/Build/badge.svg)](https://github.com/vlingo/xoom-designer/actions?query=workflow%3ABuild)

The VLINGO XOOM Designer to guide you in rapid delivery of low-code to full-code Reactive, Event-Driven Microservices and Applications using DOMA, DDD, and other approaches.

Docs: https://docs.vlingo.io/xoom-designer

### Features
<img src="https://vlingo.io/wp-content/uploads/2021/04/xoom-designer-scrn.png" width="70%" height="70%">

## [Introduction](#intro)

A common reality of unfamiliarity exists for many developers when they try to develop either [Reactive](https://docs.vlingo.io/overview/reactive-and-computing-health) applications and services, or to implement DOMA and DDD properly, or both. Many give up on using concepts and paradigms that are proven to be a strong foundation for building modern applications and systems that are robust, modularized, scalable, and that use modern architectures. These developers tend to fall back to familiar approaches, frameworks, and tools that have rarely worked out well in the long run.

The VLINGO XOOM platform SDK was created to help developers who face such challenges to confidently move forward and modernize their architectures, designs, and implementations. One platform component that greatly accelerates developer modernization efforts is the VLINGO XOOM Designer. It supports visual model definition, REST API, persistence, and container definitions, followed by project generation that is automatically built. Your applications and services can be running within minutes. With an instantly executable service experience, developers are in a position to quick implement custom business logic within the pre-generated model. This is a great way to learn Reactive architecture and coupled with DDD tactical modeling that are powered by an Actor-based ecosystem. 

The next section show you how to run and use the VLINGO XOOM Designer in your local environment. Let's get started!

## [Quick Start](#quick-start)

The quickest way to start XOOM Designer is to run it on Docker. Taking advantage of the `docker-compose` file created by the VLINGO XOOM team, you can initialize Designer simply using the following commands:

```bash
 $ curl -L -O https://raw.githubusercontent.com/vlingo/xoom-designer/master/docker-compose.yml
 $ docker-compose pull
 $ docker-compose up -d
```

Then, XOOM Designer can be accessed at [http://localhost:19090/context](http://localhost:19090/context). Likewise, [XOOM Schemata](https://docs.vlingo.io/xoom-schemata) will be also initialized and can be accessed at [http://localhost:9019](http://localhost:9019). Learn more about the XOOM Designer/Schemata integration [here](https://docs.vlingo.io/xoom-designer#architecture-api-producer-exchange-and-consumer-exchange-s).     

## [Installation](#installation)

The installation process is short. Before you start, just check if you have these tools already installed:

* Java 8+
* Maven 3.x.x

Download the `xoom-designer` compressed distribution file via `curl`:

* `zip` file: `curl -L -O https://github.com/vlingo/xoom-designer/releases/latest/download/designer.zip`
* `tar` file: `curl -L -O https://github.com/vlingo/xoom-designer/releases/latest/download/designer.tar`

Extract the file content, then set an environment variable named `VLINGO_XOOM_DESIGNER_HOME` indicating the absolute path of the uncompressed folder; that is, the installation folder. Using a *nix shell, such as `bash`, do this:

```
 $ VLINGO_XOOM_DESIGNER_HOME=[installation-path]
 $ export VLINGO_XOOM_DESIGNER_HOME
```

On Windows you can use the _System Properties > Advanced > Environment Variables..._ to set the property permanently. For a one-time setting before running the design tool you can use the command line:

```
 C:\> set VLINGO_XOOM_DESIGNER_HOME=[installation-path]
```

Additionally, on *nix systems, it is necessary to set _read_ and _execute_ access on executable script, placed under the root folder, as following:

``` 
 $ chmod 755 xoom
```

Ensure it's all set by verifying the `xoom-designer` version:

``` 
 $ ./xoom -version
 1.6.0
```

### SNAPSHOT Builds

You can get the latest snapshot build of the XOOM Designer from [GitHub Actions](https://github.com/vlingo/xoom-designer/actions) on the most recent daily build link. It's not obvious how to reach it, but it is also not difficult. Here is the best way to get there:

- Navigate to [GitHub Actions for XOOM Designer](https://github.com/vlingo/xoom-designer/actions)
- On that page, the most recent scheduled build (the build number will be higher than this example) is listed. Click on that link:
```
        Build
        Build #306: Scheduled
```
- On the specific build page there are two artifacts listed:
```
        JARs
        designer
```
- Click the link that says **designer** and it will start a download
- Follow the above [Installaiton](#Installation) steps

To build your own Designer from daily snapshots, see the [note for developers](https://github.com/vlingo/xoom-designer#note-for-developer).

#### GraalVM Build
```bash
mvn clean package -Pfrontend -Pnative-image
```
```bash
./target/xoom-designer designer
```
More details [GraalVM](GraalVM.md)

### [Note for Developers](https://github.com/vlingo/xoom-designer#note-for-developers)

If developing on the `xoom-designer` project, the `VLINGO_XOOM_DESIGNER_HOME` must be set a bit differently. It should point to an inner folder under the cloned repository. Using a *nix shell, such as `bash`, do this:

```
$ VLINGO_XOOM_DESIGNER_HOME=[git-clone-path]/dist/designer
$ export VLINGO_XOOM_DESIGNER_HOME
```

On Windows you can use the *System Properties > Advanced > Environment Variables...* to set the property permanently. For a one-time setting before running the design tool you can use the command line:

```
C:\> set VLINGO_XOOM_DESIGNER_HOME=[git-clone-path]\dist\designer
```

After making changes, from the root project directory build and run the designer. On *nix do this:

```
$ mvn clean package -P frontend
...
$ java -jar target/xoom-designer-1.8.9-SNAPSHOT.jar
```
On Windows, do this:

```
C:\[git-clone-path]> mvn clean package -P frontend
...
C:\[git-clone-path]> java -jar target\xoom-designer-1.8.9-SNAPSHOT.jar
```

We look forward to your VLINGO XOOM contribution!

### Project Generation

In addition to the command-line interface (see below), `xoom-designer` provides a web/graphical user interface for a rapid application generation. Simply open a terminal window and run the Designer.

``` 
 $ ./xoom designer
```

Following this your preferred browser will open with a wizard-fashioned screen, consisting of five steps.

**Context Step**

The first step defines the project artifact and service packaging.

![screen-sample-image](https://gblobscdn.gitbook.com/assets%2F-LLB-V2sJmANuWISDmBf%2F-MM0sShPfweroMLy17IM%2F-MM159JUDGJ54jMcKZX5%2Fimage.png)

Currently Maven build is supported. You may easily convert the generated project build to Gradle by using the [Gradle conversion version task](https://docs.gradle.org/current/userguide/migrating_from_maven.html#migmvn:automatic_conversion).

**Model - Aggregate Step**

The second step is used to design feature-based slices through the architecture. The vital parts of three architecture responsibilities are defined by means of a domain model focus. By first visually defining each domain model aggregate/entity type, its events emitted by each of its command message handlers and the REST API at the frontend, may be correspondingly specified.

![screen-sample-image](https://gblobscdn.gitbook.com/assets%2F-LLB-V2sJmANuWISDmBf%2F-MM1ED3Unz8WBDfG1Taa%2F-MM1FX_DCts7bJHJ5aLX%2Fimage.png)

In a single form, define the model aggregate/entity type, state, events, and REST API for :

* Aggregates and State: protocol name; message handler methods; state attributes/properties; emitted events
* Domain Events: name; attributes/properties
* REST Resources: HTTP method; URI path and path parameters; aggregate message handler method to which the resource dispatches

Following each aggregate definition, add it to the model and see it in the design view.

![screen-sample-image](https://gblobscdn.gitbook.com/assets%2F-LLB-V2sJmANuWISDmBf%2F-MM1IvPWmZlz3V0QdMPM%2F-MM1KFlltcy6E7Fo7DQ_%2Fimage.png?alt=media&token=3f9a7722-c1ef-4c3a-a84b-b61ba67926be)

Track each architectural slice, including REST API, aggregate, and emitted events, on the _Model - Aggregate_ design view. It shows an interesting angle of your model through a design-level _Event Storming_ perspective.

**Model - Persistence Step**

The third step is used to define the persistence, and if CQRS is in used, both command and query models.

![screen-sample-image](https://gblobscdn.gitbook.com/assets%2F-LLB-V2sJmANuWISDmBf%2F-MM1IvPWmZlz3V0QdMPM%2F-MM1OXroZiao0hr-lz0m%2Fimage.png)

This step displays persistence preferences. Configuration selections include the [storage type](https://docs.vlingo.io/xoom-symbio), CQRS usage, specific database mechanisms, and when applicable, how events are projected to the query model.

**Deployment Step**

The fourth step defines the deployment container types.

![screen-sample-image](https://gblobscdn.gitbook.com/assets%2F-LLB-V2sJmANuWISDmBf%2F-MM1OnfL6SZVQVnR5BAn%2F-MM1Rr473DOo9d6ijq4N%2Fimage.png)

The project generator provides default packaging, such as Java JAR, but may also include containerization files facilitating Docker and Kubernetes deployment. Using the _Deployment Step_, choose among deployment types as needed.  

**Generation Step**

The fifth and final step defines project component types, and generates the project.

![screen-sample-image](https://gblobscdn.gitbook.com/assets%2F-LLB-V2sJmANuWISDmBf%2F-MM1SB3vLO4INqIJkXOx%2F-MM1UeV0LJIuciHmQ8ax%2Fimage.png)

Enter the project parent folder. In addition, select whether VLINGO XOOM annotations and auto-dispatch are preferred, or not. Click Finish to generate the defined service project.

Once the five definition steps are completed and the service project is generated, take full advantage of the power of the VLINGO XOOM acceleration components. Use the VLINGO XOOM platform SDK omprehensive [documentation](https://docs.vlingo.io/) and its live and collaborative [community](https://gitter.im/vlingo-platform-java/community) that supports developers on their journey. Now, go have fun!

### Docker Commands

`xoom-designer` provides cool shortcuts for interacting with Docker, from the project root folder, as follows:

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
        <tr>
            <td align="center">xoom k8s push</td>
            <td align="left">Apply the manifest file(s) placed under <em>deployment/k8s</em> on Kubernetes.</td>
            <td align="center">N/A</td>
       </tr>
       <tr>
            <td align="center">xoom gloo init</td>
            <td align="left">Install the Gloo Gateway API generating an upstream for each running service on Kubernetes.</td>
            <td align="center">N/A</td>
       </tr>
       <tr>
            <td align="center">xoom gloo suspend</td>
            <td align="left">Uninstall the Gloo Gateway API.</td>
            <td align="center">N/A</td>
       </tr>
       <tr>
            <td align="center">xoom gloo route</td>
            <td align="left">Create routes, on Gloo Gateway API, for endpoints declared in <em>xoom-turbo.properties</em></td>
            <td align="center">In <em>xoom-turbo.properties</em> , define the options described below: 
             <p><b>gloo.upstream</b>: a Gloo upstream name for the app's service running on K8s. Example: <em>gloo.upstream = default-myxoomapp-8080</em></p>
             <p><b>gloo.resource.[resource-name]</b>: an endpoint for an application resource identified by resource-name. Example: <em>gloo.resource.balance = v1/balance</em></p>
             <p><b>gloo.gateway.[resource-name]</b>: a gateway route corresponding to a mapped endpoint identified by resource-name. Example: <em>gloo.gateway.balance= balance</em></p>
         Note that, for each resource, a pair of gloo.resource / gloo.gateway has to be informed for properly creating a route in the Gloo Gateway API.
        </td>
       </tr>
    </tbody>
</table>

## Collaboration

Our team really appreciates collaboration, not only because it boosts VLINGO to greater value, but also for the fact that the more viewpoints we have the more competent and mature the VLINGO community will become. If you want to be a catalyst for moving the platform forward, take a tour of our [development guide](https://github.com/vlingo/xoom-designer/blob/master/DEV-GUIDE.md).

License (See LICENSE file for full license)
-------------------------------------------
Copyright © 2012-2021 VLINGO LABS. All rights reserved.

This Source Code Form is subject to the terms of the
Mozilla Public License, v. 2.0. If a copy of the MPL
was not distributed with this file, You can obtain
one at https://mozilla.org/MPL/2.0/.