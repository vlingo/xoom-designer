// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.java;

import io.vlingo.xoom.designer.task.projectgeneration.DeploymentType;

public class DeploymentSettings {

  public final DeploymentType type;
  public final String dockerImage;
  public final String kubernetesPod;
  public final String kubernetesImage;
  public final int producerExchangePort;
  public final boolean useDocker;
  public final boolean useKubernetes;

  public static DeploymentSettings with(final DeploymentType type,
                                        final String dockerImage,
                                        final String kubernetesImage,
                                        final String kubernetesPod,
                                        final int producerExchangePort) {
    return new DeploymentSettings(type, dockerImage, kubernetesImage, kubernetesPod, producerExchangePort);
  }

  private DeploymentSettings(final DeploymentType type,
                             final String dockerImage,
                             final String kubernetesImage,
                             final String kubernetesPod,
                             final int producerExchangePort) {
    this.type = type;
    this.dockerImage = dockerImage;
    this.kubernetesPod = kubernetesPod;
    this.kubernetesImage = kubernetesImage;
    this.useDocker = type.isDocker() || type.isKubernetes();
    this.useKubernetes = type.isKubernetes();
    this.producerExchangePort = producerExchangePort;
  }
}
