# vlingo-xoom-starter
[![Build Status](https://travis-ci.org/vlingo/vlingo-xoom-starter.svg?branch=master)](https://travis-ci.org/vlingo/vlingo-xoom-starter)

The quick starter project generator for the vlingo/xoom quick start components

## Capabilities
`vlingo-xoom-starter` provides an interface that enables project structure generation and components initialization for `vlingo/xoom` applications. The primary generation output contains: 
* `vlingo-xoom` initializer class
* Default test class
* Micronaut and Logback configuration files
* Maven¹ build configuration (pom.xml)

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

## Installation 

Download the `vlingo-xoom-starter` compressed distribution file via `curl`:

* `zip` file: `curl -L -O https://github.com/vlingo/vlingo-xoom-starter/raw/master/dist/starter.zip`
* `tar` file: `curl -L -O https://github.com/vlingo/vlingo-xoom-starter/raw/master/dist/starter.tar`

Extract the file content into any folder, then set an environment variable named `VLINGO_XOOM_STARTER_HOME` indicating the absolute path for that folder. Additionally, in Unix based OS, it is necessary to allow _read_ and _execute_ access on executable script, placed under the root folder, as following:

``` 
 $ ./chmod 755 xoom
```

Ensure that it's all set by checking the `vlingo-xoom-starter` version:

``` 
 $ ./xoom -version
 1.3.0
```

## Application Generation 

`vlingo-xoom-starter` provides an user interface for a real quick application generation. Simply, execute `xoom gui` and your preferred browser will be opened with a wizard-fashioned interface. 

![screen-sample-image](https://github.com/vlingo/vlingo-xoom-starter/raw/user-interface/src/assets/img/screen-sample.png)
     