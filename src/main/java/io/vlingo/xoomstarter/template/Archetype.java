package io.vlingo.xoomstarter.template;

public enum Archetype {

    DEFAULT("maven-archetype-quickstart", "org.apache.maven.archetypes", "1.1");

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
