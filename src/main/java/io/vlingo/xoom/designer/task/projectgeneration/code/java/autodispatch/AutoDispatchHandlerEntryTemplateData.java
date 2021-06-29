// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.java.autodispatch;

import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.formatting.AggregateMethodInvocation;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.formatting.Formatters;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.MethodScope;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.aggregate.AggregateDetail;

import java.util.List;
import java.util.stream.Collectors;

import static io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard.AGGREGATE_STATE;
import static io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard.DATA_OBJECT;
import static io.vlingo.xoom.designer.task.projectgeneration.code.java.TemplateParameter.*;
import static io.vlingo.xoom.designer.task.projectgeneration.code.java.formatting.Formatters.Variables.Style.VALUE_OBJECT_INITIALIZER;

public class AutoDispatchHandlerEntryTemplateData extends TemplateData {

  private final TemplateParameters parameters;

  public static List<TemplateData> from(final Dialect dialect,
                                        final CodeGenerationParameter aggregate,
                                        final List<CodeGenerationParameter> valueObjects) {
    return aggregate.retrieveAllRelated(Label.ROUTE_SIGNATURE).filter(route -> !route.hasAny(Label.READ_ONLY))
            .map(route -> new AutoDispatchHandlerEntryTemplateData(dialect, route, valueObjects))
            .collect(Collectors.toList());
  }

  private AutoDispatchHandlerEntryTemplateData(final Dialect dialect,
                                               final CodeGenerationParameter route,
                                               final List<CodeGenerationParameter> valueObjects) {
    final CodeGenerationParameter aggregate = route.parent(Label.AGGREGATE);
    final CodeGenerationParameter method = AggregateDetail.methodWithName(aggregate, route.value);
    final boolean factoryMethod = method.retrieveRelatedValue(Label.FACTORY_METHOD, Boolean::valueOf);
    final List<String> valueObjectInitializers =
            Formatters.Variables.format(VALUE_OBJECT_INITIALIZER, dialect, method, valueObjects.stream());

    this.parameters =
            TemplateParameters.with(METHOD_NAME, route.value)
                    .and(FACTORY_METHOD, factoryMethod)
                    .and(AGGREGATE_PROTOCOL_NAME, aggregate.value)
                    .and(STATE_DATA_OBJECT_NAME, DATA_OBJECT.resolveClassname(aggregate.value))
                    .and(AGGREGATE_PROTOCOL_VARIABLE, CodeElementFormatter.simpleNameToAttribute(aggregate.value))
                    .and(STATE_NAME, AGGREGATE_STATE.resolveClassname(aggregate.value))
                    .and(INDEX_NAME, CodeElementFormatter.staticConstant(route.value))
                    .and(METHOD_INVOCATION_PARAMETERS, resolveMethodInvocationParameters(method))
                    .and(VALUE_OBJECT_INITIALIZERS, valueObjectInitializers);
  }

  private String resolveMethodInvocationParameters(final CodeGenerationParameter method) {
    final boolean factoryMethod = method.retrieveRelatedValue(Label.FACTORY_METHOD, Boolean::valueOf);
    final MethodScope methodScope = factoryMethod ? MethodScope.STATIC : MethodScope.INSTANCE;
    return AggregateMethodInvocation.accessingParametersFromDataObject("$stage").format(method, methodScope);
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return JavaTemplateStandard.AUTO_DISPATCH_HANDLER_ENTRY;
  }
}
