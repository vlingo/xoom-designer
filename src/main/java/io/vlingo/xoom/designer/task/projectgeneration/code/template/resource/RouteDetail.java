// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.resource;

import io.vlingo.xoom.designer.task.projectgeneration.code.formatting.Formatters;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.model.aggregate.AggregateDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.storage.QueriesDetail;
import io.vlingo.xoom.http.Method;
import io.vlingo.xoom.turbo.codegen.parameter.CodeGenerationParameter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard.DATA_OBJECT;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.*;
import static io.vlingo.xoom.http.Method.*;

public class RouteDetail {

  private static final String BODY_DEFAULT_NAME = "data";
  private static final String METHOD_PARAMETER_PATTERN = "final %s %s";
  private static final String METHOD_SIGNATURE_PATTERN = "%s(%s)";
  private static final List<Method> BODY_SUPPORTED_HTTP_METHODS = Arrays.asList(POST, PUT, PATCH);

  public static String resolveBodyName(final CodeGenerationParameter route) {
    final Method httpMethod = route.retrieveRelatedValue(ROUTE_METHOD, Method::from);

    if (!BODY_SUPPORTED_HTTP_METHODS.contains(httpMethod)) {
      return "";
    }

    if (route.hasAny(BODY)) {
      return route.retrieveRelatedValue(BODY);
    }

    return BODY_DEFAULT_NAME;
  }

  public static String resolveBodyType(final CodeGenerationParameter route) {
    final Method httpMethod = route.retrieveRelatedValue(ROUTE_METHOD, Method::from);

    if (!BODY_SUPPORTED_HTTP_METHODS.contains(httpMethod)) {
      return "";
    }

    if (route.parent().isLabeled(AGGREGATE)) {
      return DATA_OBJECT.resolveClassname(route.parent(AGGREGATE).value);
    }

    return route.retrieveRelatedValue(BODY_TYPE);
  }

  public static boolean requireEntityLoad(final CodeGenerationParameter aggregate) {
    return aggregate.retrieveAllRelated(ROUTE_SIGNATURE)
            .filter(route -> route.hasAny(REQUIRE_ENTITY_LOADING))
            .anyMatch(route -> route.retrieveRelatedValue(REQUIRE_ENTITY_LOADING, Boolean::valueOf));
  }

  public static boolean requireModelFactory(final CodeGenerationParameter aggregate) {
    return aggregate.retrieveAllRelated(ROUTE_SIGNATURE)
            .map(methodSignature -> AggregateDetail.methodWithName(aggregate, methodSignature.value))
            .anyMatch(method -> method.retrieveRelatedValue(FACTORY_METHOD, Boolean::valueOf));
  }

  public static String resolveMethodSignature(final CodeGenerationParameter routeSignature) {
    if (hasValidMethodSignature(routeSignature.value)) {
      return routeSignature.value;
    }

    if (routeSignature.retrieveRelatedValue(Label.ROUTE_METHOD, Method::from).isGET()) {
      final String arguments =
              Formatters.Arguments.SIGNATURE_DECLARATION.format(routeSignature);

      return String.format(METHOD_SIGNATURE_PATTERN, routeSignature.value, arguments);
    }

    return resolveMethodSignatureWithParams(routeSignature);
  }

  public static Stream<CodeGenerationParameter> findInvolvedStateFieldTypes(final CodeGenerationParameter aggregate) {
    return aggregate.retrieveAllRelated(ROUTE_SIGNATURE).filter(RouteDetail::hasBody)
            .map(route -> AggregateDetail.methodWithName(aggregate, route.value))
            .flatMap(method -> method.retrieveAllRelated(METHOD_PARAMETER))
            .map(parameter -> AggregateDetail.stateFieldWithName(aggregate, parameter.value));
  }

  private static String resolveMethodSignatureWithParams(final CodeGenerationParameter routeSignature) {
    final String idParameter =
            routeSignature.retrieveRelatedValue(REQUIRE_ENTITY_LOADING, Boolean::valueOf) ?
                    String.format(METHOD_PARAMETER_PATTERN, "String", "id") : "";

    final CodeGenerationParameter method = AggregateDetail.methodWithName(routeSignature.parent(), routeSignature.value);
    final String dataClassname = DATA_OBJECT.resolveClassname(routeSignature.parent().value);
    final String dataParameterDeclaration = String.format(METHOD_PARAMETER_PATTERN, dataClassname, "data");
    final String dataParameter = method.hasAny(METHOD_PARAMETER) ? dataParameterDeclaration : "";
    final String parameters =
            Stream.of(idParameter, dataParameter).filter(param -> !param.isEmpty())
                    .collect(Collectors.joining(", "));
    return String.format(METHOD_SIGNATURE_PATTERN, routeSignature.value, parameters);
  }

  private static boolean hasValidMethodSignature(final String signature) {
    return signature.contains("(") && signature.contains(")");
  }

  public static CodeGenerationParameter[] defaultQueryRoutes(final CodeGenerationParameter aggregate) {
    final CodeGenerationParameter queryAll =
            CodeGenerationParameter.of(ROUTE_SIGNATURE, QueriesDetail.resolveQueryAllMethodName(aggregate.value))
                    .relate(ROUTE_METHOD, GET).relate(READ_ONLY, "true");

    final CodeGenerationParameter queryById =
            CodeGenerationParameter.of(ROUTE_SIGNATURE, QueriesDetail.resolveQueryByIdMethodName(aggregate.value))
                    .relate(ROUTE_METHOD, GET)
                    .relate(ROUTE_PATH, "/{id}")
                    .relate(READ_ONLY, "true")
                    .relate(METHOD_PARAMETER, "id");

    return new CodeGenerationParameter[]{queryAll, queryById};
  }

  public static boolean hasBody(final CodeGenerationParameter route) {
    return !resolveBodyName(route).isEmpty();
  }
}
