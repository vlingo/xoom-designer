// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.model.aggregate;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.model.MethodScope;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.codegen.content.CodeElementFormatter.simpleNameToAttribute;
import static io.vlingo.xoom.designer.task.projectgeneration.code.formatting.Formatters.Arguments.AGGREGATE_METHOD_INVOCATION;
import static io.vlingo.xoom.designer.task.projectgeneration.code.formatting.Formatters.Arguments.SIGNATURE_DECLARATION;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard.AGGREGATE;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard.AGGREGATE_STATE;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.AGGREGATE_METHOD;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.TemplateParameter.*;
import static java.util.stream.Collectors.toList;

public class AggregateProtocolMethodTemplateData extends TemplateData {

  private final TemplateParameters parameters;

  public static List<TemplateData> from(final TemplateParameters parentParameters,
                                        final CodeGenerationParameter aggregateParameter) {
    final Function<CodeGenerationParameter, List<TemplateData>> mapper =
            method -> fromInferredScopes(parentParameters, method);

    return aggregateParameter.retrieveAllRelated(AGGREGATE_METHOD).map(mapper)
            .flatMap(List::stream).collect(toList());
  }

  private static List<TemplateData> fromInferredScopes(final TemplateParameters parentParameters,
                                                       final CodeGenerationParameter methodParameter) {
    final Function<MethodScope, TemplateData> mapper =
            scope -> new AggregateProtocolMethodTemplateData(scope, parentParameters, methodParameter);

    return MethodScope.infer(methodParameter).map(mapper).collect(toList());
  }

  private AggregateProtocolMethodTemplateData(final MethodScope methodScope,
                                              final TemplateParameters parentParameters,
                                              final CodeGenerationParameter method) {
    this.parameters =
            TemplateParameters.with(METHOD_SCOPE, methodScope).and(METHOD_NAME, method.value)
                    .and(STATE_NAME, AGGREGATE_STATE.resolveClassname(method.parent().value))
                    .and(ENTITY_NAME, AGGREGATE.resolveClassname(method.parent().value))
                    .and(METHOD_INVOCATION_PARAMETERS, AGGREGATE_METHOD_INVOCATION.format(method))
                    .and(METHOD_PARAMETERS, SIGNATURE_DECLARATION.format(method, methodScope))
                    .and(AGGREGATE_PROTOCOL_VARIABLE, simpleNameToAttribute(method.parent().value))
                    .and(AGGREGATE_PROTOCOL_NAME, method.parent().value);

    parentParameters.addImports(resolveImports(method, methodScope));
  }

  private Set<String> resolveImports(final CodeGenerationParameter method,
                                     final MethodScope methodScope) {
    final Stream<CodeGenerationParameter> involvedStateFields =
            AggregateDetail.findInvolvedStateFields(method.parent(), method.value);

    return Stream.of(AggregateDetail.resolveImports(involvedStateFields).stream(),
            Stream.of(methodScope.requiredClasses).map(clazz -> clazz.getCanonicalName()))
            .flatMap(s -> s).collect(Collectors.toSet());
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return DesignerTemplateStandard.AGGREGATE_PROTOCOL_METHOD;
  }

}
