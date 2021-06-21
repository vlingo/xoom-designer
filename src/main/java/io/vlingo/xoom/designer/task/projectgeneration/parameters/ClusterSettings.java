// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.parameters;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableList;

public class ClusterSettings {

  public final String oneSeedNode;
  public final String seedNodes;
  public final List<ClusterNode> nodes;
  public final int nodeCount;

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
  }
}
