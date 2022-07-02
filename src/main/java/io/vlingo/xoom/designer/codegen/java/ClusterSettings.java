// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableList;

public class ClusterSettings {

  public final String oneSeedNode;
  public final String seedNodes;
  public final List<ClusterNode> nodes;
  public final int nodeCount;
  public final int quorum;
  public final int seed;

  public static ClusterSettings with(final int startPortRange,
                                     final int clusterTotalNodes) {
    return new ClusterSettings(startPortRange, clusterTotalNodes);
  }

  private ClusterSettings(final int startPortRange,
                          final int clusterTotalNodes) {
    this.nodes = unmodifiableList(ClusterNode.from(startPortRange, clusterTotalNodes));
    this.nodeCount = nodes.size();
    this.oneSeedNode = nodes.get(0).name;
    this.seedNodes = nodes.stream().map(node -> node.name).collect(Collectors.joining(","));
    this.quorum = nodeCount / 2 + 1;
    this.seed = (int) (((double) nodeCount) / 3.0d + 0.5d);
  }
}
