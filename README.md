# vlingo-xoom-starter

[![Build](https://github.com/vlingo/vlingo-xoom-starter/workflows/Build/badge.svg)](https://github.com/vlingo/vlingo-xoom-starter/actions?query=workflow%3ABuild)

Be guided into the Reactive DOMA and DDD world by the project quick start for the VLINGO XOOM components.

Docs: https://docs.vlingo.io/vlingo-xoom/xoom-starter

### Important
If using snapshot builds [follow these instructions](https://github.com/vlingo/vlingo-platform#snapshots-repository) or you will experience failures.

## Introduction

A common reality of unfamiliarity exists for many developers either when they try to develop either [Reactive](https://docs.vlingo.io/overview/reactive-and-computing-health) applications or to implement DDD properly, or both. Many give up on using concepts and paradigms that are proven to be a strong foundation for building modern applications and systems that are robust, modularized, scalable, and that use modern architectures. These developers tend to fall back to familiar, yet outdated, frameworks and tools.

The VLINGO XOOM platform SDK was created to help developers who face such challenges to confidently move forward and modernize their work. One platform component that greatly accelerates developer modernization efforts is the VLINGO XOOM Designer. It supports visual model definition, REST API, persistence, and container definitions, followed by project generation that can be immediately built and your services can be running within minutes. With an instantly executable service experience, developers are in a position to quick implement custom business logic within the pre-generated model. This is a great way to learn Reactive architecture and coupled with DDD tactical modeling that are powered by an Actor-based ecosystem. 

The next section show you how to run and use the VLINGO XOOM Designer in your local environment. Let's get started!

## Installation 

The installation process is short. Before you start, just check if you have these tools already installed:

* Java 8+
* Maven 3.x.x
* Docker Desktop 18.x

Download the `vlingo-xoom-starter` compressed distribution file via `curl`:

* `zip` file: `curl -L -O https://github.com/vlingo/vlingo-xoom-starter/releases/latest/download/starter.zip`
* `tar` file: `curl -L -O https://github.com/vlingo/vlingo-xoom-starter/releases/latest/download/starter.tar`

Extract the file content, then set an environment variable named `VLINGO_XOOM_STARTER_HOME` indicating the absolute path of the uncompressed folder; that is, the installation folder. Using a *nix shell, such as `bash`, do this:

```
 $ VLINGO_XOOM_STARTER_HOME=[installation-path]
 $ export VLINGO_XOOM_STARTER_HOME
```

On Windows you can use the _System Properties > Advanced > Environment Variables..._ to set the property permanently. For a one-time setting before running the design tool you can use the command line:

```
 C:\> set VLINGO_XOOM_STARTER_HOME=[installation-path]
```

Additionally, on *nix systems, it is necessary to set _read_ and _execute_ access on executable script, placed under the root folder, as following:

``` 
 $ chmod 755 xoom
```

Ensure it's all set by verifying the `vlingo-xoom-starter` version:

``` 
 $ ./xoom -version
 1.6.0
```

### Note for Developers

If developing on the `vlingo-xoom-start` project, the `VLINGO_XOOM_STARTER_HOME` must be set a bit differently. It should point to an inner folder under the cloned repository. Using a *nix shell, such as `bash`, do this:

```
$ VLINGO_XOOM_STARTER_HOME=[git-clone-path]/dist/starter
$ export VLINGO_XOOM_STARTER_HOME
```

On Windows you can use the *System Properties > Advanced > Environment Variables...* to set the property permanently. For a one-time setting before running the design tool you can use the command line:

```
C:\> set VLINGO_XOOM_STARTER_HOME=[git-clone-path]\dist\starter
```

After making changes, from the root project directory build and run the starter. On *nix do this:

```
$ mvn clean package -P frontend
...
$ java -jar target/vlingo-xoom-starter-1.6.1-SNAPSHOT.jar gui
```
On Windows, do this:

```
C:\[git-clone-path]> mvn clean package -P frontend
...
C:\[git-clone-path]> java -jar target\vlingo-xoom-starter-1.6.1-SNAPSHOT.jar gui
```

We look forward to your VLINGO XOOM contribution!

### Application Generation

In addition to the command-line interface (see below), `vlingo-xoom-starter` provides a web/graphical user interface for a rapid application generation. Simply open a terminal window and run the Starter.

``` 
 $ ./xoom gui
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

This step displays persistence preferences. Configuration selections include the [storage type](https://docs.vlingo.io/vlingo-symbio), CQRS usage, specific database mechanisms, and when applicable, how events are projected to the query model.

**Deployment Step**

The fourth step defines the deployment container types.

![screen-sample-image](https://gblobscdn.gitbook.com/assets%2F-LLB-V2sJmANuWISDmBf%2F-MM1OnfL6SZVQVnR5BAn%2F-MM1Rr473DOo9d6ijq4N%2Fimage.png)

The project generator provides default packaging, such as Java JAR, but may also include containerization files facilitating Docker and Kubernetes deployment. Using the _Deployment Step_, choose among deployment types as needed.  

**Generation Step**

The fifth and final step defines project component types, and generates the project.

![screen-sample-image](https://gblobscdn.gitbook.com/assets%2F-LLB-V2sJmANuWISDmBf%2F-MM1SB3vLO4INqIJkXOx%2F-MM1UeV0LJIuciHmQ8ax%2Fimage.png)

Enter the project parent folder. In addition, select whether VLINGO XOOM annotations and auto-dispatch are preferred, or not. Click Finish to generate the defined service project.

Once the five definition steps are completed and the service project is generated, take full advantage of the power of the VLINGO XOOM acceleration components. Use the VLINGO XOOM platform SDK omprehensive [documentation](https://docs.vlingo.io/) and its live and collaborative [community](https://gitter.im/vlingo-platform-java/community) that supports developers on their journey. Now, go have fun!
     
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
            <td align="left">Create routes, on Gloo Gateway API, for endpoints declared in <em>vlingo-xoom.properties</em></td>
            <td align="center">In <em>vlingo-xoom.properties</em> , define the options described below: 
             <p><b>gloo.upstream</b>: a Gloo upstream name for the app's service running on K8s. Example: <em>gloo.upstream = default-myxoomapp-8080</em></p>
             <p><b>gloo.resource.[resource-name]</b>: an endpoint for an application resource identified by resource-name. Example: <em>gloo.resource.balance = v1/balance</em></p>
             <p><b>gloo.gateway.[resource-name]</b>: a gateway route corresponding to a mapped endpoint identified by resource-name. Example: <em>gloo.gateway.balance= balance</em></p>
         Note that, for each resource, a pair of gloo.resource / gloo.gateway has to be informed for properly creating a route in the Gloo Gateway API.
        </td>
       </tr>
    </tbody>
</table>

## Building snapshots

To build examples from this repository you'll need access to VLINGO snapshot builds on
[GitHub Packages](https://github.com/vlingo/vlingo-platform/packages).

GitHub [requires authentication with a Personal Access Token](https://docs.github.com/en/packages/guides/configuring-apache-maven-for-use-with-github-packages#authenticating-with-a-personal-access-token)
to use their Maven repository.
In order to build VLINGO examples locally, you will need to configure the following in your `~/.m2/settings.xml`:

```xml
<settings>
  <servers>
    <server>
      <id>github</id>
      <username>GITHUB-USERNAME</username>
      <password>GITHUB-PERSONAL-ACCESS-TOKEN</password>
    </server>
  </servers>
</settings>
```

Replace `GITHUB-USERNAME` with your GitHub username, and `GITHUB-PERSONAL-ACCESS-TOKEN` with your Personal Access Token.
Personal Access Tokens can be created in Settings > Developer Settings > [Personal Access Tokens](https://github.com/settings/tokens) on GitHub.
Remember to create the token with `read:packages` scope.

## Collaboration

Our team really appreciates collaboration, not only because it boosts VLINGO to greater value, but also for the fact that the more viewpoints we have the more competent and mature the VLINGO community will become. If you want to be a catalyst for moving the platform forward, take a tour of our [development guide](https://github.com/vlingo/vlingo-xoom-starter/blob/master/DEV-GUIDE.md).

License (See LICENSE file for full license)
-------------------------------------------
Copyright © 2012-2020 VLINGO LABS. All rights reserved.

This Source Code Form is subject to the terms of the
Mozilla Public License, v. 2.0. If a copy of the MPL
was not distributed with this file, You can obtain
one at https://mozilla.org/MPL/2.0/.
