// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.csharp.autodispatch;

import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.AggregateDetail;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.MethodScope;
import io.vlingo.xoom.designer.codegen.csharp.RouteDetail;
import io.vlingo.xoom.designer.codegen.csharp.formatting.AggregateMethodInvocation;
import io.vlingo.xoom.designer.codegen.csharp.formatting.Formatters;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.util.List;
import java.util.stream.Collectors;

import static io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard.AGGREGATE_STATE;
import static io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard.DATA_OBJECT;
import static io.vlingo.xoom.designer.codegen.csharp.TemplateParameter.*;
import static io.vlingo.xoom.designer.codegen.csharp.formatting.Formatters.Variables.Style.VALUE_OBJECT_INITIALIZER;
import static io.vlingo.xoom.designer.codegen.java.TemplateParameter.HANDLER_TYPE;

public class AutoDispatchHandlerEntryTemplateData extends TemplateData {

  private final TemplateParameters parameters;

  public static List<TemplateData> from(final Dialect dialect, final CodeGenerationParameter aggregate,
                                        final List<CodeGenerationParameter> valueObjects) {
    return aggregate.retrieveAllRelated(Label.ROUTE_SIGNATURE).filter(route -> !route.hasAny(Label.READ_ONLY))
        .map(route -> new AutoDispatchHandlerEntryTemplateData(dialect, route, valueObjects))
        .collect(Collectors.toList());
  }

  private AutoDispatchHandlerEntryTemplateData(final Dialect dialect, final CodeGenerationParameter route,
                                               final List<CodeGenerationParameter> valueObjects) {
    final CodeGenerationParameter aggregate = route.parent(Label.AGGREGATE);
    final CodeGenerationParameter method = AggregateDetail.methodWithName(aggregate, route.value);
    final CodeElementFormatter formatter = ComponentRegistry.withName("cSharpCodeFormatter");
    final boolean factoryMethod = isFactoryMethod(method);
    final List<String> valueObjectInitializers =
        Formatters.Variables.format(VALUE_OBJECT_INITIALIZER, dialect, method, valueObjects.stream());

    this.parameters =
        TemplateParameters.with(METHOD_NAME, route.value)
            .and(FACTORY_METHOD, factoryMethod)
            .and(REQUIRE_ENTITY_LOADING, RouteDetail.resolveEntityLoading(route))
            .and(AGGREGATE_PROTOCOL_NAME, aggregate.value)
            .and(STATE_DATA_OBJECT_NAME, DATA_OBJECT.resolveClassname(aggregate.value))
            .and(AGGREGATE_PROTOCOL_VARIABLE, formatter.simpleNameToAttribute(aggregate.value))
            .and(STATE_NAME, AGGREGATE_STATE.resolveClassname(aggregate.value))
            .and(INDEX_NAME, formatter.staticConstant(route.value))
            .and(METHOD_INVOCATION_PARAMETERS, resolveMethodInvocationParameters(method, route))
            .and(VALUE_OBJECT_INITIALIZERS, valueObjectInitializers)
            .and(HANDLER_TYPE, RouteDetail.resolveHandlerTypeFrom(""));
  }

  private String resolveMethodInvocationParameters(final CodeGenerationParameter method, CodeGenerationParameter route) {
    final boolean factoryMethod = isFactoryMethod(method) && !RouteDetail.resolveEntityLoading(route);
    final MethodScope methodScope = factoryMethod ? MethodScope.STATIC : MethodScope.INSTANCE;
    return AggregateMethodInvocation.accessingParametersFromDataObject("$stage").format(method, methodScope);
  }

  private boolean isFactoryMethod(CodeGenerationParameter method) {
    return method.retrieveRelatedValue(Label.FACTORY_METHOD, Boolean::valueOf) || !AggregateDetail.hasFactoryMethod(method.parent());
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return CsharpTemplateStandard.AUTO_DISPATCH_HANDLER_ENTRY;
  }
}
