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
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.AggregateDetail;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.TemplateParameter;
import io.vlingo.xoom.designer.codegen.csharp.formatting.Formatters;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class AggregateMethodTemplateData extends TemplateData {

  private final TemplateParameters parameters;

  public static List<TemplateData> from(final TemplateParameters parentParameters, final CodeGenerationParameter aggregate) {
    return aggregate.retrieveAllRelated(Label.AGGREGATE_METHOD)
        .map(method -> new AggregateMethodTemplateData(parentParameters, method))
        .collect(toList());
  }

  private AggregateMethodTemplateData(final TemplateParameters parentParameters, final CodeGenerationParameter method) {

    this.parameters = TemplateParameters.with(TemplateParameter.METHOD_NAME, method.value)
        .and(TemplateParameter.DOMAIN_EVENT_NAME, method.retrieveRelatedValue(Label.DOMAIN_EVENT))
        .and(TemplateParameter.METHOD_INVOCATION_PARAMETERS, Formatters.Arguments.AGGREGATE_METHOD_INVOCATION.format(method))
        .and(TemplateParameter.METHOD_PARAMETERS, Formatters.Arguments.SIGNATURE_DECLARATION.format(method))
        .and(TemplateParameter.STATE_NAME, CsharpTemplateStandard.AGGREGATE_STATE.resolveClassname(method.parent(Label.AGGREGATE).value));

    parentParameters.addImports(resolveImports(method));
  }

  private Set<String> resolveImports(final CodeGenerationParameter method) {
    final Stream<CodeGenerationParameter> involvedStateFields = AggregateDetail.findInvolvedStateFields(method.parent(), method.value);

    return AggregateDetail.resolveImports(involvedStateFields);
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return CsharpTemplateStandard.AGGREGATE_METHOD;
  }

}
