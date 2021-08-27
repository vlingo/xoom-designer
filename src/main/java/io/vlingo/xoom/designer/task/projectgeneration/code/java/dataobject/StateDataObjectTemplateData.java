// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java.dataobject;

import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.content.ContentQuery;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.formatting.Formatters;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.FieldDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.aggregate.AggregateDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.valueobject.ValueObjectDetail;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard.AGGREGATE_STATE;
import static io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard.DATA_OBJECT;
import static io.vlingo.xoom.designer.task.projectgeneration.code.java.TemplateParameter.*;
import static java.util.stream.Collectors.toList;

public class StateDataObjectTemplateData extends TemplateData {

  private final static String PACKAGE_PATTERN = "%s.%s";
  private final static String INFRA_PACKAGE_NAME = "infrastructure";

  private final String protocolName;
  private final TemplateParameters parameters;
  private final CodeElementFormatter codeElementFormatter;

  public static List<TemplateData> from(final String basePackage,
                                        final Dialect dialect,
                                        final Stream<CodeGenerationParameter> aggregates,
                                        final List<CodeGenerationParameter> valueObjects,
                                        final List<Content> contents) {
    final Function<CodeGenerationParameter, TemplateData> mapper =
        aggregate -> new StateDataObjectTemplateData(basePackage, dialect,
            aggregate, valueObjects, contents);

    return aggregates.map(mapper).collect(toList());
  }

  private StateDataObjectTemplateData(final String basePackage,
                                      final Dialect dialect,
                                      final CodeGenerationParameter aggregate,
                                      final List<CodeGenerationParameter> valueObjects,
                                      final List<Content> contents) {
    this.protocolName = aggregate.value;
    this.codeElementFormatter = ComponentRegistry.withName("defaultCodeFormatter");
    this.parameters =
        loadParameters(resolvePackage(basePackage), dialect, aggregate, valueObjects, contents);
  }

  private TemplateParameters loadParameters(final String packageName,
                                            final Dialect dialect,
                                            final CodeGenerationParameter aggregate,
                                            final List<CodeGenerationParameter> valueObjects,
                                            final List<Content> contents) {
    final String stateName = AGGREGATE_STATE.resolveClassname(aggregate.value);

    final String dataName = DATA_OBJECT.resolveClassname(protocolName);

    final List<String> members =
        Formatters.Fields.format(Formatters.Fields.Style.DATA_OBJECT_MEMBER_DECLARATION, dialect, aggregate);

    final List<String> membersAssignment =
        Formatters.Fields.format(Formatters.Fields.Style.DATA_VALUE_OBJECT_ASSIGNMENT, dialect, aggregate);

    final List<String> valueObjectTranslations =
            Formatters.Variables.format(Formatters.Variables.Style.DATA_TO_VALUE_OBJECT_TRANSLATION, dialect, aggregate, valueObjects.stream());

    return TemplateParameters.with(PACKAGE_NAME, packageName)
        .and(STATE_NAME, stateName).and(STATE_DATA_OBJECT_NAME, dataName)
        .and(STATIC_FACTORY_METHODS, StaticFactoryMethod.from(aggregate))
        .and(MEMBERS, members).and(MEMBERS_ASSIGNMENT, membersAssignment)
        .and(MEMBER_NAMES, aggregate.retrieveAllRelated(Label.STATE_FIELD).map(p -> p.value).collect(Collectors.toList()))
        .and(VALUE_OBJECT_TRANSLATIONS, valueObjectTranslations).and(STATE_FIELDS, joinStateFields(aggregate))
        .and(DATA_OBJECT_QUALIFIED_NAME, codeElementFormatter.qualifiedNameOf(packageName, dataName))
        .and(CONSTRUCTOR_PARAMETERS, Formatters.Arguments.DATA_OBJECT_CONSTRUCTOR.format(aggregate))
        .addImports(ValueObjectDetail.resolveImports(contents, aggregate.retrieveAllRelated(Label.STATE_FIELD)))
        .addImport(ContentQuery.findFullyQualifiedClassName(AGGREGATE_STATE, stateName, contents))
        .addImport(codeElementFormatter.importAllFrom("java.util"))
        .addImports(AggregateDetail.resolveImports(aggregate));
  }

  private String joinStateFields(final CodeGenerationParameter aggregate) {
    return aggregate.retrieveAllRelated(Label.STATE_FIELD)
        .map(field -> {
          if (FieldDetail.isValueObjectCollection(field)) {
            return ValueObjectDetail.translateDataObjectCollection(field.value, field);
          } else {
            return field.value;
          }
        }).collect(Collectors.joining(", "));
  }

  private String resolvePackage(final String basePackage) {
    return String.format(PACKAGE_PATTERN, basePackage,
        INFRA_PACKAGE_NAME).toLowerCase();
  }

  @Override
  public String filename() {
    return standard().resolveFilename(protocolName, parameters);
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return DATA_OBJECT;
  }

}
