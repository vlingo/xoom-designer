package io.vlingo.xoom.starter.template.steps;

public enum PropertiesKeys {

    VERSION("version"),
    GROUP_ID("group.id"),
    ARTIFACT_ID("artifact.id"),
    PACKAGE("package"),
    DEPLOYMENT("deployment"),
    TARGET_FOLDER("target.folder");

    private final String literal;

    PropertiesKeys(final String literal) {
        this.literal = literal;
    }

    public String literal() {
        return literal;
    }

}
