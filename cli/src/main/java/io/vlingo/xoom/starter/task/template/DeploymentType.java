package io.vlingo.xoom.starter.task.template;

public enum DeploymentType {

    NONE("NONE"),
    DOCKER("DOCKER"),
    KUBERNETES("K8S");

    private final String key;

    DeploymentType(final String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }

}
