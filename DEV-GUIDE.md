### Development Guide

In this section, practical development steps are discussed for anyone interested in contributing to `vlingo-xoom-starter`. 

### Introduction to `Application Generation` feature
     
The following diagram gives us an `Application Generation` overview showing its components interaction:

<p align="center">
    <img src="https://github.com/vlingo/vlingo-xoom-starter/blob/documentation/docs/raw-proj-gen-diagram.png" height="400" />
</p>

As illustrated above, `Application Generation` can be run by [two commands](https://github.com/vlingo/vlingo-xoom-starter/blob/documentation/README.md#application-generation): `xoom gen` or `xoom gui`. Both are alternative ways for a quick start with `vlingo\xoom`, having exactly the same parameters list. The only difference is that the latter reads parameters from a properties file, while the first consumes parameters from web-based UI.

The second half of the diagram shows some tools that perform core actions. [Maven Archetypes](https://maven.apache.org/guides/introduction/introduction-to-archetypes.html) creates the project structure dynamically organizing the directory hierarchy, Maven configuration, also handling  deployment resources as Dockerfile and K8s manifest file. Further, [Apache FreeMarker](https://freemarker.apache.org/) takes care of classes generation by processing preexisting code templates. That said, let's see how to add templates at code level.

### Create / Update Code Templates

The main constituent parts for every auto-generated class are: 
* A Freemarker template file
* A [io.vlingo.xoom.starter.task.template.code.TemplateData](https://github.com/vlingo/vlingo-xoom-starter/blob/master/src/main/java/io/vlingo/xoom/starter/task/template/code/TemplateData.java) implementation
* A [io.vlingo.xoom.starter.task.template.steps.TemplateProcessingStep](https://github.com/vlingo/vlingo-xoom-starter/blob/master/src/main/java/io/vlingo/xoom/starter/task/template/steps/TemplateProcessingStep.java) implementation

Considering those parts, let's take `RestResource` classes generation as example and go through the implementation details, starting from the template file:

```
package ${packageName};

import io.vlingo.http.resource.Resource;
import static io.vlingo.http.resource.ResourceBuilder.resource;

public class ${resourceName}  {

    public Resource<?> routes() {
        return resource("${resourceName}" /*Add Request Handlers here as a second parameter*/);
    }

}
```

As easy as it seems, the [Rest Resource template file](https://github.com/vlingo/vlingo-xoom-starter/blob/master/src/main/resources/codegen/RestResource.ftl) requires only two parameters values to generate a `Rest Resource` class: `packageName` and `resourceName`. The parameters handling and mapping are addressed by [RestResourceTemplateData] as follows:  

```
public class RestResourceTemplateData extends TemplateData {

    private final static String PACKAGE_PATTERN = "%s.%s";
    private final static String PARENT_PACKAGE_NAME = "resource";

    private final String aggregateName;
    private final String packageName;
    private final String absolutePath;
    private final CodeTemplateParameters parameters;

    public RestResourceTemplateData(final String aggregateName,
                                    final String basePackage,
                                    final String projectPath) {
        this.aggregateName = aggregateName;
        this.packageName = resolvePackage(basePackage);
        this.absolutePath = resolveAbsolutePath(basePackage, projectPath, PARENT_PACKAGE_NAME);
        this.parameters = loadParameters();
    }

    private CodeTemplateParameters loadParameters() {
        return CodeTemplateParameters
                .with(CodeTemplateParameter.REST_RESOURCE_NAME, CodeTemplateStandard.REST_RESOURCE.resolveClassname(aggregateName))
                .and(CodeTemplateParameter.PACKAGE_NAME, packageName);
    }

    private String resolvePackage(final String basePackage) {
        return String.format(PACKAGE_PATTERN, basePackage, PARENT_PACKAGE_NAME).toLowerCase();
    }

    @Override
    public File file() {
        return buildFile(absolutePath, aggregateName);
    }

    @Override
    public CodeTemplateParameters parameters() {
        return parameters;
    }

    @Override
    public CodeTemplateStandard standard() {
        return CodeTemplateStandard.REST_RESOURCE;
    }

}
```

`RestResource` classes should be placed under its own package. Hence, the `resolvePackage` method appends the project base package to the `resource` package. The full package name and the `RestResource` classname are mapped to the template parameters in `loadParameters`. Additionally, `TemplateData` requires the `file` method implementation, which can simply invoke a parent method passing the file absolute path and the filename.

```
public class RestResourceGenerationStep extends TemplateProcessingStep {

    @Override
    protected List<TemplateData> buildTemplateData(final TaskExecutionContext context) {
        final String projectPath = context.projectPath();
        final String basePackage = context.propertyOf(Property.PACKAGE);
        final String restResourcesData = context.propertyOf(Property.REST_RESOURCES);
        return RestResourceTemplateDataFactory.build(basePackage, projectPath, restResourcesData);
    }

    @Override
    public boolean shouldProcess(final TaskExecutionContext context) {
        return context.hasProperty(Property.REST_RESOURCES);
    }

}
```

`RestResourceGenerationStep` implements `buildTemplateData` method that passes parameter values, coming from the Web-based UI or properties file, to `RestResourceTemplateData`. In this particular scenario, `RestResourceTemplateDataFactory` is an additional and optional class that helps building `RestResourceTemplateData`. The `shouldProcess` method is also optional and useful when a `TemplateProcessingStep` subclass needs to be conditionally skipped.
