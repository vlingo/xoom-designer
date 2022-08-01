// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.model.aggregate;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.AggregateDetail;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.FieldDetail;
import io.vlingo.xoom.designer.codegen.csharp.TemplateParameter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AggregateTemplateData extends TemplateData {

  private final String protocolName;
  private final TemplateParameters parameters;

  @SuppressWarnings("unchecked")
  public AggregateTemplateData(final String packageName, final CodeGenerationParameter aggregate, final Boolean useCQRS) {
    this.protocolName = aggregate.value;
    this.parameters = TemplateParameters.with(TemplateParameter.PACKAGE_NAME, packageName)
        .and(TemplateParameter.AGGREGATE_PROTOCOL_NAME, protocolName)
        .and(TemplateParameter.STATE_NAME, CsharpTemplateStandard.AGGREGATE_STATE.resolveClassname(protocolName))
        .and(TemplateParameter.ENTITY_NAME, CsharpTemplateStandard.AGGREGATE.resolveClassname(protocolName))
        .and(TemplateParameter.ID_TYPE, FieldDetail.typeOf(aggregate, AggregateDetail.findIdField(aggregate).value))
        .addImports(resolveImports(aggregate))
        .and(TemplateParameter.METHODS, new ArrayList<String>())
        .and(TemplateParameter.USE_CQRS, useCQRS);

    this.dependOn(AggregateMethodTemplateData.from(parameters, aggregate));
  }

  private Set<String>  resolveImports(final CodeGenerationParameter aggregate) {
    final Set<String> imports = new HashSet<>();

    if (aggregate.hasAny(Label.AGGREGATE_METHOD)) {
      imports.add(Completes.class.getCanonicalName());
    }

    return imports;
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
    return CsharpTemplateStandard.AGGREGATE;
  }

}
