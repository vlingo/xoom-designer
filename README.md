# vlingo-xoom-starter
[![Build Status](https://travis-ci.org/vlingo/vlingo-xoom-starter.svg?branch=master)](https://travis-ci.org/vlingo/vlingo-xoom-starter)

The quick starter project generator for the vlingo/xoom quick start components

## Capabilities
`vlingo-xoom-starter` provides an interface that enables project structure generation for `vlingo/xoom` applications. The primary generation output contains: 
* `vlingo-xoom` initializer class
* Default test class
* Micronaut and Logback configuration files
* Maven build configuration (pom.xml)

The structure is represented below: 

```

    └── src
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

## Setup and Running
Download the `vlingo-xoom-starter` compressed file via `curl`:

* For `zip` file: `curl https://github.com/vlingo/vlingo-xoom-starter/blob/master/dist/starter.zip -output starter.zip`
* For `tar.gz` file: `curl https://github.com/vlingo/vlingo-xoom-starter/blob/master/dist/starter.tar.gz -output starter.tar.gz`

Extract the file content into any location and configure the `vlingo/xoom` project by editing `vlingo-xoom-starter.properties` under the root folder. The available parameters are described below:

<table>
    <thead>
        <tr>
            <th align="center">Parameter</th>
            <th align="center">Description</th>
            <th align="center">Mandatory</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td align="center">version</td>
            <td align="left">Maven project initial version.</td>
            <td align="center">Yes</td>
        </tr>
        <tr>
            <td align="center">group.id</td>
            <td align="left">Maven artifact group id.</td>
            <td align="center">Yes</td>
        </tr>
        <tr>
            <td align="center">artifact.id</td>
            <td align="left">Maven artifact id.</td>
            <td align="center">Yes</td>
        </tr>
        <tr>
            <td align="center">package</td>
            <td align="left">Project base package.</td>
            <td align="center">Yes</td>
        </tr>
        <tr>
            <td align="center">target.folder</td>
            <td align="left">Absolute path to the generated project folder.</td>
            <td align="center">Yes</td>
        </tr>
        <tr>
            <td align="center">vlingo.xoom.server.version</td>
            <td align="left">The `vlingo-xoom-server` dependency version.</td>
            <td align="center">Yes</td>
        </tr>
        <tr>
            <td align="center">deployment</td>
            <td align="left">Enables config generation for a specific deployment environment. Currently, only Kubernetes is supported by setting `k8s` value in this property.</td>
            <td align="center">No</td>
        </tr>
    </tbody>
</table>



# Supported Build Tool

In its current version, Maven is the only supported build tool by `vlingo-xoom-starter`.


