package io.vlingo.xoom.starter.template;

public enum Archetype {

    DEFAULT("vlingo-xoom-basic-archetype", "io.vlingo", "1.0");

    private final String artifactId;
    private final String groupId;
    private final String version;

    Archetype(final String artifactId,
              final String groupId,
              final String version) {
        this.artifactId = artifactId;
        this.groupId = groupId;
        this.version = version;
    }

    public String artifactId() {
        return artifactId;
    }

    public String groupId() {
        return groupId;
    }

    public String version() {
        return version;
    }

}
