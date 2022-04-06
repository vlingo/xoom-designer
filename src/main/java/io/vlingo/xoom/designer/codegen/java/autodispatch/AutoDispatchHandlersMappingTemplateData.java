// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.autodispatch;

import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.content.ContentQuery;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.model.aggregate.AggregateDetail;
import io.vlingo.xoom.designer.codegen.java.model.valueobject.ValueObjectDetail;
import io.vlingo.xoom.designer.codegen.java.resource.RouteDetail;
import io.vlingo.xoom.designer.codegen.java.storage.QueriesDetail;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard.QUERIES;
import static io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard.*;
import static io.vlingo.xoom.designer.codegen.java.TemplateParameter.*;

public class AutoDispatchHandlersMappingTemplateData extends TemplateData {

  private final static String HANDLER_INDEX_PATTERN = "public static final int %s = %d;";
  private final static CodeGenerationParameter STATE_ADAPTER_HANDLER =
          CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "adaptState");

  private final String aggregateName;
  private final TemplateParameters parameters;
  private final CodeElementFormatter codeElementFormatter;

  @SuppressWarnings("unchecked")
  protected AutoDispatchHandlersMappingTemplateData(final String basePackage,
                                                    final Dialect dialect,
                                                    final CodeGenerationParameter aggregate,
                                                    final List<CodeGenerationParameter> valueObjects,
                                                    final List<Content> contents,
                                                    final Boolean useCQRS) {
    this.aggregateName = aggregate.value;
    this.codeElementFormatter = ComponentRegistry.withName("defaultCodeFormatter");
    final String compositeId = AggregateDetail.resolveCompositeIdFieldsNames(aggregate);
    this.parameters =
            TemplateParameters.with(PACKAGE_NAME, resolvePackage(basePackage))
                    .and(AGGREGATE_PROTOCOL_NAME, aggregateName)
                    .and(STATE_NAME, AGGREGATE_STATE.resolveClassname(aggregateName))
                    .and(STATE_DATA_OBJECT_NAME, DATA_OBJECT.resolveClassname(aggregateName))
                    .and(QUERIES_NAME, QUERIES.resolveClassname(aggregateName)).and(USE_CQRS, useCQRS)
                    .and(QUERY_BY_ID_METHOD_NAME, QueriesDetail.resolveQueryByIdMethodName(aggregateName))
                    .and(QUERY_ALL_METHOD_NAME, QueriesDetail.resolveQueryAllMethodName(aggregateName))
                    .andResolve(QUERY_ALL_INDEX_NAME, params -> codeElementFormatter.staticConstant(params.find(QUERY_ALL_METHOD_NAME)))
                    .andResolve(QUERY_BY_ID_INDEX_NAME, params -> codeElementFormatter.staticConstant(params.find(QUERY_BY_ID_METHOD_NAME)))
                    .addImport(codeElementFormatter.importAllFrom(ContentQuery.findPackage(DATA_OBJECT, contents)))
                    .and(AUTO_DISPATCH_HANDLERS_MAPPING_NAME, standard().resolveClassname(aggregateName))
                    .and(HANDLER_INDEXES, resolveHandlerIndexes(aggregate, useCQRS))
                    .and(HANDLER_ENTRIES, new ArrayList<String>())
                    .addImports(resolveImports(aggregate, contents))
                    .and(COMPOSITE_ID, compositeId)
                    .and(COMPOSITE_ID_TYPE, RouteDetail.resolveCompositeIdTypeFrom(compositeId))
                    .and(QUERY_ALL_COMPOSITE_ID_TYPE, RouteDetail.resolveQueryAllCompositeIdTypeFrom(compositeId))
                    .and(QUERY_BY_ID_HANDLER_TYPE, RouteDetail.resolveHandlerTypeFrom(compositeId))
                    .and(QUERY_ALL_HANDLER_TYPE, RouteDetail.resolveQueryAllHandlerTypeFrom(compositeId));

    this.dependOn(AutoDispatchHandlerEntryTemplateData.from(dialect, aggregate, valueObjects));
  }

  @Override
  public void handleDependencyOutcome(final TemplateStandard standard, final String outcome) {
    this.parameters.<List<String>>find(HANDLER_ENTRIES).add(outcome);
  }

  private List<String> resolveHandlerIndexes(final CodeGenerationParameter aggregate, final Boolean useCQRS) {
    final List<CodeGenerationParameter> handlers =
            Stream.of(aggregate.retrieveAllRelated(Label.ROUTE_SIGNATURE), Stream.of(STATE_ADAPTER_HANDLER))
                    .flatMap(stream -> stream).collect(Collectors.toList());

    return IntStream.range(0, handlers.size()).mapToObj(index -> {
      final String signature = handlers.get(index).value;
      final String mappingValue = codeElementFormatter.staticConstant(signature);
      return String.format(HANDLER_INDEX_PATTERN, mappingValue, index);
    }).collect(Collectors.toList());
  }

  private Set<String> resolveImports(final CodeGenerationParameter aggregate,
                                     final List<Content> contents) {
    final Set<String> aggregateRelatedImports =
            mapClassesWithTemplateStandards(aggregate.value).entrySet().stream().map(entry -> {
              try {
                final String className = entry.getValue();
                final TemplateStandard standard = entry.getKey();
                if(className.isEmpty()) {
                  return codeElementFormatter.importAllFrom(ContentQuery.findPackage(standard, contents));
                }
                return ContentQuery.findFullyQualifiedClassName(standard, className, contents);
              } catch (final IllegalArgumentException exception) {
                return null;
              }
            }).collect(Collectors.toSet());

    final Set<String> valueObjectImports =
            ValueObjectDetail.resolveImports(contents, RouteDetail.findInvolvedStateFieldTypes(aggregate));

    return Stream.of(aggregateRelatedImports, valueObjectImports)
        .flatMap(Set::stream)
        .sorted()
        .collect(Collectors.toCollection(LinkedHashSet::new));
  }

  @SuppressWarnings("serial")
  private Map<TemplateStandard, String> mapClassesWithTemplateStandards(final String aggregateName) {
    return new HashMap<TemplateStandard, String>() {{
      put(AGGREGATE_PROTOCOL, aggregateName);
      put(AGGREGATE_STATE, AGGREGATE_STATE.resolveClassname(aggregateName));
      put(QUERIES, QUERIES.resolveClassname(aggregateName));
    }};
  }

  private String resolvePackage(final String basePackage) {
    return String.format("%s.%s.%s", basePackage, "infrastructure", "resource").toLowerCase();
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return JavaTemplateStandard.AUTO_DISPATCH_HANDLERS_MAPPING;
  }

  @Override
  public String filename() {
    return standard().resolveFilename(aggregateName, parameters);
  }

}
