// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.restapi.data;

import io.vlingo.xoom.starter.task.Property;
import io.vlingo.xoom.starter.task.projectgeneration.steps.DeploymentType;

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

    public List<String> validate(List<String> errorStrings) {
        if(clusterNodes==null) errorStrings.add("clusterNodes is null");
        if(type==null) errorStrings.add("type is null");
        if(type.equals(DeploymentType.DOCKER.name()) && dockerImage==null) errorStrings.add("dockerImage is null");
        if(type.equals(DeploymentType.KUBERNETES.name())) {
            if(kubernetesImage==null) errorStrings.add("kubernetesImage is null");
            if(kubernetesPod==null) errorStrings.add("kubernetesPod is null");
        }
        return errorStrings;
    }
}
