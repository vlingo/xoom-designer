// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java.autodispatch;

import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.content.ContentQuery;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.resource.RouteDetail;

import java.util.*;
import java.util.stream.Collectors;

import static io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard.QUERIES;
import static io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard.*;
import static io.vlingo.xoom.designer.task.projectgeneration.code.java.TemplateParameter.*;

public class AutoDispatchMappingTemplateData extends TemplateData {

  private final String aggregateName;
  private final TemplateParameters parameters;

  protected AutoDispatchMappingTemplateData(final String basePackage,
                                            final CodeGenerationParameter aggregate,
                                            final Boolean useCQRS,
                                            final List<Content> contents) {

    this.aggregateName = aggregate.value;
    this.parameters =
            TemplateParameters.with(PACKAGE_NAME, resolvePackage(basePackage))
                    .and(AGGREGATE_PROTOCOL_NAME, aggregateName)
                    .and(ENTITY_NAME, AGGREGATE.resolveClassname(aggregateName))
                    .and(STATE_DATA_OBJECT_NAME, DATA_OBJECT.resolveClassname(aggregateName))
                    .and(QUERIES_NAME, QUERIES.resolveClassname(aggregateName))
                    .and(QUERIES_ACTOR_NAME, QUERIES_ACTOR.resolveClassname(aggregateName))
                    .and(AUTO_DISPATCH_MAPPING_NAME, AUTO_DISPATCH_MAPPING.resolveClassname(aggregateName))
                    .and(AUTO_DISPATCH_HANDLERS_MAPPING_NAME, AUTO_DISPATCH_HANDLERS_MAPPING.resolveClassname(aggregateName))
                    .and(URI_ROOT, aggregate.retrieveRelatedValue(Label.URI_ROOT))
                    .addImports(resolveImports(aggregateName, contents))
                    .and(ROUTE_DECLARATIONS, new ArrayList<String>())
                    .and(USE_CQRS, useCQRS);

    this.loadDependencies(aggregate, useCQRS);
  }

  @SuppressWarnings("unchecked")
  private void loadDependencies(final CodeGenerationParameter aggregate, final boolean useCQRS) {
    if (useCQRS) {
      aggregate.relate(RouteDetail.defaultQueryRoutes(aggregate));
    }
    this.dependOn(AutoDispatchRouteTemplateData.from(aggregate.retrieveAllRelated(Label.ROUTE_SIGNATURE)));
  }

  @Override
  public void handleDependencyOutcome(final TemplateStandard standard, final String outcome) {
    this.parameters.<List<String>>find(ROUTE_DECLARATIONS).add(outcome);
  }

  private Set<String> resolveImports(final String aggregateName,
                                     final List<Content> contents) {
    final Map<TemplateStandard, String> classes =
            mapClassesWithTemplateStandards(aggregateName);

    return classes.entrySet().stream().map(entry -> {
      try {
        final String className = entry.getValue();
        final TemplateStandard standard = entry.getKey();
        return ContentQuery.findFullyQualifiedClassName(standard, className, contents);
      } catch (final IllegalArgumentException exception) {
        return null;
      }
    }).collect(Collectors.toSet());
  }

  @SuppressWarnings("serial")
  private Map<TemplateStandard, String> mapClassesWithTemplateStandards(final String aggregateName) {
    return new HashMap<TemplateStandard, String>() {{
      put(AGGREGATE, AGGREGATE.resolveClassname(aggregateName));
      put(AGGREGATE_PROTOCOL, aggregateName);
      put(QUERIES, QUERIES.resolveClassname(aggregateName));
      put(QUERIES_ACTOR, QUERIES_ACTOR.resolveClassname(aggregateName));
      put(DATA_OBJECT, DATA_OBJECT.resolveClassname(aggregateName));
    }};
  }

  private String resolvePackage(final String basePackage) {
    return String.format("%s.%s.%s", basePackage, "infrastructure", "resource").toLowerCase();
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return AUTO_DISPATCH_MAPPING;
  }

  @Override
  public String filename() {
    return standard().resolveFilename(aggregateName, parameters);
  }

}
