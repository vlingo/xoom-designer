// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java.autodispatch;

import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.formatting.Formatters;
import io.vlingo.xoom.designer.codegen.java.model.FieldDetail;
import io.vlingo.xoom.designer.codegen.java.resource.PathFormatter;
import io.vlingo.xoom.http.Method;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.codegen.java.TemplateParameter.*;

public class AutoDispatchRouteTemplateData extends TemplateData {

  private final TemplateParameters parameters;
  private final CodeElementFormatter codeElementFormatter;

  public static List<TemplateData> from(final Stream<CodeGenerationParameter> routes) {
    return routes.map(AutoDispatchRouteTemplateData::new).collect(Collectors.toList());
  }

  private AutoDispatchRouteTemplateData(final CodeGenerationParameter route) {
    final CodeGenerationParameter aggregate = route.parent(Label.AGGREGATE);
    this.codeElementFormatter = ComponentRegistry.withName("defaultCodeFormatter");
    this.parameters =
            TemplateParameters.with(RETRIEVAL_ROUTE, isRetrievalRoute(route))
                    .and(ID_TYPE, FieldDetail.typeOf(aggregate, "id"))
                    .and(ROUTE_METHOD, route.retrieveRelatedValue(Label.ROUTE_METHOD))
                    .and(ROUTE_PATH, PathFormatter.formatRelativeRoutePath(route))
                    .and(STATE_DATA_OBJECT_NAME, JavaTemplateStandard.DATA_OBJECT.resolveClassname(aggregate.value))
                    .and(ROUTE_MAPPING_VALUE, codeElementFormatter.staticConstant(route.value))
                    .and(REQUIRE_ENTITY_LOADING, route.retrieveRelatedValue(Label.REQUIRE_ENTITY_LOADING, Boolean::valueOf))
                    .and(METHOD_PARAMETERS, Formatters.Arguments.SIGNATURE_DECLARATION.format(route))
                    .and(AUTO_DISPATCH_HANDLERS_MAPPING_NAME, JavaTemplateStandard.AUTO_DISPATCH_HANDLERS_MAPPING.resolveClassname(aggregate.value))
                    .and(METHOD_NAME, route.value);
  }

  private boolean isRetrievalRoute(final CodeGenerationParameter route) {
    final Method method = route.retrieveRelatedValue(Label.ROUTE_METHOD, Method::from);
    return method.isGET() || method.isOPTIONS();
  }

  @Override
  public TemplateStandard standard() {
    return JavaTemplateStandard.AUTO_DISPATCH_ROUTE;
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

}
