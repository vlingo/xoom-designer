// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.model.aggregate;

import io.vlingo.xoom.codegen.language.Language;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.formatting.Formatters;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard;

import java.util.List;

import static io.vlingo.xoom.designer.task.projectgeneration.code.formatting.Formatters.Arguments.SIGNATURE_DECLARATION;
import static io.vlingo.xoom.designer.task.projectgeneration.code.formatting.Formatters.Fields.Style.SELF_ALTERNATE_REFERENCE;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard.AGGREGATE_STATE;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.AGGREGATE_METHOD;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.METHOD_PARAMETER;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.TemplateParameter.*;
import static java.util.stream.Collectors.toList;

public class AggregateStateMethodTemplateData extends TemplateData {

  private final TemplateParameters parameters;

  public static List<TemplateData> from(final Language language, final CodeGenerationParameter aggregate) {
    return aggregate.retrieveAllRelated(AGGREGATE_METHOD)
            .map(method -> new AggregateStateMethodTemplateData(language, aggregate, method))
            .collect(toList());
  }

  private AggregateStateMethodTemplateData(final Language language,
                                           final CodeGenerationParameter aggregate,
                                           final CodeGenerationParameter method) {
    this.parameters =
            TemplateParameters.with(METHOD_NAME, method.value)
                    .and(METHOD_PARAMETERS, SIGNATURE_DECLARATION.format(method))
                    .and(CONSTRUCTOR_PARAMETERS, resolveConstructorParameters(language, method))
                    .and(STATE_NAME, AGGREGATE_STATE.resolveClassname(aggregate.value));
  }

  private String resolveConstructorParameters(final Language language, final CodeGenerationParameter method) {
    return Formatters.Fields.format(SELF_ALTERNATE_REFERENCE, language, method.parent(), method.retrieveAllRelated(METHOD_PARAMETER));
  }

  @Override
  public TemplateStandard standard() {
    return DesignerTemplateStandard.AGGREGATE_STATE_METHOD;
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

}
