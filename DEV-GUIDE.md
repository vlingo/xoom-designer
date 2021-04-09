## Development Guide

In this section, practical development steps are discussed for anyone interested in contributing to `xoom-designer`. 

### Introduction to `Application Generation` feature
     
The following diagram gives us an `Application Generation` overview showing the interaction of its components:

<p align="center">
    <img src="https://github.com/vlingo/xoom-designer/blob/master/docs/raw-proj-gen-diagram.png" height="400" />
</p>

As illustrated above, `Application Generation` can be run by [two commands](https://github.com/vlingo/xoom-designer/blob/documentation/README.md#application-generation): `xoom gen` or `xoom gui`. Both are alternative ways for a quick start with `VLINGO/XOOM`, having exactly the same parameters list. The only difference is that the latter reads parameters from a properties file, while the first consumes parameters from a web-based UI.

The second half of the diagram shows some tools that perform core actions. [Maven Archetypes](https://maven.apache.org/guides/introduction/introduction-to-archetypes.html) creates the project structure, dynamically organizing the directory hierarchy, Maven configuration, also handling deployment resources as Dockerfile and K8s manifest file. Further, [Apache FreeMarker](https://freemarker.apache.org/) takes care of classes generation by processing preexisting code templates. That said, let's see how to add templates at code level.

### Create / Update Code Templates

The main constituent parts for every auto-generated class are: 
* A Freemarker template file
* A [io.vlingo.xoom.turbo.codegen.template.TemplateData](https://github.com/vlingo/xoom-turbo/blob/50568b630d92e7dd9b3389496ebf5602d8a84755/src/main/java/io/vlingo/xoom/turbo/codegen/template/TemplateData.java) implementation
* A [io.vlingo.xoom.turbo.codegen.template.TemplateProcessingStep](https://github.com/vlingo/xoom-turbo/blob/50568b630d92e7dd9b3389496ebf5602d8a84755/src/main/java/io/vlingo/xoom/turbo/codegen/template/TemplateProcessingStep.java) implementation

Considering those parts, let's take `RestResource` class generation as an example and go through the implementation details, starting from the template file:

```
package ${packageName};

import io.vlingo.xoom.http.resource.Resource;
import static io.vlingo.xoom.http.resource.ResourceBuilder.resource;

public class ${resourceName}  {

    public Resource<?> routes() {
        return resource("${resourceName}" /*Add Request Handlers here as a second parameter*/);
    }

}
```

As easy as it seems, the [Rest Resource template file](https://github.com/vlingo/xoom-turbo/blob/066a5630bf06cbed350e676ded6eeb16a99fbd0d/src/main/resources/codegen/RestResource.ftl) requires only two parameters values to generate a `Rest Resource` class: `packageName` and `resourceName`. The parameters handling and mapping are addressed by [RestResourceTemplateData](https://github.com/vlingo/xoom-turbo/blob/master/src/main/java/io/vlingo/xoom/turbo/codegen/template/resource/RestResourceTemplateData.java) as follows:  

```
public class RestResourceTemplateData extends TemplateData {

    private final static String PACKAGE_PATTERN = "%s.%s";
    private final static String PARENT_PACKAGE_NAME = "resource";

    private final String packageName;
    private final String aggregateName;
    private final TemplateParameters parameters;

    public RestResourceTemplateData(final String aggregateName,
                                    final String basePackage) {
        this.aggregateName = aggregateName;
        this.packageName = resolvePackage(basePackage);
        this.parameters = loadParameters();
    }

    private TemplateParameters loadParameters() {
        return TemplateParameters
                .with(REST_RESOURCE_NAME, REST_RESOURCE.resolveClassname(aggregateName))
                .and(PACKAGE_NAME, packageName);
    }

    private String resolvePackage(final String basePackage) {
        return String.format(PACKAGE_PATTERN, basePackage, PARENT_PACKAGE_NAME).toLowerCase();
    }

    @Override
    public TemplateStandard standard() {
        return REST_RESOURCE;
    }

    @Override
    public TemplateParameters parameters() {
        return parameters;
    }

    @Override
    public String filename() {
        return standard().resolveFilename(aggregateName, parameters);
    }

}
```

`RestResource` classes should be placed under its own package. Hence, the `resolvePackage` method appends the project base package to the `resource` package. The full package name and the `RestResource` class name are mapped to the template parameters in `loadParameters`. Additionally, [TemplateData](https://github.com/vlingo/xoom-turbo/blob/50568b630d92e7dd9b3389496ebf5602d8a84755/src/main/java/io/vlingo/xoom/turbo/codegen/template/TemplateData.java) requires the [filename method](https://github.com/vlingo/xoom-turbo/blob/50568b630d92e7dd9b3389496ebf5602d8a84755/src/main/java/io/vlingo/xoom/turbo/codegen/template/TemplateData.java#L16) implementation, which commonly uses the filename resolution logic in the corresponding [TemplateStandard](https://github.com/vlingo/xoom-turbo/blob/d12cff40e4ceefcd8cee0577103c2de705b55aea/src/main/java/io/vlingo/xoom/turbo/codegen/template/TemplateStandard.java).

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

[RestResourceGenerationStep](https://github.com/vlingo/xoom-turbo/blob/50568b630d92e7dd9b3389496ebf5602d8a84755/src/main/java/io/vlingo/xoom/turbo/codegen/template/resource/RestResourceGenerationStep.java) implements `buildTemplateData` method that passes parameter values, coming from the Web-based UI or properties file, to [RestResourceTemplateData](https://github.com/vlingo/xoom-turbo/blob/master/src/main/java/io/vlingo/xoom/turbo/codegen/template/resource/RestResourceTemplateData.java). In this particular scenario, [RestResourceTemplateDataFactory](https://github.com/vlingo/xoom-turbo/blob/50568b630d92e7dd9b3389496ebf5602d8a84755/src/main/java/io/vlingo/xoom/turbo/codegen/template/resource/RestResourceTemplateDataFactory.java)  is an additional and optional class that helps building [RestResourceTemplateData](https://github.com/vlingo/xoom-turbo/blob/master/src/main/java/io/vlingo/xoom/turbo/codegen/template/resource/RestResourceTemplateData.java). The [shouldProcess method](https://github.com/vlingo/xoom-turbo/blob/50568b630d92e7dd9b3389496ebf5602d8a84755/src/main/java/io/vlingo/xoom/turbo/codegen/CodeGenerationStep.java#L14) is also optional and useful when a [TemplateProcessingStep](https://github.com/vlingo/xoom-turbo/blob/50568b630d92e7dd9b3389496ebf5602d8a84755/src/main/java/io/vlingo/xoom/turbo/codegen/template/TemplateProcessingStep.java) subclass needs to be conditionally skipped.


Finally, [TemplateProcessingStep](https://github.com/vlingo/xoom-turbo/blob/50568b630d92e7dd9b3389496ebf5602d8a84755/src/main/java/io/vlingo/xoom/turbo/codegen/template/TemplateProcessingStep.java) has to be added in the [Configuration](https://github.com/vlingo/xoom-designer/blob/c9b2bf7af16d105509debd71450576975bae342f/src/main/java/io/vlingo/xoom/turbo/designer/Configuration.java#L52) steps list:

<pre>
<code>
    public static final List<CodeGenerationStep> CODE_GENERATION_STEPS = Arrays.asList(
            new ModelGenerationStep(),
            new ProjectionGenerationStep(),
            new StorageGenerationStep(),
            new RestResourceGenerationStep(),
            new BootstrapGenerationStep(),
            new ContentCreationStep()
    );
</code>
</pre>

Eventually, some peripheral points in the code are also involved. The following list is mainly related to a template file creation:

1.  Create an enum value in [Template](https://github.com/vlingo/xoom-turbo/blob/d12cff40e4ceefcd8cee0577103c2de705b55aea/src/main/java/io/vlingo/xoom/turbo/codegen/template/Template.java) passing the template filename (without extension) in the constructor. Example:

<pre>
<code>
    public enum Template {

        //Other template filenames

        <strong>REST_RESOURCE("RestResource")</strong>

        //Enum attributes
    }
</code>
</pre>

2. Map the new standard file to an existing [TemplateStandard](https://github.com/vlingo/xoom-turbo/blob/d12cff40e4ceefcd8cee0577103c2de705b55aea/src/main/java/io/vlingo/xoom/turbo/codegen/template/TemplateStandard.java) or create one. Sometimes there are multiple files for the same standard. For instance, there is one `Aggregate` template file for each `Storage` (Journal, State Store, Object Store). That means [TemplateStandard](https://github.com/vlingo/xoom-turbo/blob/d12cff40e4ceefcd8cee0577103c2de705b55aea/src/main/java/io/vlingo/xoom/turbo/codegen/template/TemplateStandard.java) is responsible for grouping template files by standard and helps the [TemplateProcessor](https://github.com/vlingo/xoom-turbo/blob/50568b630d92e7dd9b3389496ebf5602d8a84755/src/main/java/io/vlingo/xoom/turbo/codegen/template/TemplateProcessor.java) to find the proper file based on [TemplateParameters](https://github.com/vlingo/xoom-turbo/blob/fd5f4a923601a912becd9e10e89a7e8186ed5bf8/src/main/java/io/vlingo/xoom/turbo/codegen/template/TemplateParameters.java) such as [StorageType](https://github.com/vlingo/xoom-turbo/blob/51e02beb23dd83166170089a49cb3eb25a1bac4f/src/main/java/io/vlingo/xoom/turbo/codegen/template/storage/StorageType.java). The example below demonstrates the `Aggregate` and `Rest Resource` standards. The latter has only one related template file:

<pre>
<code>
    public enum TemplateStandard {
        
        <strong>AGGREGATE(parameters -> AGGREGATE_TEMPLATES.get(parameters.from(STORAGE_TYPE))),</strong>
        <strong>REST_RESOURCE(parameters -> CodeTemplateFile.REST_RESOURCE.filename),</strong>

        //Other standards
    }
</code>
</pre>

3. In case it doesn't already exist, create an enum value in [TemplateParameter](https://github.com/vlingo/xoom-turbo/blob/fd5f4a923601a912becd9e10e89a7e8186ed5bf8/src/main/java/io/vlingo/xoom/turbo/codegen/template/TemplateParameter.java) for each template parameter.

In sum, those are the common steps regarding `code template files` on `xoom-designer`. Our team is available to discuss and provide more information on [Gitter](https://gitter.im/vlingo-platform-java/community/).
