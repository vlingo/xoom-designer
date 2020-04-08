# vlingo-xoom-starter
[![Build Status](https://travis-ci.org/vlingo/vlingo-xoom-starter.svg?branch=master)](https://travis-ci.org/vlingo/vlingo-xoom-starter)

The quick starter project generator for the vlingo/xoom quick start components

## Capabilities
`vlingo-xoom-starter` provides an interface that enables project structure generation and components initialization for `vlingo/xoom` applications. The primary generation output contains: 
* `vlingo-xoom` initializer class
* Default test class
* Micronaut and Logback configuration files
* Maven build configuration (pom.xml)

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

## Setup and Generation

Download the `vlingo-xoom-starter` compressed file via `curl`:

* For `zip` file: `curl https://github.com/vlingo/vlingo-xoom-starter/blob/master/dist/starter.zip -output starter.zip`
* For `tar.gz` file: `curl https://github.com/vlingo/vlingo-xoom-starter/blob/master/dist/starter.tar.gz -output starter.tar.gz`

Extract the file content into any folder and customize the `vlingo/xoom` project generation by editing `vlingo-xoom-starter.properties` under the root folder. The available parameters are described below:

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
            <td align="left">The vlingo-xoom-server dependency version.</td>
            <td align="center">Yes</td>
        </tr>
        <tr>
            <td align="center">deployment</td>
            <td align="left">Enables config generation for a specific deployment environment. Currently, only Kubernetes is supported by setting "k8s" in this property.</td>
            <td align="center">No</td>
        </tr>
        <tr>
            <td align="center">k8s.image</td>
            <td align="left">The application Docker image on Kubernetes.</td>
            <td align="center">Conditional (mandatory when the deployment property is "k8s")</td>
        </tr>
        <tr>
            <td align="center">k8s.pod.name</td>
            <td align="left">Application POD name on Kubernetes.</td>
            <td align="center">Conditional (mandatory when the deployment property is "k8s")</td>
        </tr>
    </tbody>
</table>

The following snippet shows a `vlingo-xoom-starter.properties` example:

``` 
    version=1.0
    group.id=com.company
    artifact.id=xoom-application
    package=com.company.business
    target.folder=/home/user/Projects/
    vlingo.xoom.server.version=1.2.10-SNAPSHOT
    deployment=k8s
    k8s.image=xoom-application
    k8s.pod.name=xoom-application
```

In the example above, Kubernetes deployment is enabled, consequently, `vlingo-xoom-starter` will generate two additional files: a `Dockerfile` and a Kubernetes resource YAML file, respectively under the root folder and deployment folder. 

Once the properties are properly configured, open the terminal and run the generation script **as administrator** in the `vlingo-xoom-starter` root folder:

* For `bat` script: `start.bat`
* For `sh` script: `chmod +x start.sh || ./start.sh `

The log will print the progress and, when the generation is successfully completed, a message is shown: 

```
    vlingo/xoom template has been successfully generated under [targer-folder].
```

## Supported Build Tool

The current `vlingo-xoom-starter` version only supports Maven.


