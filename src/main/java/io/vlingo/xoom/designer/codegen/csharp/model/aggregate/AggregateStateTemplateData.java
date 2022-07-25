// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.csharp.model.aggregate;

import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.FieldDetail;
import io.vlingo.xoom.designer.codegen.csharp.TemplateParameter;
import io.vlingo.xoom.designer.codegen.csharp.formatting.Formatters;
import io.vlingo.xoom.designer.codegen.java.model.aggregate.AggregateDetail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class AggregateStateTemplateData extends TemplateData {

  private final String protocolName;
  private final TemplateParameters parameters;

  @SuppressWarnings("unchecked")
  public AggregateStateTemplateData(final String packageName, final Dialect dialect,
                                    final CodeGenerationParameter aggregate, final List<Content> contents) {
    this.protocolName = aggregate.value;
    this.parameters = TemplateParameters.with(TemplateParameter.PACKAGE_NAME, packageName)
        .and(TemplateParameter.MEMBERS, Formatters.Fields.format(Formatters.Fields.Style.MEMBER_DECLARATION, dialect, aggregate))
        .and(TemplateParameter.MEMBERS_ASSIGNMENT, Formatters.Fields.format(Formatters.Fields.Style.ASSIGNMENT, dialect, aggregate))
        .and(TemplateParameter.ID_TYPE, FieldDetail.typeOf(aggregate, AggregateDetail.findIdField(aggregate).value))
        .addImports(resolveImports(contents, aggregate))
        .and(TemplateParameter.STATE_NAME, CsharpTemplateStandard.AGGREGATE_STATE.resolveClassname(protocolName))
        .and(TemplateParameter.MEMBER_NAMES, AggregateDetail.resolveStateFieldsNames(aggregate))
        .and(TemplateParameter.CONSTRUCTOR_PARAMETERS, Formatters.Arguments.SIGNATURE_DECLARATION.format(aggregate))
        .and(TemplateParameter.METHOD_INVOCATION_PARAMETERS, resolveIdBasedConstructorParameters(dialect, aggregate))
        .and(TemplateParameter.METHODS, new ArrayList<String>());

    this.dependOn(AggregateStateMethodTemplateData.from(dialect, aggregate));
  }

  private Set<String> resolveImports(final List<Content> contents, final CodeGenerationParameter aggregate) {
    return new HashSet<>();
  }

  private String resolveIdBasedConstructorParameters(final Dialect dialect, final CodeGenerationParameter aggregate) {
    final CodeGenerationParameter idField = AggregateDetail.findIdField(aggregate);
    return Formatters.Fields.format(Formatters.Fields.Style.ALTERNATE_REFERENCE_WITH_DEFAULT_VALUE, dialect, aggregate, Stream.of(idField));
  }

  @Override
  public void handleDependencyOutcome(final TemplateStandard standard, final String outcome) {
    this.parameters.<List<String>>find(TemplateParameter.METHODS).add(outcome);
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
    return CsharpTemplateStandard.AGGREGATE_STATE;
  }

}
