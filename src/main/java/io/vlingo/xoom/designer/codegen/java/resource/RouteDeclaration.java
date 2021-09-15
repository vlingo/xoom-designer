// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.resource;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.http.resource.ResourceBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class RouteDeclaration {

  public final String path;
  public final String bodyType;
  public final String handlerName;
  public final String builderMethod;
  public final String signature;
  public final List<String> parameterTypes = new ArrayList<>();

  public static List<RouteDeclaration> from(final CodeGenerationParameter mainParameter) {
    return mainParameter.retrieveAllRelated(Label.ROUTE_SIGNATURE).map(RouteDeclaration::new).collect(toList());
  }

  private RouteDeclaration(final CodeGenerationParameter routeSignatureParameter) {
    this.signature = RouteDetail.resolveMethodSignature(routeSignatureParameter);
    this.handlerName = resolveHandlerName();
    this.path = PathFormatter.formatAbsoluteRoutePath(routeSignatureParameter);
    this.bodyType = RouteDetail.resolveBodyType(routeSignatureParameter);
    this.builderMethod = ResourceBuilder.class.getCanonicalName() + "." + routeSignatureParameter.retrieveRelatedValue(Label.ROUTE_METHOD).toLowerCase();
    this.parameterTypes.addAll(resolveParameterTypes(routeSignatureParameter));
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
            .collect(toList());
  }

}
