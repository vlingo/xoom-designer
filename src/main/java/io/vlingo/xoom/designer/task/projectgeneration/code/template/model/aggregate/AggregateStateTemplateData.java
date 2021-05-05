// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.template.model.aggregate;

import io.vlingo.xoom.designer.task.projectgeneration.code.formatting.Formatters;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.model.FieldDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.model.valueobject.ValueObjectDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.storage.StorageType;
import io.vlingo.xoom.turbo.codegen.content.Content;
import io.vlingo.xoom.turbo.codegen.language.Language;
import io.vlingo.xoom.turbo.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.turbo.codegen.template.TemplateData;
import io.vlingo.xoom.turbo.codegen.template.TemplateParameters;
import io.vlingo.xoom.turbo.codegen.template.TemplateStandard;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.task.projectgeneration.code.formatting.Formatters.Arguments.SIGNATURE_DECLARATION;
import static io.vlingo.xoom.designer.task.projectgeneration.code.formatting.Formatters.Fields.Style.*;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard.AGGREGATE_STATE;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.STATE_FIELD;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.TemplateParameter.*;

public class AggregateStateTemplateData extends TemplateData {

  private final String protocolName;
  private final TemplateParameters parameters;

  @SuppressWarnings("unchecked")
  public AggregateStateTemplateData(final String packageName,
                                    final Language language,
                                    final CodeGenerationParameter aggregate,
                                    final StorageType storageType,
                                    final List<Content> contents) {
    this.protocolName = aggregate.value;

    this.parameters =
            TemplateParameters.with(PACKAGE_NAME, packageName)
                    .and(EVENT_SOURCED, storageType.isSourced())
                    .and(MEMBERS, Formatters.Fields.format(MEMBER_DECLARATION, language, aggregate))
                    .and(MEMBERS_ASSIGNMENT, Formatters.Fields.format(ASSIGNMENT, language, aggregate))
                    .and(ID_TYPE, FieldDetail.typeOf(aggregate, "id"))
                    .addImports(resolveImports(contents, aggregate))
                    .and(STATE_NAME, AGGREGATE_STATE.resolveClassname(protocolName))
                    .and(CONSTRUCTOR_PARAMETERS, SIGNATURE_DECLARATION.format(aggregate))
                    .and(METHOD_INVOCATION_PARAMETERS, resolveIdBasedConstructorParameters(language, aggregate))
                    .and(METHODS, new ArrayList<String>());

    this.dependOn(AggregateStateMethodTemplateData.from(language, aggregate));
  }

  private Set<String> resolveImports(final List<Content> contents, final CodeGenerationParameter aggregate) {
    return ValueObjectDetail.resolveImports(contents, aggregate.retrieveAllRelated(STATE_FIELD));
  }

  private String resolveIdBasedConstructorParameters(final Language language, final CodeGenerationParameter aggregate) {
    final CodeGenerationParameter idField = CodeGenerationParameter.of(STATE_FIELD, "id");
    return Formatters.Fields.format(ALTERNATE_REFERENCE_WITH_DEFAULT_VALUE, language, aggregate, Stream.of(idField));
  }

  @Override
  public void handleDependencyOutcome(final TemplateStandard standard, final String outcome) {
    this.parameters.<List<String>>find(METHODS).add(outcome);
  }

  @Override
  public String filename() {
    return standard().resolveFilename(protocolName, parameters);
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return AGGREGATE_STATE;
  }

}
