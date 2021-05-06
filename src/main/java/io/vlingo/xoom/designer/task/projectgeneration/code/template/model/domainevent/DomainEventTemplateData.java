// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.model.domainevent;

import io.vlingo.xoom.designer.task.projectgeneration.code.CodeGenerationSetup;
import io.vlingo.xoom.designer.task.projectgeneration.code.formatting.Formatters;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.model.aggregate.AggregateDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.model.valueobject.ValueObjectDetail;
import io.vlingo.xoom.turbo.codegen.content.Content;
import io.vlingo.xoom.turbo.codegen.language.Language;
import io.vlingo.xoom.turbo.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.turbo.codegen.template.TemplateData;
import io.vlingo.xoom.turbo.codegen.template.TemplateParameters;
import io.vlingo.xoom.turbo.codegen.template.TemplateStandard;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.task.projectgeneration.code.formatting.Formatters.Fields.Style.MEMBER_DECLARATION;
import static io.vlingo.xoom.designer.task.projectgeneration.code.formatting.Formatters.Fields.Style.STATE_BASED_ASSIGNMENT;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard.AGGREGATE_STATE;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.STATE_FIELD;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.TemplateParameter.*;
import static java.util.stream.Collectors.toSet;

public class DomainEventTemplateData extends TemplateData {

  private final String name;
  private final TemplateParameters parameters;

  public static List<TemplateData> from(final String packageName,
                                        final Language language,
                                        final CodeGenerationParameter aggregate,
                                        final List<Content> contents) {
    return aggregate.retrieveAllRelated(Label.DOMAIN_EVENT).map(event ->
            new DomainEventTemplateData(packageName, language, event, aggregate, contents))
            .collect(Collectors.toList());
  }

  private DomainEventTemplateData(final String packageName,
                                  final Language language,
                                  final CodeGenerationParameter event,
                                  final CodeGenerationParameter aggregate,
                                  final List<Content> contents) {
    final List<CodeGenerationParameter> involvedStateFields =
            DomainEventDetail.findInvolvedStateFields(event).collect(Collectors.toList());

    this.name = event.value;
    this.parameters =
            TemplateParameters.with(PACKAGE_NAME, packageName).and(DOMAIN_EVENT_NAME, name)
                    .and(STATE_NAME, AGGREGATE_STATE.resolveClassname(aggregate.value))
                    .and(DEFAULT_SCHEMA_VERSION, CodeGenerationSetup.DEFAULT_SCHEMA_VERSION)
                    .and(MEMBERS, Formatters.Fields.format(MEMBER_DECLARATION, language, aggregate, involvedStateFields.stream()))
                    .and(MEMBERS_ASSIGNMENT, Formatters.Fields.format(STATE_BASED_ASSIGNMENT, language, aggregate, involvedStateFields.stream()))
                    .addImports(resolveImports(aggregate, event, contents));
  }

  private Set<String> resolveImports(final CodeGenerationParameter aggregate,
                                     final CodeGenerationParameter event,
                                     final List<Content> contents) {
    final Set<String> eventFields =
            event.retrieveAllRelated(STATE_FIELD).map(field -> field.value).collect(toSet());

    final List<CodeGenerationParameter> stateFields =
            aggregate.retrieveAllRelated(STATE_FIELD)
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
    return DesignerTemplateStandard.DOMAIN_EVENT;
  }

}
