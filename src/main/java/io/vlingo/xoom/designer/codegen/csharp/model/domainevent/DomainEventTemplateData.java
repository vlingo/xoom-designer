// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.model.domainevent;

import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.CodeGenerationProperties;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.AggregateDetail;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.TemplateParameter;
import io.vlingo.xoom.designer.codegen.csharp.formatting.Formatters;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class DomainEventTemplateData extends TemplateData {

  private final String name;
  private final TemplateParameters parameters;

  public static List<TemplateData> from(final String packageName, final Dialect dialect, final CodeGenerationParameter aggregate) {
    return aggregate.retrieveAllRelated(Label.DOMAIN_EVENT)
        .map(event -> new DomainEventTemplateData(packageName, dialect, event, aggregate))
        .collect(Collectors.toList());
  }

  private DomainEventTemplateData(final String packageName, final Dialect dialect, final CodeGenerationParameter event,
                                  final CodeGenerationParameter aggregate) {
    this.name = event.value;
    this.parameters = TemplateParameters.with(TemplateParameter.PACKAGE_NAME, packageName)
        .and(TemplateParameter.DOMAIN_EVENT_NAME, name)
        .and(TemplateParameter.STATE_NAME, CsharpTemplateStandard.AGGREGATE_STATE.resolveClassname(aggregate.value))
        .and(TemplateParameter.DEFAULT_SCHEMA_VERSION, CodeGenerationProperties.DEFAULT_SCHEMA_VERSION)
        .and(TemplateParameter.CONSTRUCTOR_PARAMETERS, Formatters.Arguments.SIGNATURE_DECLARATION.format(event))
        .and(TemplateParameter.MEMBERS, Formatters.Fields.format(Formatters.Fields.Style.MEMBER_DECLARATION, dialect, event))
        .and(TemplateParameter.MEMBERS_ASSIGNMENT, Formatters.Fields.format(Formatters.Fields.Style.ASSIGNMENT, dialect, event))
        .addImports(resolveImports(aggregate, event));
  }

  private Set<String> resolveImports(final CodeGenerationParameter aggregate, final CodeGenerationParameter event) {
    final Set<String> eventFields = event.retrieveAllRelated(Label.STATE_FIELD)
        .map(field -> field.value)
        .collect(toSet());

    final List<CodeGenerationParameter> stateFields = aggregate.retrieveAllRelated(Label.STATE_FIELD)
            .filter(field -> eventFields.contains(field.value))
            .collect(Collectors.toList());

    return Stream.of(AggregateDetail.resolveImports(stateFields.stream()))
        .flatMap(Set::stream)
        .collect(Collectors.toSet());
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
    return CsharpTemplateStandard.DOMAIN_EVENT;
  }

}
