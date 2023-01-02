// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.restapi.data;

import io.vlingo.xoom.designer.codegen.DeploymentType;

import java.util.List;

public class DeploymentSettingsData {

  public final String type;
  public final String dockerImage;
  public final String kubernetesImage;
  public final String kubernetesPod;
  public final int clusterTotalNodes;
  public final int clusterPort;
  public final int producerExchangePort;
  public final int httpServerPort;
  public final boolean pullSchemas;

  public DeploymentSettingsData(final String type,
                                final String dockerImage,
                                final String kubernetesImage,
                                final String kubernetesPod,
                                final int clusterTotalNodes,
                                final int clusterPort,
                                final int producerExchangePort,
                                final int httpServerPort,
                                final boolean pullSchemas) {
    this.type = type;
    this.dockerImage = dockerImage;
    this.kubernetesImage = kubernetesImage;
    this.kubernetesPod = kubernetesPod;
    this.clusterTotalNodes = clusterTotalNodes;
    this.clusterPort = clusterPort;
    this.producerExchangePort = producerExchangePort;
    this.httpServerPort = httpServerPort;
    this.pullSchemas = pullSchemas;
  }

  public List<String> validate(List<String> errorStrings) {
    if (type == null) errorStrings.add("DeploymentSettingsData.type is null");
    if (type.equals(DeploymentType.DOCKER.name()) && dockerImage == null)
      errorStrings.add("DeploymentSettingsData.dockerImage is null");
    if (type.equals(DeploymentType.KUBERNETES.name())) {
      if (kubernetesImage == null) errorStrings.add("DeploymentSettingsData.kubernetesImage is null");
      if (kubernetesPod == null) errorStrings.add("DeploymentSettingsData.kubernetesPod is null");
    }
    return errorStrings;
  }
}
