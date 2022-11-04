// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.storage;

import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.content.ContentQuery;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;

import java.util.List;
import java.util.Set;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Projection {

  private final String actor;
  private final String causes;
  private final Boolean last;

  public static List<Projection> from(final List<Content> contents) {
    final List<Content> protocols =
            ContentQuery.filterByStandard(CsharpTemplateStandard.AGGREGATE_PROTOCOL, contents)
                    .collect(Collectors.toList());

    final IntFunction<Projection> mapper =
            index -> new Projection(index, protocols.get(index),
                    contents, protocols.size());

    return IntStream.range(0, protocols.size()).mapToObj(mapper).collect(Collectors.toList());
  }

  private Projection(final int index,
                     final Content protocol,
                     final List<Content> contents,
                     final int numberOfProjections) {
    this.causes = joinEvents(protocol, contents);
    this.last = index == numberOfProjections - 1;
    this.actor = CsharpTemplateStandard.PROJECTION.resolveClassname(protocol.retrieveName());
  }

  private String joinEvents(final Content protocol,
                            final List<Content> contents) {
    final Set<String> eventNames =
            ContentQuery.findClassNames(CsharpTemplateStandard.DOMAIN_EVENT,
                    protocol.retrievePackage(), contents);

    return eventNames.stream()
            .map(name -> name + ".class")
            .collect(Collectors.joining(", "));
  }

  public String getActor() {
    return actor;
  }

  public String getCauses() {
    return causes;
  }

  public boolean isLast() {
    return last;
  }

}
