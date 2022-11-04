// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.resource;

import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.AggregateDetail;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.RouteDetail;
import io.vlingo.xoom.designer.codegen.csharp.formatting.Formatters;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.codegen.csharp.TemplateParameter.*;

public class RouteMethodTemplateData extends TemplateData {

  private static final String DEFAULT_ID_NAME = "id";

  private final TemplateParameters parameters;

  public static List<TemplateData> from(final CodeGenerationParameter autoDispatchParameter,
                                        final TemplateParameters parentParameters) {
    return from(Dialect.findDefault(), autoDispatchParameter, Collections.emptyList(), parentParameters);
  }

  public static List<TemplateData> from(final Dialect dialect, final CodeGenerationParameter mainParameter,
                                        final List<CodeGenerationParameter> valueObjects,
                                        final TemplateParameters parentParameters) {
    final Predicate<CodeGenerationParameter> filter =
        parameter -> !parameter.retrieveRelatedValue(Label.INTERNAL_ROUTE_HANDLER, Boolean::valueOf);

    final Function<CodeGenerationParameter, RouteMethodTemplateData> mapper =
        routeSignatureParameter -> new RouteMethodTemplateData(dialect, mainParameter,
            routeSignatureParameter, valueObjects, parentParameters);

    return mainParameter.retrieveAllRelated(Label.ROUTE_SIGNATURE)
        .filter(filter).map(mapper).collect(Collectors.toList());
  }

  private RouteMethodTemplateData(final Dialect dialect, final CodeGenerationParameter mainParameter,
                                  final CodeGenerationParameter routeSignatureParameter,
                                  final List<CodeGenerationParameter> valueObjects,
                                  final TemplateParameters parentParameters) {
    final HandlerInvocationResolver invocationResolver = HandlerInvocationResolver.with();

    final String routeHandlerInvocation =
        invocationResolver.resolveRouteHandlerInvocation(mainParameter, routeSignatureParameter);

    final String adapterHandlerInvocation =
        invocationResolver.resolveAdapterHandlerInvocation(mainParameter, routeSignatureParameter);

    final List<String> valueObjectInitializers =
        resolveValueObjectInitializers(dialect, routeSignatureParameter, mainParameter, valueObjects);

    this.parameters = TemplateParameters.with(ROUTE_SIGNATURE, RouteDetail.resolveMethodSignature(routeSignatureParameter))
        .and(MODEL_ATTRIBUTE, resolveModelAttributeName(mainParameter))
        .and(ROUTE_METHOD, routeSignatureParameter.retrieveRelatedValue(Label.ROUTE_METHOD))
        .and(REQUIRE_ENTITY_LOADING, RouteDetail.resolveEntityLoading(routeSignatureParameter))
        .and(ADAPTER_HANDLER_INVOCATION, adapterHandlerInvocation)
        .and(VALUE_OBJECT_INITIALIZERS, valueObjectInitializers)
        .and(ROUTE_HANDLER_INVOCATION, routeHandlerInvocation)
        .and(ID_NAME, resolveIdName(routeSignatureParameter));

    parentParameters.addImports(resolveImports(mainParameter, routeSignatureParameter));
  }


  private Set<String> resolveImports(final CodeGenerationParameter mainParameter, final CodeGenerationParameter route) {
    return Stream.of(retrieveIdTypeQualifiedName(route),
            route.retrieveRelatedValue(Label.BODY_TYPE),
            mainParameter.retrieveRelatedValue(Label.HANDLERS_CONFIG_NAME),
            mainParameter.retrieveRelatedValue(Label.MODEL_PROTOCOL),
            mainParameter.retrieveRelatedValue(Label.MODEL_ACTOR),
            mainParameter.retrieveRelatedValue(Label.MODEL_DATA))
        .filter(qualifiedName -> !qualifiedName.isEmpty())
        .collect(Collectors.toSet());
  }

  private String resolveIdName(final CodeGenerationParameter routeSignatureParameter) {
    if (!routeSignatureParameter.hasAny(Label.ID)) {
      return DEFAULT_ID_NAME;
    }
    return routeSignatureParameter.retrieveRelatedValue(Label.ID);
  }

  private String retrieveIdTypeQualifiedName(final CodeGenerationParameter routeSignatureParameter) {
    final String idType = routeSignatureParameter.retrieveRelatedValue(Label.ID_TYPE);
    return idType.contains(".") ? "" : idType;
  }

  private String resolveModelAttributeName(final CodeGenerationParameter mainParameter) {
    final CodeElementFormatter codeElementFormatter = ComponentRegistry.withName("cSharpCodeFormatter");

    if (mainParameter.isLabeled(Label.AGGREGATE)) {
      return codeElementFormatter.simpleNameToAttribute(mainParameter.value);
    }
    final String qualifiedName = mainParameter.retrieveRelatedValue(Label.MODEL_PROTOCOL);
    return codeElementFormatter.qualifiedNameToAttribute(qualifiedName);
  }

  private List<String> resolveValueObjectInitializers(final Dialect dialect, final CodeGenerationParameter route,
                                                      final CodeGenerationParameter aggregate,
                                                      final List<CodeGenerationParameter> valueObjects) {
    if (valueObjects.isEmpty() || !RouteDetail.hasBody(route)) {
      return Collections.emptyList();
    }

    final CodeGenerationParameter method =
        AggregateDetail.methodWithName(aggregate, route.value);

    return Formatters.Variables.format(Formatters.Variables.Style.VALUE_OBJECT_INITIALIZER, dialect, method, valueObjects.stream());
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return CsharpTemplateStandard.ROUTE_METHOD;
  }

}
