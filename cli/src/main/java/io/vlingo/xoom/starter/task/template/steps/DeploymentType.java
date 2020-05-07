package io.vlingo.xoom.starter.task.template.steps;

public enum DeploymentType {
    NONE,
    DOCKER,
    KUBERNETES;

    public boolean isDocker() {
        return equals(DOCKER);
    }

    public boolean isKubernetes() {
        return equals(KUBERNETES);
    }

}
