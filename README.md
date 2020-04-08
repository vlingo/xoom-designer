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

| Parameter                     | Description                                        | Mandatory |
| :----:                        | :---                                               | :----:    |
| version                       | Maven project initial version.                     | Yes       |
| group.id                      | Maven group id.                                    | Yes       |
| artifact.id                   | Maven artifact id.                                 | Yes       |
| package                       | Project base package.                              | Yes       |
| target.folder                 | Absolute path to the generated project folder.     | Yes       |
| vlingo.xoom.server.version    | The `vlingo-xoom-server` dependency version.       | Yes       |

# Supported Build Tool

In its current version, Maven is the only supported build tool by `vlingo-xoom-starter`.


