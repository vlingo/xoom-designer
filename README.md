# vlingo-xoom-starter
[![Build Status](https://travis-ci.org/vlingo/vlingo-xoom-starter.svg?branch=master)](https://travis-ci.org/vlingo/vlingo-xoom-starter)

The quick starter project generator for the vlingo/xoom quick start components

## Introduction
`vlingo-xoom-starter` enables project structure generation and components initialization for `vlingo/xoom` applications. The primary generation output contains: 
* `vlingo-xoom` initializer class
* Maven¹ build configuration (pom.xml)
* Default test class

The structure is represented below: 

```
    ├── src
    |   ├── main
    |   |     ├── java 
    |   |     |     └──[custom-package]
    |   |     |              └──  Bootstrap.java
    |   |     └── resources 
    |   |           ├──  application.yml
    |   |           └──  logback.xml  
    |   └── test
    |         └── java 
    |              └──[custom-package]
    |                       └──  BootstrapTest.java
    └── pom.xml                
```

## Requirements
* Java 7+
* Maven 3.x.x
* Docker Desktop 18.x

## Installation 

Download the `vlingo-xoom-starter` compressed distribution file via `curl`:

* `zip` file: `curl -L -O https://github.com/vlingo/vlingo-xoom-starter/raw/master/dist/starter.zip`
* `tar` file: `curl -L -O https://github.com/vlingo/vlingo-xoom-starter/raw/master/dist/starter.tar`

Extract the file content, then set an environment variable named `VLINGO_XOOM_STARTER_HOME` indicating the absolute path for the uncompressed folder. Additionally, on Unix-based operating systems, it is necessary to allow _read_ and _execute_ access on executable script, placed under the root folder, as following:

``` 
 $ ./chmod 755 xoom
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
* Rest Resources, CQRS Projections, Adapters and Data type;
* Store Providers supporting State Store and Journal;
* Containerization files facilitating Docker / Kubernetes deployment; 

## Additional Commands

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
            <td align="center">xoom gen</td>
            <td align="left">Allows application generation being an alternative for the graphical interface. In this case, the project settings have to be informed in a <a href="https://github.com/vlingo/vlingo-xoom-starter/blob/master/dist/starter/vlingo-xoom-starter.properties">properties file</a> under the `vlingo-xoom-starter` folder.</td>
            <td align="center">N/A</td>
        </tr>
        <tr>
            <td align="center">xoom docker package</td>
            <td align="left">Builds / update the Docker image from the application current state. Should be executed on the project root folder.</td>
            <td align="center"><strong>tag</strong>: relate a tag to the current image build. If not informed, the default value is <em>latest</em>. Example: xoom docker package --tag 1.0</td>
        </tr>
        <tr>
            <td align="center">xoom docker status</td>
            <td align="left">Shows the container status. Should be executed on the project root folder.</td>
            <td align="center">N/A</td>
        </tr>
        <tr>
            <td align="center">xoom docker push</td>
            <td align="left">Publishes the image into the configured docker repository.</td>
            <td align="center"><strong>tag</strong>: relate the local tag to the remote tag. If not informed, the default value is <em>latest:latest</em>, following the pattern <em>local-tag:remote-tag</em>. Example: xoom docker push --tag 1.0:latest</td>
        </tr>
    </tbody>
</table>


