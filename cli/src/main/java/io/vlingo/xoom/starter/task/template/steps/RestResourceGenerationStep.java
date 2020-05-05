package io.vlingo.xoom.starter.task.template.steps;

import io.vlingo.xoom.starter.codegen.RestResourceCodeGenerator;
import io.vlingo.xoom.starter.task.Property;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.steps.TaskExecutionStep;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;

public class RestResourceGenerationStep implements TaskExecutionStep {

    private final static String PACKAGE_PATTERN = "%s.%s";
    private final static String PARENT_PACKAGE_NAME = "resource";
    private static final String REST_RESOURCES_SEPARATOR = ";";

    @Override
    public void process(final TaskExecutionContext context) {
        final String projectPath = context.projectPath();
        final String restResourcesData = context.propertyOf(Property.REST_RESOURCES);
        final String packageName = resolvePackage(context.propertyOf(Property.PACKAGE));
        final String absolutePath = resolveAbsolutePath(packageName, projectPath);

        Arrays.asList(restResourcesData.split(REST_RESOURCES_SEPARATOR))
                .stream().map(restResourceData -> restResourceData.trim())
                .forEach(restResourceData -> {
                    final String className = restResourceData + "Resource.java";
                    final String fullPath = Paths.get(absolutePath, className).toString();
                    final String restResourceCode =
                            RestResourceCodeGenerator.instance().generate(restResourceData,
                                    packageName);

                    context.addOutputResource(new File(fullPath), restResourceCode);
                });
    }

    private String resolvePackage(final String basePackage) {
        return String.format(PACKAGE_PATTERN, basePackage, PARENT_PACKAGE_NAME).toLowerCase();
    }

    private String resolveAbsolutePath(final String basePackage, final String projectPath) {
        final String basePackagePath = basePackage.replaceAll("\\.", "\\\\");
        return Paths.get(projectPath, "src", "main", "java", basePackagePath).toString();
    }

}
