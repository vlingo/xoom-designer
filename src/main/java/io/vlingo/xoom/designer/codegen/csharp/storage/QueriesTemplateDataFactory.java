package io.vlingo.xoom.designer.codegen.csharp.storage;

import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.content.ContentQuery;
import io.vlingo.xoom.codegen.parameter.ImportParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.QueriesDetail;
import io.vlingo.xoom.designer.codegen.csharp.TemplateParameter;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class QueriesTemplateDataFactory {
  public static List<TemplateData> from(final String persistencePackage, final List<Content> contents) {
    return ContentQuery.findClassNames(CsharpTemplateStandard.AGGREGATE_PROTOCOL, contents)
        .stream().map(protocol -> createTemplates(protocol, persistencePackage, contents))
        .flatMap(Collection::stream).collect(Collectors.toList());
  }

  private static List<TemplateData> createTemplates(final String protocol, final String persistencePackage,
                                                    final List<Content> contents) {
    final TemplateParameters parameters = createParameters(persistencePackage, protocol, contents);

    return Arrays.asList(new QueriesTemplateData(protocol, parameters), new QueriesActorTemplateData(protocol.substring(1), parameters));
  }

  private static TemplateParameters createParameters(final String persistencePackage, final String aggregateProtocol,
                                                     final List<Content> contents) {
    final String dataObjectName = CsharpTemplateStandard.DATA_OBJECT.resolveClassname(aggregateProtocol.substring(1));

    final String dataObjectQualifiedName =
        ContentQuery.findPackage(CsharpTemplateStandard.DATA_OBJECT, dataObjectName, contents);

    return TemplateParameters.with(TemplateParameter.PACKAGE_NAME, persistencePackage)
        .and(TemplateParameter.STATE_DATA_OBJECT_NAME, dataObjectName)
        .and(TemplateParameter.QUERY_BY_ID_METHOD_NAME, QueriesDetail.resolveQueryByIdMethodName(aggregateProtocol.substring(1)))
        .and(TemplateParameter.QUERY_ALL_METHOD_NAME, QueriesDetail.resolveQueryAllMethodName(aggregateProtocol.substring(1)))
        .and(TemplateParameter.IMPORTS, ImportParameter.of(dataObjectQualifiedName));
  }

}
