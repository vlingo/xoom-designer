// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java.storage;

import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.content.ContentQuery;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.TemplateParameter;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.util.*;
import java.util.stream.Collectors;

public class Queries {

  private static final String QUALIFIED_NAME_PATTERN = "%s.%s";

  private final String protocolName;
  private final String actorName;
  private final String attributeName;
  private final Set<String> qualifiedNames = new HashSet<>();

  public static List<Queries> from(final Boolean useCQRS,
                                   final List<Content> contents,
                                   final List<TemplateData> templatesData) {
    if (!useCQRS) {
      return Collections.emptyList();
    }

    return from(Model.QUERY, contents, templatesData);
  }

  public static List<Queries> from(final Model model,
                                   final List<Content> contents,
                                   final List<TemplateData> templatesData) {
    if (!model.isQueryModel()) {
      return Collections.emptyList();
    }

    if (ContentQuery.exists(JavaTemplateStandard.QUERIES_ACTOR, contents)) {
      return ContentQuery.filterByStandard(JavaTemplateStandard.QUERIES_ACTOR, contents)
              .filter(Content::isProtocolBased)
              .map(content -> new Queries(content.retrieveProtocolQualifiedName(),
                      content.retrieveQualifiedName()))
              .collect(Collectors.toList());
    }

    return templatesData.stream().filter(data -> data.hasStandard(JavaTemplateStandard.QUERIES_ACTOR))
            .map(data -> new Queries(data.parameters())).collect(Collectors.toList());
  }

  public static Queries from(final CodeGenerationParameter autoDispatchParameter) {
    if (!autoDispatchParameter.hasAny(Label.QUERIES_PROTOCOL)) {
      return Queries.empty();
    }
    return new Queries(autoDispatchParameter.retrieveRelatedValue(Label.QUERIES_PROTOCOL),
            autoDispatchParameter.retrieveRelatedValue(Label.QUERIES_ACTOR));
  }

  public static Queries from(final CodeGenerationParameter aggregateParameter,
                                                                           final List<Content> contents,
                                                                           final Boolean useCQRS) {
    if (!useCQRS) {
      return Queries.empty();
    }

    final String queriesProtocol = JavaTemplateStandard.QUERIES.resolveClassname(aggregateParameter.value);
    final String queriesActor = JavaTemplateStandard.QUERIES_ACTOR.resolveClassname(aggregateParameter.value);
    return new Queries(ContentQuery.findFullyQualifiedClassName(JavaTemplateStandard.QUERIES, queriesProtocol, contents),
            ContentQuery.findFullyQualifiedClassName(JavaTemplateStandard.QUERIES_ACTOR, queriesActor, contents));
  }

  private static Queries empty() {
    return new Queries("", "", "", "");
  }

  private Queries(final TemplateParameters parameters) {
    this(parameters.find(TemplateParameter.PACKAGE_NAME), parameters.find(TemplateParameter.QUERIES_NAME),
            parameters.find(TemplateParameter.PACKAGE_NAME), parameters.find(TemplateParameter.QUERIES_ACTOR_NAME));
  }

  private Queries(final String protocolQualifiedName,
                  final String actorQualifiedName) {
    this(codeElementFormatter().packageOf(protocolQualifiedName), codeElementFormatter().simpleNameOf(protocolQualifiedName),
            codeElementFormatter().packageOf(actorQualifiedName), codeElementFormatter().simpleNameOf(actorQualifiedName));
  }

  private Queries(final String protocolPackageName,
                  final String protocolName,
                  final String actorPackageName,
                  final String actorName) {
    this.actorName = actorName;
    this.protocolName = protocolName;
    this.attributeName = codeElementFormatter().simpleNameToAttribute(protocolName);

    if (!isEmpty()) {
      this.qualifiedNames.addAll(
              Arrays.asList(String.format(QUALIFIED_NAME_PATTERN, protocolPackageName, protocolName),
                      String.format(QUALIFIED_NAME_PATTERN, actorPackageName, actorName)));
    }
  }

  public String getProtocolName() {
    return protocolName;
  }

  public String getActorName() {
    return actorName;
  }

  public String getAttributeName() {
    return attributeName;
  }

  public Set<String> getQualifiedNames() {
    return qualifiedNames;
  }

  public boolean isEmpty() {
    return protocolName.isEmpty() && actorName.isEmpty() && attributeName.isEmpty();
  }

  private static CodeElementFormatter codeElementFormatter() {
    return ComponentRegistry.withName("defaultCodeFormatter");
  }
}
