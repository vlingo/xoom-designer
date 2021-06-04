// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java.model.domainevent;

import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.CodeGenerationProperties;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.formatting.Formatters;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.aggregate.AggregateDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.valueobject.ValueObjectDetail;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.task.projectgeneration.code.java.TemplateParameter.*;
import static io.vlingo.xoom.designer.task.projectgeneration.code.java.formatting.Formatters.Fields.Style.ASSIGNMENT;
import static io.vlingo.xoom.designer.task.projectgeneration.code.java.formatting.Formatters.Fields.Style.MEMBER_DECLARATION;
import static java.util.stream.Collectors.toSet;

public class DomainEventTemplateData extends TemplateData {

  private final String name;
  private final TemplateParameters parameters;

  public static List<TemplateData> from(final String packageName,
                                        final Dialect dialect,
                                        final CodeGenerationParameter aggregate,
                                        final List<Content> contents) {
    return aggregate.retrieveAllRelated(Label.DOMAIN_EVENT).map(event ->
            new DomainEventTemplateData(packageName, dialect, event, aggregate, contents))
            .collect(Collectors.toList());
  }

  private DomainEventTemplateData(final String packageName,
                                  final Dialect dialect,
                                  final CodeGenerationParameter event,
                                  final CodeGenerationParameter aggregate,
                                  final List<Content> contents) {
    this.name = event.value;
    this.parameters =
            TemplateParameters.with(PACKAGE_NAME, packageName).and(DOMAIN_EVENT_NAME, name)
                    .and(STATE_NAME, JavaTemplateStandard.AGGREGATE_STATE.resolveClassname(aggregate.value))
                    .and(DEFAULT_SCHEMA_VERSION, CodeGenerationProperties.DEFAULT_SCHEMA_VERSION)
                    .and(CONSTRUCTOR_PARAMETERS, Formatters.Arguments.SIGNATURE_DECLARATION.format(event))
                    .and(MEMBERS, Formatters.Fields.format(MEMBER_DECLARATION, dialect, event))
                    .and(MEMBERS_ASSIGNMENT, Formatters.Fields.format(ASSIGNMENT, dialect, event))
                    .addImports(resolveImports(aggregate, event, contents));
  }

  private Set<String> resolveImports(final CodeGenerationParameter aggregate,
                                     final CodeGenerationParameter event,
                                     final List<Content> contents) {
    final Set<String> eventFields =
            event.retrieveAllRelated(Label.STATE_FIELD).map(field -> field.value).collect(toSet());

    final List<CodeGenerationParameter> stateFields =
            aggregate.retrieveAllRelated(Label.STATE_FIELD)
                    .filter(field -> eventFields.contains(field.value))
                    .collect(Collectors.toList());

    return Stream.of(ValueObjectDetail.resolveImports(contents, stateFields.stream()),
            AggregateDetail.resolveImports(stateFields.stream()))
            .flatMap(Set::stream).collect(Collectors.toSet());
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public String filename() {
    return standard().resolveFilename(name, parameters);
  }

  @Override
  public TemplateStandard standard() {
    return JavaTemplateStandard.DOMAIN_EVENT;
  }

}
