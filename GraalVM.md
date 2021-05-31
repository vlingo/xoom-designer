# XOOM Designer - GraalVM Support

The VLINGO XOOM Designer to guide you in rapid delivery of low-code to full-code Reactive, Event-Driven Microservices and Applications using DOMA, DDD, and other approaches.

Docs: https://docs.vlingo.io/xoom-designer

### Features
<img src="https://vlingo.io/wp-content/uploads/2021/04/xoom-designer-scrn.png" width="70%" height="70%">

## Getting started

Prerequisites:
* Java JDK 8 or greater
* Maven
* [GraalVM 21.1.0 Java 8/11](https://www.graalvm.org/docs/getting-started/)

## Maven build
```bash
mvn clean package -Pfrontend
```
- Generate native image resources Configs
```bash
java -agentlib:native-image-agent=config-output-dir=src/main/resources/META-INF/native-image/io.vlingo.xoom/xoom-designer -jar target/xoom-designer-<version>-SNAPSHOT.jar gui --target zip-download
```
### Tips to load more resources and reflection for the agent
- Open browser and Reload all web pages to register all resources files
- Test functionality
- Delete from resource-config.json all `{"pattern":"\\Qfrontend/<directory>\\E"},`
- Adding missing serialization:
```json
[
  ...
  {
    "name": "java.sql.Timestamp"
  },
  {
    "name": "java.lang.Object"
  },
  {
    "name": "java.lang.Boolean"
  },
  {
    "name": "java.lang.Character"
  },
  {
    "name": "java.lang.Double"
  },
  {
    "name": "java.lang.Float"
  },
  {
    "name": "java.lang.Long"
  },
  {
    "name": "java.lang.Integer"
  },
  {
    "name": "java.lang.Short"
  },
  {
    "name": "java.lang.Byte"
  },
  {
    "name": "java.lang.String"
  },
  ...
]
```

## Native Image Maven Plugin & GraalVM SDK
```
<properties>
    ...
    <exec.mainClass>io.vlingo.xoom.designer.Initializer</exec.mainClass>
    <graalvm.version>21.1.0</graalvm.version>
    ...
</properties>
<dependencies>
    ...
    <dependency>
      <groupId>org.graalvm.sdk</groupId>
      <artifactId>graal-sdk</artifactId>
      <version>${graalvm.version}</version>
      <scope>provided</scope>
    </dependency>
    ...
</dependencies>

```
```
<profiles>
    ...
    <profile>
      <id>native-image</id>
      <build>
        <plugins>
          <plugin>
            <groupId>org.graalvm.nativeimage</groupId>
            <artifactId>native-image-maven-plugin</artifactId>
            <version>${graalvm.version}</version>
            <executions>
              <execution>
                <goals>
                  <goal>native-image</goal>
                </goals>
                <phase>package</phase>
              </execution>
            </executions>
            <configuration>
              <imageName>${project.name}</imageName>
              <mainClass>${exec.mainClass}</mainClass>
              <buildArgs>
                --no-fallback --no-server --enable-url-protocols=http -H:+AllowIncompleteClasspath
                -H:ReflectionConfigurationFiles=classes/META-INF/native-image/reflect-config.json
                -H:ResourceConfigurationFiles=classes/META-INF/native-image/resource-config.json
                -H:SerializationConfigurationFiles=classes/META-INF/native-image/serialization-config.json
                --initialize-at-build-time=com.google.common.jimfs.SystemJimfsFileSystemProvider
                --initialize-at-run-time=io.netty
                --initialize-at-run-time=io.vlingo.xoom.common.identity.IdentityGeneratorType
                --report-unsupported-elements-at-runtime
                --allow-incomplete-classpath
              </buildArgs>
            </configuration>
          </plugin>
        </plugins>
      </build>
    </profile>
    ...
</profiles>
```
- To build the native image run:
```bash
mvn clean package -Pfrontend -Pnative-image
```
```bash
./target/xoom-designer gui --target zip-download
```
- On native image runtime, an exception is always thrown, issue described here: [ISSUE](https://github.com/RuedigerMoeller/fast-serialization/issues/313)

## Docker build and run
- Increase the Docker Memory Resource to +8Go.
- First build the jar file:
```bash
mvn clean package -Pfrontend
```
- Build the docker image
```bash
docker build -f Dockerfile.native -t vlingo/xoom-designer .
```
- Run the docker image
```bash
docker run -it --rm -p '19090:19090' -v $(pwd)/projects:/designer/VLINGO-XOOM vlingo/xoom-designer
```
- On native image runtime, a netty native transport epoll issue: [PR](https://github.com/netty/netty/pull/11163)
