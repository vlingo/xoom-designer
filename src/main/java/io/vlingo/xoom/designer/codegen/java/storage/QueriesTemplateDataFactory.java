// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.storage;

import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.content.ContentQuery;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.parameter.ImportParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.TemplateParameter;
import io.vlingo.xoom.designer.codegen.java.model.FieldDetail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class QueriesTemplateDataFactory {

  public static List<TemplateData> from(final String persistencePackage,
                                        final Boolean useCQRS,
                                        final List<Content> contents) {
    if (!useCQRS) {
      return Collections.emptyList();
    }
    return ContentQuery.findClassNames(JavaTemplateStandard.AGGREGATE_PROTOCOL, contents)
            .stream().map(protocol -> createTemplates(protocol, persistencePackage, contents))
            .flatMap(templateData -> templateData.stream()).collect(Collectors.toList());
  }

  public static List<TemplateData> from(final String persistencePackage,
                                        final Boolean useCQRS,
                                        final List<Content> contents,
                                        final List<CodeGenerationParameter> aggregates) {
    if (!useCQRS) {
      return Collections.emptyList();
    }
    return ContentQuery.findClassNames(JavaTemplateStandard.AGGREGATE_PROTOCOL, contents)
        .stream().map(protocol -> createTemplates(protocol, persistencePackage, contents,
            aggregates.stream().filter(aggregate -> aggregate.value.equals(protocol)).findFirst()))
        .flatMap(templateData -> templateData.stream()).collect(Collectors.toList());
  }

  private static List<TemplateData> createTemplates(final String protocol,
                                                    final String persistencePackage,
                                                    final List<Content> contents) {
    final TemplateParameters parameters =
            createParameters(persistencePackage, protocol, contents);

    return Arrays.asList(new QueriesTemplateData(protocol, parameters),
            new QueriesActorTemplateData(protocol, parameters));
  }

  private static List<TemplateData> createTemplates(final String protocol,
                                                    final String persistencePackage,
                                                    final List<Content> contents,
                                                    final Optional<CodeGenerationParameter> aggregate) {
    final TemplateParameters parameters =
        createParameters(persistencePackage, protocol, contents);

    aggregate.ifPresent(codeGenerationParameter -> parameters.and(TemplateParameter.COMPOSITE_ID,
        resolveCompositeIdFields(codeGenerationParameter)));

    return Arrays.asList(new QueriesTemplateData(protocol, parameters),
        new QueriesActorTemplateData(protocol, parameters));
  }

  private static String resolveCompositeIdFields(CodeGenerationParameter aggregate) {
    final List<String> compositeIdFields = aggregate.retrieveAllRelated(Label.STATE_FIELD)
        .filter(FieldDetail::isCompositeId)
        .map(field -> "String " + field.value).collect(toList());

    if(compositeIdFields.isEmpty())
      return "";

    compositeIdFields.add(" ");
    return String.join(",", compositeIdFields);
  }

  private static TemplateParameters createParameters(final String persistencePackage,
                                                     final String aggregateProtocol,
                                                     final List<Content> contents) {
    final String dataObjectName =
            JavaTemplateStandard.DATA_OBJECT.resolveClassname(aggregateProtocol);

    final String dataObjectQualifiedName =
            ContentQuery.findFullyQualifiedClassName(JavaTemplateStandard.DATA_OBJECT, dataObjectName, contents);

    return TemplateParameters.with(TemplateParameter.PACKAGE_NAME, persistencePackage)
            .and(TemplateParameter.STATE_DATA_OBJECT_NAME, dataObjectName)
            .and(TemplateParameter.QUERY_BY_ID_METHOD_NAME, QueriesDetail.resolveQueryByIdMethodName(aggregateProtocol))
            .and(TemplateParameter.QUERY_ALL_METHOD_NAME, QueriesDetail.resolveQueryAllMethodName(aggregateProtocol))
            .and(TemplateParameter.IMPORTS, ImportParameter.of(dataObjectQualifiedName));
  }

}