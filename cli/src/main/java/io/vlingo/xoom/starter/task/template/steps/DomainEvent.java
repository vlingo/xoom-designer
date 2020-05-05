package io.vlingo.xoom.starter.task.template.steps;

import java.io.File;
import java.nio.file.Paths;

public class DomainEvent {

    public final String name;
    public final String packageName;
    public final String absolutePath;

    public DomainEvent(final String name, final String packageName, final String absolutePath) {
        this.name = name.trim();
        this.packageName = packageName;
        this.absolutePath = absolutePath;
    }

    public File file() {
        return new File(Paths.get(absolutePath, name + ".java").toString());
    }

}
