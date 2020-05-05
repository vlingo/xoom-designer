package io.vlingo.xoom.starter.task.template.steps;

import io.vlingo.xoom.starter.task.template.StorageType;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Aggregate {

    private final static String PACKAGE_PATTERN = "%s.%s.%s";
    private final static String PARENT_PACKAGE_NAME = "model";

    public final String name;
    public final String packageName;
    public final StorageType storageType;
    private final String absolutePath;
    public final List<DomainEvent> events = new ArrayList<>();

    public Aggregate(final String[] dataBlocks,
                     final String basePackage,
                     final StorageType storageType,
                     final String projectPath) {
        this.name = dataBlocks[0].trim();
        this.packageName = resolvePackage(basePackage);
        this.absolutePath = resolveAbsolutePath(basePackage, projectPath);
        this.storageType = storageType;
        this.loadEvents(dataBlocks);
    }

    public String stateName() {
        return name + "State";
    }

    private void loadEvents(final String[] dataBlocks) {
        if(dataBlocks.length > 1) {
            Arrays.asList(dataBlocks).stream().skip(1).forEach(eventName -> {
                this.events.add(new DomainEvent(eventName, packageName, absolutePath));
            });
        }
    }

    private String resolvePackage(final String basePackage) {
        return String.format(PACKAGE_PATTERN, basePackage, PARENT_PACKAGE_NAME, name).toLowerCase();
    }

    private String resolveAbsolutePath(final String basePackage, final String projectPath) {
        final String basePackagePath = basePackage.replaceAll("\\.", "\\\\");
        return Paths.get(projectPath, "src", "main", "java", basePackagePath,
                PARENT_PACKAGE_NAME, name.toLowerCase()).toString();
    }

    public boolean dependsOnPlaceholderEvent() {
        return this.storageType.equals(StorageType.STATE_STORE);
    }

    public File protocolFile() {
        return retrieveFile(name);
    }

    public File aggregateFile() {
        return retrieveFile(name + "Entity");
    }

    public File stateFile() {
        return retrieveFile(name + "State");
    }

    public File placeholderEventFile() {
        return retrieveFile(placeholderEventName());
    }

    private File retrieveFile(final String fileName) {
        return new File(Paths.get(absolutePath, fileName + ".java").toString());
    }

    public String placeholderEventName() {
        return name + "PlaceholderDefinedEvent";
    }

}
