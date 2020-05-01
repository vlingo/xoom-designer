package io.vlingo.xoom.starter.restapi.data;

import io.vlingo.xoom.starter.task.Property;

import java.util.Arrays;
import java.util.List;

public class DeploymentSettingsData {

    public final Integer clusterNodes;
    public final String type;
    public final String dockerImage;
    public final String kubernetesImage;
    public final String kubernetesPod;

    public DeploymentSettingsData(
            final Integer clusterNodes,
            final String type,
            final String dockerImage,
            final String kubernetesImage,
            final String kubernetesPod) {
        this.clusterNodes = clusterNodes;
        this.type = type;
        this.dockerImage = dockerImage;
        this.kubernetesImage = kubernetesImage;
        this.kubernetesPod = kubernetesPod;
    }

    List<String> toArguments() {
        return Arrays.asList(
            Property.CLUSTER_NODES.literal(), clusterNodes.toString(),
            Property.DEPLOYMENT.literal(), type,
            Property.DOCKER_IMAGE.literal(), dockerImage,
            Property.KUBERNETES_IMAGE.literal(), kubernetesImage,
            Property.KUBERNETES_POD_NAME.literal(), kubernetesPod
        );
    }

}
