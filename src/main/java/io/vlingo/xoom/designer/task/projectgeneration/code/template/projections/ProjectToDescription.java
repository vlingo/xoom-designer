// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.projections;

import io.vlingo.xoom.turbo.codegen.content.Content;
import io.vlingo.xoom.turbo.codegen.content.ContentQuery;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard.*;
import static java.util.stream.Collectors.toList;

public class ProjectToDescription {

  private static final String FIRST_BECAUSE_OF_PLACEHOLDER = "\"%s name here\"";
  private static final String SECOND_BECAUSE_OF_PLACEHOLDER = "\"Another %s name here\"";
  private static final String PROJECT_TO_DESCRIPTION_BUILD_PATTERN = "ProjectToDescription.with(%s.class, %s)%s";
  private static final String DEFAULT_SOURCE_NAME_INVOCATION = ".class.getName()";
  private static final String ENUM_SOURCE_NAME_INVOCATION = ".name()";

  private final String joinedTypes;
  private final boolean lastParameter;
  private final String projectionClassName;

  public static List<ProjectToDescription> from(final ProjectionType projectionType,
                                                final List<Content> contents) {
    final Set<String> aggregateProtocols =
            ContentQuery.findClassNames(AGGREGATE_PROTOCOL, contents);

    final Iterator<String> iterator = aggregateProtocols.iterator();

    return IntStream.range(0, aggregateProtocols.size()).mapToObj(index -> {
      final String aggregateProtocol = iterator.next();

      final String projectionName =
              PROJECTION.resolveClassname(aggregateProtocol);

      final String becauseOf =
              buildCauseTypesExpression(aggregateProtocol, projectionType, contents);

      return new ProjectToDescription(index, aggregateProtocols.size(), projectionName, becauseOf);
    }).collect(toList());
  }

  private static String buildCauseTypesExpression(final String aggregateProtocol,
                                                  final ProjectionType projectionType,
                                                  final List<Content> contents) {
    final String protocolPackage =
            ContentQuery.findPackage(AGGREGATE_PROTOCOL, aggregateProtocol, contents);

    final Set<String> sourceNames =
            ContentQuery.findClassNames(DOMAIN_EVENT, protocolPackage, contents);

    if (sourceNames.isEmpty()) {
      return String.format(FIRST_BECAUSE_OF_PLACEHOLDER, projectionType.sourceName) + ", " +
              String.format(SECOND_BECAUSE_OF_PLACEHOLDER, projectionType.sourceName);
    }

    return formatSourceNames(projectionType, sourceNames);
  }

  private static String formatSourceNames(final ProjectionType projectionType, final Set<String> sourceNames) {
    final String sourceNameInvocationExpression =
            projectionType.isEventBased() ? DEFAULT_SOURCE_NAME_INVOCATION : ENUM_SOURCE_NAME_INVOCATION;

    return sourceNames.stream().map(s -> s + sourceNameInvocationExpression).collect(Collectors.joining(", "));
  }

  private ProjectToDescription(final int index,
                               final int numberOfProtocols,
                               final String projectionClassName,
                               final String joinedTypes) {
    this.projectionClassName = projectionClassName;
    this.lastParameter = index == numberOfProtocols - 1;
    this.joinedTypes = joinedTypes;
  }

  public String getInitializationCommand() {
    final String separator = lastParameter ? "" : ",";
    return String.format(PROJECT_TO_DESCRIPTION_BUILD_PATTERN, projectionClassName, joinedTypes, separator);
  }

}
