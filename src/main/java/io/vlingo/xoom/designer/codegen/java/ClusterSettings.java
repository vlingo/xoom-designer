// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java;

public class ClusterSettings {

  public final int startPortRange;
  public final int nodeCount;
  public final int quorum;
  public final int seed;

  public static ClusterSettings with(final int startPortRange,
                                     final int clusterTotalNodes) {
    return new ClusterSettings(startPortRange, clusterTotalNodes);
  }

  private ClusterSettings(final int startPortRange,
                          final int clusterTotalNodes) {
    this.startPortRange = startPortRange;
    this.nodeCount = clusterTotalNodes;
    this.quorum = nodeCount / 2 + 1;
    this.seed = (int)Math.round (((double) nodeCount) / 3.0d + 0.5d);
  }
}
