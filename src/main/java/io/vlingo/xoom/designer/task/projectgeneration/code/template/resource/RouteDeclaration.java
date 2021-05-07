// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.resource;

import io.vlingo.xoom.designer.task.projectgeneration.code.template.Label;
import io.vlingo.xoom.http.resource.ResourceBuilder;
import io.vlingo.xoom.turbo.codegen.parameter.CodeGenerationParameter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.ROUTE_METHOD;
import static java.util.stream.Collectors.toList;

public class RouteDeclaration {

  private final boolean last;
  private final String path;
  private final String bodyType;
  private final String handlerName;
  private final String builderMethod;
  private final String signature;
  private final List<String> parameterTypes = new ArrayList<>();

  public static List<RouteDeclaration> from(final CodeGenerationParameter mainParameter) {
    final List<CodeGenerationParameter> routeSignatures =
            mainParameter.retrieveAllRelated(Label.ROUTE_SIGNATURE).collect(toList());

    return IntStream.range(0, routeSignatures.size()).mapToObj(index ->
            new RouteDeclaration(index, routeSignatures.size(), routeSignatures.get(index)))
            .collect(Collectors.toList());
  }

  private RouteDeclaration(final int routeIndex,
                           final int numberOfRoutes,
                           final CodeGenerationParameter routeSignatureParameter) {
    this.signature = RouteDetail.resolveMethodSignature(routeSignatureParameter);
    this.handlerName = resolveHandlerName();
    this.path = PathFormatter.formatAbsoluteRoutePath(routeSignatureParameter);
    this.bodyType = RouteDetail.resolveBodyType(routeSignatureParameter);
    this.builderMethod = routeSignatureParameter.retrieveRelatedValue(ROUTE_METHOD);
    this.parameterTypes.addAll(resolveParameterTypes(routeSignatureParameter));
    this.last = routeIndex == numberOfRoutes - 1;
  }

  private String resolveHandlerName() {
    return signature.substring(0, signature.indexOf("(")).trim();
  }

  private List<String> resolveParameterTypes(final CodeGenerationParameter routeSignatureParameter) {
    final String bodyParameterName = RouteDetail.resolveBodyName(routeSignatureParameter);

    final String parameters =
            signature.substring(signature.indexOf("(") + 1, signature.lastIndexOf(")"));

    if (parameters.trim().isEmpty()) {
      return Collections.emptyList();
    }

    return Stream.of(parameters.split(","))
            .map(parameter -> parameter.replaceAll("final", "").trim())
            .filter(parameter -> !parameter.endsWith(" " + bodyParameterName))
            .map(parameter -> parameter.substring(0, parameter.indexOf(" ")))
            .collect(Collectors.toList());
  }

  public String getPath() {
    return path;
  }

  public String getBodyType() {
    return bodyType;
  }

  public String getHandlerName() {
    return handlerName;
  }

  public String getBuilderMethod() {
    return ResourceBuilder.class.getCanonicalName() + "." + builderMethod.toLowerCase();
  }

  public List<String> getParameterTypes() {
    return parameterTypes;
  }

  public boolean isLast() {
    return last;
  }

}
