// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.resource;

import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.RouteDetail;
import io.vlingo.xoom.designer.codegen.csharp.ValueObjectDetail;
import io.vlingo.xoom.designer.codegen.csharp.storage.Model;
import io.vlingo.xoom.designer.codegen.csharp.storage.Queries;
import io.vlingo.xoom.designer.codegen.csharp.storage.StorageType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static io.vlingo.xoom.codegen.content.ContentQuery.findPackage;
import static io.vlingo.xoom.designer.codegen.csharp.TemplateParameter.*;

public class RestResourceTemplateData extends TemplateData {

  private final String packageName;
  private final String aggregateName;
  private final TemplateParameters parameters;

  @SuppressWarnings("unchecked")
  public RestResourceTemplateData(final String basePackage, final Dialect dialect,
                                  final CodeGenerationParameter aggregateParameter,
                                  final List<CodeGenerationParameter> valueObjects,
                                  final List<Content> contents, final Boolean useCQRS) {
    this.aggregateName = aggregateParameter.value;
    this.packageName = resolvePackage(basePackage);
    this.parameters = loadParameters(aggregateParameter, contents, useCQRS);
    this.dependOn(RouteMethodTemplateData.from(dialect, aggregateParameter, valueObjects, parameters));
  }

  private TemplateParameters loadParameters(final CodeGenerationParameter aggregateParameter,
                                            final List<Content> contents, final Boolean useCQRS) {
    final String uriRoot = aggregateParameter.retrieveRelatedValue(Label.URI_ROOT);

    final Queries queries = useCQRS ? Queries.from(aggregateParameter, contents) : Queries.empty();

    final Function<TemplateParameters, Object> modelProtocolResolver =
        params -> requireModelTypes(aggregateParameter) ? "I" + aggregateName : "";

    if (useCQRS) {
      aggregateParameter.relate(RouteDetail.defaultQueryRoutes(aggregateParameter));
    }

    return TemplateParameters.with(REST_RESOURCE_NAME, CsharpTemplateStandard.REST_RESOURCE.resolveClassname(aggregateName))
        .and(QUERIES, queries).and(PACKAGE_NAME, packageName).and(USE_CQRS, useCQRS)
        .and(ROUTE_DECLARATIONS, RouteDeclaration.from(aggregateParameter))
        .addImports(resolveImports(aggregateParameter, contents, useCQRS))
        .and(MODEL_ACTOR, CsharpTemplateStandard.AGGREGATE.resolveClassname(aggregateName))
        .and(STORE_PROVIDER_NAME, resolveQueryStoreProviderName())
        .and(URI_ROOT, PathFormatter.formatRootPath(uriRoot))
        .andResolve(MODEL_PROTOCOL, modelProtocolResolver)
        .and(ROUTE_METHODS, new ArrayList<String>());
  }

  private Set<String> resolveImports(final CodeGenerationParameter aggregate, final List<Content> contents,
                                     final Boolean useCQRS) {
    final Set<String> imports = new HashSet<>();
    final Stream<CodeGenerationParameter> involvedStateFields = RouteDetail.findInvolvedStateFieldTypes(aggregate);
    imports.add(findPackage(CsharpTemplateStandard.AGGREGATE_PROTOCOL, contents));
    if (RouteDetail.requireEntityLoad(aggregate)) {
      final String aggregateEntityName = CsharpTemplateStandard.AGGREGATE.resolveClassname(aggregateName);
      imports.add(findPackage(CsharpTemplateStandard.AGGREGATE, aggregateEntityName, contents));
      imports.add(findPackage(CsharpTemplateStandard.DATA_OBJECT, CsharpTemplateStandard.DATA_OBJECT.resolveClassname(aggregateName), contents));
    }
    if (RouteDetail.requireModelFactory(aggregate)) {
      imports.add(findPackage(CsharpTemplateStandard.DATA_OBJECT, CsharpTemplateStandard.DATA_OBJECT.resolveClassname(aggregateName), contents));
    }
    if (useCQRS) {
      imports.add(findPackage(CsharpTemplateStandard.STORE_PROVIDER, resolveQueryStoreProviderName(), contents));
    }
    imports.addAll(ValueObjectDetail.resolveImports(contents, involvedStateFields));
    return imports;
  }

  private String resolveQueryStoreProviderName() {
    final TemplateParameters queryStoreProviderParameters =
        TemplateParameters.with(STORAGE_TYPE, StorageType.STATE_STORE)
            .and(MODEL, Model.QUERY);

    return CsharpTemplateStandard.STORE_PROVIDER.resolveClassname(queryStoreProviderParameters);
  }

  private String resolvePackage(final String basePackage) {
    return String.format("%s.%s.%s", basePackage, "Infrastructure", "Resource");
  }

  private boolean requireModelTypes(final CodeGenerationParameter aggregateParameter) {
    return RouteDetail.requireEntityLoad(aggregateParameter);
  }

  @Override
  public void handleDependencyOutcome(final TemplateStandard standard, final String outcome) {
    this.parameters.<List<String>>find(ROUTE_METHODS).add(outcome);
  }

  @Override
  public TemplateStandard standard() {
    return CsharpTemplateStandard.REST_RESOURCE;
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public String filename() {
    return standard().resolveFilename(aggregateName, parameters);
  }

}
