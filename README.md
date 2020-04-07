# vlingo-xoom-starter
[![Build Status](https://travis-ci.org/vlingo/vlingo-xoom-starter.svg?branch=master)](https://travis-ci.org/vlingo/vlingo-xoom-starter)

The quick starter project generator for the vlingo/xoom quick start components

## Capabilities
`vlingo-xoom-starter` provides an interface that enables project structure generation for `vlingo-xoom` applications. The primary generation output contains: 
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

## Installation
Install `vlingo-xoom-starter` by downloading the compressed file via `curl`:

> ZIP


> TAR.GZ

