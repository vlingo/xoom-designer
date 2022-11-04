// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.formatting.Formatters;
import io.vlingo.xoom.http.Method;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.codegen.csharp.FieldDetail.toPascalCase;
import static io.vlingo.xoom.http.Method.*;

public class RouteDetail {

  private static final String BODY_DEFAULT_NAME = "data";
  private static final String METHOD_PARAMETER_PATTERN = "%s %s";
  private static final String METHOD_SIGNATURE_PATTERN = "%s(%s)";
  private static final List<Method> BODY_SUPPORTED_HTTP_METHODS = Arrays.asList(POST, PUT, PATCH);

  public static String resolveBodyName(final CodeGenerationParameter route) {
    final Method httpMethod = route.retrieveRelatedValue(Label.ROUTE_METHOD, Method::from);

    if (!BODY_SUPPORTED_HTTP_METHODS.contains(httpMethod)) {
      return "";
    }

    if (route.hasAny(Label.BODY)) {
      return route.retrieveRelatedValue(Label.BODY);
    }

    return BODY_DEFAULT_NAME;
  }

  public static String resolveBodyType(final CodeGenerationParameter route) {
    final Method httpMethod = route.retrieveRelatedValue(Label.ROUTE_METHOD, Method::from);

    if (!BODY_SUPPORTED_HTTP_METHODS.contains(httpMethod)) {
      return "";
    }

    if (route.parent().isLabeled(Label.AGGREGATE)) {
      final CodeGenerationParameter aggregate = route.parent(Label.AGGREGATE);
      if (AggregateDetail.methodWithName(aggregate, route.value).hasAny(Label.METHOD_PARAMETER)) {
        return CsharpTemplateStandard.DATA_OBJECT.resolveClassname(route.parent(Label.AGGREGATE).value);
      }
      return "";
    }
    return route.retrieveRelatedValue(Label.BODY_TYPE);
  }

  public static boolean requireEntityLoad(final CodeGenerationParameter aggregate) {
    return aggregate.retrieveAllRelated(Label.ROUTE_SIGNATURE)
        .filter(route -> route.hasAny(Label.REQUIRE_ENTITY_LOADING))
        .anyMatch(route -> route.retrieveRelatedValue(Label.REQUIRE_ENTITY_LOADING, Boolean::valueOf));
  }

  public static boolean requireModelFactory(final CodeGenerationParameter aggregate) {
    try {
      return !AggregateDetail.hasFactoryMethod(aggregate) || aggregate.retrieveAllRelated(Label.ROUTE_SIGNATURE)
          .map(methodSignature -> AggregateDetail.methodWithName(aggregate, methodSignature.value))
          .anyMatch(method -> method.retrieveRelatedValue(Label.FACTORY_METHOD, Boolean::valueOf));
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  public static String resolveMethodSignature(final CodeGenerationParameter routeSignature) {
    if (hasValidMethodSignature(routeSignature.value)) {
      return toPascalCase(routeSignature.value);
    }

    if (routeSignature.retrieveRelatedValue(Label.ROUTE_METHOD, Method::from).isGET()) {
      final String arguments =
          Formatters.Arguments.SIGNATURE_DECLARATION.format(routeSignature);

      final String parameters = formatParameters(Stream.of(arguments));
      return String.format(METHOD_SIGNATURE_PATTERN, toPascalCase(routeSignature.value), parameters);
    }

    return resolveMethodSignatureWithParams(routeSignature);
  }

  public static Stream<CodeGenerationParameter> findInvolvedStateFieldTypes(final CodeGenerationParameter aggregate) {
    return aggregate.retrieveAllRelated(Label.ROUTE_SIGNATURE).filter(RouteDetail::hasBody)
        .map(route -> AggregateDetail.methodWithName(aggregate, route.value))
        .flatMap(method -> method.retrieveAllRelated(Label.METHOD_PARAMETER))
        .map(parameter -> AggregateDetail.stateFieldWithName(aggregate, parameter.value));
  }

  public static Boolean resolveEntityLoading(final CodeGenerationParameter route) {
    return route.retrieveRelatedValue(Label.ROUTE_PATH).contains("{id}") || AggregateDetail.hasFactoryMethod(route.parent()) &&
        (route.hasAny(Label.REQUIRE_ENTITY_LOADING) && route.retrieveRelatedValue(Label.REQUIRE_ENTITY_LOADING, Boolean::valueOf));
  }

  private static String resolveMethodSignatureWithParams(final CodeGenerationParameter routeSignature) {
    final String idParameter = requireModelFactory(routeSignature.parent()) &&
        routeSignature.retrieveRelatedValue(Label.REQUIRE_ENTITY_LOADING, Boolean::valueOf) ?
        String.format(METHOD_PARAMETER_PATTERN, "string", "id") : "";
    final CodeGenerationParameter method = AggregateDetail.methodWithName(routeSignature.parent(), routeSignature.value);
    final String dataClassname = CsharpTemplateStandard.DATA_OBJECT.resolveClassname(routeSignature.parent().value);
    final String dataParameterDeclaration = String.format(METHOD_PARAMETER_PATTERN, dataClassname, "data");
    final String dataParameter = method.hasAny(Label.METHOD_PARAMETER) ? dataParameterDeclaration : "";

    final String parameters = formatParameters(Stream.of(idParameter, dataParameter));
    return String.format(METHOD_SIGNATURE_PATTERN, toPascalCase(routeSignature.value), parameters);
  }

  private static String formatParameters(Stream<String> arguments) {
    return arguments
        .distinct()
        .filter(param -> !param.isEmpty())
        .collect(Collectors.joining(", "));
  }

  private static boolean hasValidMethodSignature(final String signature) {
    return signature.contains("(") && signature.contains(")");
  }

  public static CodeGenerationParameter[] defaultQueryRoutes(final CodeGenerationParameter aggregate) {
    final CodeGenerationParameter queryAll =
        CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, QueriesDetail.resolveQueryAllMethodName(aggregate.value))
            .relate(Label.ROUTE_METHOD, GET).relate(Label.READ_ONLY, "true");

    final CodeGenerationParameter queryById =
        CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, QueriesDetail.resolveQueryByIdMethodName(aggregate.value))
            .relate(Label.ROUTE_METHOD, GET)
            .relate(Label.ROUTE_PATH, "/{id}")
            .relate(Label.READ_ONLY, "true")
            .relate(Label.METHOD_PARAMETER, "id");

    return new CodeGenerationParameter[]{queryAll, queryById};
  }

  public static boolean hasBody(final CodeGenerationParameter route) {
    return !resolveBodyName(route).isEmpty();
  }
}
