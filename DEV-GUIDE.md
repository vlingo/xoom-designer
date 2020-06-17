## Development Guide

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

As easy as it seems, the [Rest Resource template file](https://github.com/vlingo/vlingo-xoom-starter/blob/master/src/main/resources/codegen/RestResource.ftl) requires only two parameters values to generate a `Rest Resource` class: `packageName` and `resourceName`. The parameters handling and mapping are addressed by [RestResourceTemplateData](https://github.com/vlingo/vlingo-xoom-starter/blob/master/src/main/java/io/vlingo/xoom/starter/task/template/code/resource/RestResourceTemplateData.java) as follows:  

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

`RestResource` classes should be placed under its own package. Hence, the `resolvePackage` method appends the project base package to the `resource` package. The full package name and the `RestResource` classname are mapped to the template parameters in `loadParameters`. Additionally, [TemplateData](https://github.com/vlingo/vlingo-xoom-starter/blob/master/src/main/java/io/vlingo/xoom/starter/task/template/code/TemplateData.java) requires the [file method](https://github.com/vlingo/vlingo-xoom-starter/blob/master/src/main/java/io/vlingo/xoom/starter/task/template/code/TemplateData.java#L19) implementation, which can simply invoke a parent method passing the file absolute path and the filename.

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

[RestResourceGenerationStep](https://github.com/vlingo/vlingo-xoom-starter/blob/master/src/main/java/io/vlingo/xoom/starter/task/template/steps/RestResourceGenerationStep.java) implements `buildTemplateData` method that passes parameter values, coming from the Web-based UI or properties file, to [RestResourceTemplateData](https://github.com/vlingo/vlingo-xoom-starter/blob/master/src/main/java/io/vlingo/xoom/starter/task/template/code/resource/RestResourceTemplateData.java). In this particular scenario, [RestResourceTemplateDataFactory](https://github.com/vlingo/vlingo-xoom-starter/blob/master/src/main/java/io/vlingo/xoom/starter/task/template/code/resource/RestResourceTemplateDataFactory.java)  is an additional and optional class that helps building [RestResourceTemplateData](https://github.com/vlingo/vlingo-xoom-starter/blob/master/src/main/java/io/vlingo/xoom/starter/task/template/code/resource/RestResourceTemplateData.java). The [shouldProcess method](https://github.com/vlingo/vlingo-xoom-starter/blob/master/src/main/java/io/vlingo/xoom/starter/task/steps/TaskExecutionStep.java#L16) is also optional and useful when a [TemplateProcessingStep](https://github.com/vlingo/vlingo-xoom-starter/blob/master/src/main/java/io/vlingo/xoom/starter/task/template/steps/TemplateProcessingStep.java) subclass needs to be conditionally skipped.


Finally, [TemplateProcessingStep](https://github.com/vlingo/vlingo-xoom-starter/blob/master/src/main/java/io/vlingo/xoom/starter/task/template/steps/TemplateProcessingStep.java) has to be added in the [Configuration](https://github.com/vlingo/vlingo-xoom-starter/blob/master/src/main/java/io/vlingo/xoom/starter/Configuration.java) steps list:

<pre>
<code>
    public static final List<TaskExecutionStep> TEMPLATE_GENERATION_STEPS = Arrays.asList(
            new ResourcesLocationStep(), new PropertiesLoadStep(), new ArchetypeCommandResolverStep(),
            new CommandExecutionStep(), new LoggingStep(), new StatusHandlingStep(),

            <strong>// TemplateProcessingSteps starts here</strong>

            new ModelGenerationStep(),
            new ProjectionGenerationStep(),
            new StorageGenerationStep(),
            <strong>new RestResourceGenerationStep(),</strong>
            new BootstrapGenerationStep(),

            <strong>// TemplateProcessingSteps ends here</strong>

            new ContentCreationStep(), new ContentPurgerStep()
    );
</code>
</pre>

Eventually, some peripherals points in the code are also involved. The following list are mainly related when a new template file is added:

1.  Create a enum value in [CodeTemplateFile](https://github.com/vlingo/vlingo-xoom-starter/blob/master/src/main/java/io/vlingo/xoom/starter/task/template/code/CodeTemplateFile.java) passing the template filename (without extension) in the construtor. Example:

<pre>
<code>
    public enum CodeTemplateFile {

        //Other template filenames

        <strong>REST_RESOURCE("RestResource")</strong>

        //Enum attributes
    }
</code>
</pre>

2. Map the new standard file to an existing [CodeTemplateStandard](https://github.com/vlingo/vlingo-xoom-starter/blob/master/src/main/java/io/vlingo/xoom/starter/task/template/code/CodeTemplateStandard.java) or create a new one. Sometimes there are multiple files for the same standard. For instance, there is one `Aggregate` template file for each `Storage` (Journal, State Store, Object Store). That means [CodeTemplateStandard](https://github.com/vlingo/vlingo-xoom-starter/blob/master/src/main/java/io/vlingo/xoom/starter/task/template/code/CodeTemplateStandard.java) is responsible for grouping template files by standard and helps the [CodeTemplateProcessor](https://github.com/vlingo/vlingo-xoom-starter/blob/master/src/main/java/io/vlingo/xoom/starter/task/template/code/CodeTemplateProcessor.java) to find the proper file based on [CodeTemplateParameters](https://github.com/vlingo/vlingo-xoom-starter/blob/master/src/main/java/io/vlingo/xoom/starter/task/template/code/CodeTemplateParameters.java) such as [StorageType](https://github.com/vlingo/vlingo-xoom-starter/blob/master/src/main/java/io/vlingo/xoom/starter/task/template/code/storage/StorageType.java). The examples below demonstrates the `Aggregate` and `Rest Resource` standards. The latter has only one related template file:

<pre>
<code>
    public enum CodeTemplateStandard {
        
        <strong>AGGREGATE(parameters -> AGGREGATE_TEMPLATES.get(parameters.from(STORAGE_TYPE))),</strong>
        <strong>REST_RESOURCE(parameters -> CodeTemplateFile.REST_RESOURCE.filename),</strong>

        //Other standards
    }
</code>
</pre>

3. In case it doesn't already exist, create a enum value in [CodeTemplateParameters](https://github.com/vlingo/vlingo-xoom-starter/blob/master/src/main/java/io/vlingo/xoom/starter/task/template/code/CodeTemplateParameters.java) for each template parameter.

In sum, those are the common steps regarding `code template files` on `vlingo-xoom-starter`. Our team is available to discuss and provide more information on [Gitter](https://gitter.im/vlingo-platform-java/community/).