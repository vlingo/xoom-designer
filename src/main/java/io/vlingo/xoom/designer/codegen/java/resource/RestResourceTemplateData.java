// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.resource;

import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.model.FieldDetail;
import io.vlingo.xoom.designer.codegen.java.model.valueobject.ValueObjectDetail;
import io.vlingo.xoom.designer.codegen.java.storage.Model;
import io.vlingo.xoom.designer.codegen.java.storage.Queries;
import io.vlingo.xoom.designer.codegen.java.storage.StorageType;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static io.vlingo.xoom.codegen.content.ContentQuery.findFullyQualifiedClassName;
import static io.vlingo.xoom.codegen.content.ContentQuery.findPackage;
import static io.vlingo.xoom.designer.codegen.java.TemplateParameter.*;
import static java.util.stream.Collectors.toList;

public class RestResourceTemplateData extends TemplateData {

  private final String packageName;
  private final String aggregateName;
  private final TemplateParameters parameters;

  @SuppressWarnings("unchecked")
  public RestResourceTemplateData(final String basePackage,
                                  final Dialect dialect,
                                  final CodeGenerationParameter aggregateParameter,
                                  final List<CodeGenerationParameter> valueObjects,
                                  final List<Content> contents,
                                  final Boolean useCQRS) {
    this.aggregateName = aggregateParameter.value;
    this.packageName = resolvePackage(basePackage);
    this.parameters = loadParameters(aggregateParameter, contents, useCQRS);
    this.dependOn(RouteMethodTemplateData.from(dialect, aggregateParameter, valueObjects, parameters));
  }

  private TemplateParameters loadParameters(final CodeGenerationParameter aggregateParameter,
                                            final List<Content> contents,
                                            final Boolean useCQRS) {
    final String uriRoot =
            aggregateParameter.retrieveRelatedValue(Label.URI_ROOT);

    final Queries queries =
            Queries.from(aggregateParameter, contents, useCQRS);

    final Function<TemplateParameters, Object> modelProtocolResolver =
            params -> requireModelTypes(aggregateParameter) ? aggregateName : "";

    if (useCQRS) {
      aggregateParameter.relate(RouteDetail.defaultQueryRoutes(aggregateParameter));
    }

    return TemplateParameters.with(REST_RESOURCE_NAME, JavaTemplateStandard.REST_RESOURCE.resolveClassname(aggregateName))
            .and(QUERIES, queries).and(PACKAGE_NAME, packageName).and(USE_CQRS, useCQRS)
            .and(ROUTE_DECLARATIONS, RouteDeclaration.from(aggregateParameter))
            .addImports(resolveImports(aggregateParameter, contents, useCQRS))
            .and(MODEL_ACTOR, JavaTemplateStandard.AGGREGATE.resolveClassname(aggregateName))
            .and(STORE_PROVIDER_NAME, resolveQueryStoreProviderName())
            .and(URI_ROOT, PathFormatter.formatRootPath(uriRoot))
            .andResolve(MODEL_PROTOCOL, modelProtocolResolver)
            .and(ROUTE_METHODS, new ArrayList<String>())
            .and(COMPOSITE_ID, resolveCompositeIdFields(aggregateParameter))
            .and(USE_AUTO_DISPATCH, false);
  }

  private String resolveCompositeIdFields(CodeGenerationParameter method) {
    final List<String> compositeIdFields = method.retrieveAllRelated(Label.STATE_FIELD)
        .filter(FieldDetail::isCompositeId)
        .map(field -> "final String " + field.value).collect(toList());

    if(compositeIdFields.isEmpty())
      return "";
    compositeIdFields.add(" ");
    return String.join(", ", compositeIdFields);
  }

  private Set<String> resolveImports(final CodeGenerationParameter aggregate,
                                     final List<Content> contents,
                                     final Boolean useCQRS) {
    final Set<String> imports = new HashSet<>();
    final CodeElementFormatter codeElementFormatter = ComponentRegistry.withName("defaultCodeFormatter");
    final Stream<CodeGenerationParameter> involvedStateFields = RouteDetail.findInvolvedStateFieldTypes(aggregate);
    if (RouteDetail.requireEntityLoad(aggregate)) {
      final String aggregateEntityName = JavaTemplateStandard.AGGREGATE.resolveClassname(aggregateName);
      imports.add(findFullyQualifiedClassName(JavaTemplateStandard.AGGREGATE, aggregateEntityName, contents));
      imports.add(findFullyQualifiedClassName(JavaTemplateStandard.AGGREGATE_PROTOCOL, aggregateName, contents));
      imports.add(codeElementFormatter.importAllFrom(findPackage(JavaTemplateStandard.DATA_OBJECT, JavaTemplateStandard.DATA_OBJECT.resolveClassname(aggregateName), contents)));
    }
    if (RouteDetail.requireModelFactory(aggregate)) {
      imports.add(findFullyQualifiedClassName(JavaTemplateStandard.AGGREGATE_PROTOCOL, aggregateName, contents));
      imports.add(codeElementFormatter.importAllFrom(findPackage(JavaTemplateStandard.DATA_OBJECT, JavaTemplateStandard.DATA_OBJECT.resolveClassname(aggregateName), contents)));
    }
    if (useCQRS) {
      final String queriesName = JavaTemplateStandard.QUERIES.resolveClassname(aggregateName);
      imports.add(findFullyQualifiedClassName(JavaTemplateStandard.STORE_PROVIDER, resolveQueryStoreProviderName(), contents));
      imports.add(findFullyQualifiedClassName(JavaTemplateStandard.QUERIES, queriesName, contents));
    }
    imports.addAll(ValueObjectDetail.resolveImports(contents, involvedStateFields));
    return imports;
  }

  private String resolveQueryStoreProviderName() {
    final TemplateParameters queryStoreProviderParameters =
            TemplateParameters.with(STORAGE_TYPE, StorageType.STATE_STORE)
                    .and(MODEL, Model.QUERY);

    return JavaTemplateStandard.STORE_PROVIDER.resolveClassname(queryStoreProviderParameters);
  }

  private String resolvePackage(final String basePackage) {
    return String.format("%s.%s.%s", basePackage, "infrastructure", "resource");
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
    return JavaTemplateStandard.REST_RESOURCE;
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
