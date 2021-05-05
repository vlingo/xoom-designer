// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.dataobject;

import io.vlingo.xoom.designer.task.projectgeneration.code.formatting.Formatters;
import io.vlingo.xoom.designer.task.projectgeneration.code.formatting.Formatters.Fields.Style;
import io.vlingo.xoom.turbo.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.turbo.codegen.content.Content;
import io.vlingo.xoom.turbo.codegen.content.ContentQuery;
import io.vlingo.xoom.turbo.codegen.language.Language;
import io.vlingo.xoom.turbo.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.turbo.codegen.template.TemplateData;
import io.vlingo.xoom.turbo.codegen.template.TemplateParameters;
import io.vlingo.xoom.turbo.codegen.template.TemplateStandard;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard.AGGREGATE_STATE;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard.DATA_OBJECT;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.TemplateParameter.*;
import static java.util.stream.Collectors.toList;

public class StateDataObjectTemplateData extends TemplateData {

  private final static String PACKAGE_PATTERN = "%s.%s";
  private final static String INFRA_PACKAGE_NAME = "infrastructure";

  private final String protocolName;
  private final TemplateParameters parameters;

  public static List<TemplateData> from(final String basePackage,
                                        final Language language,
                                        final Stream<CodeGenerationParameter> aggregates,
                                        final List<Content> contents) {
    final Function<CodeGenerationParameter, TemplateData> mapper =
            aggregate -> new StateDataObjectTemplateData(basePackage, language,
                    aggregate, contents);

    return aggregates.map(mapper).collect(toList());
  }

  private StateDataObjectTemplateData(final String basePackage,
                                      final Language language,
                                      final CodeGenerationParameter aggregate,
                                      final List<Content> contents) {
    this.protocolName = aggregate.value;
    this.parameters =
            loadParameters(resolvePackage(basePackage), language, aggregate, contents);
  }

  private TemplateParameters loadParameters(final String packageName,
                                            final Language language,
                                            final CodeGenerationParameter aggregate,
                                            final List<Content> contents) {

    final String stateName = AGGREGATE_STATE.resolveClassname(aggregate.value);

    final String dataName = DATA_OBJECT.resolveClassname(protocolName);

    final List<String> members =
            Formatters.Fields.format(Style.DATA_OBJECT_MEMBER_DECLARATION, language, aggregate);

    final List<String> membersAssignment =
            Formatters.Fields.format(Style.DATA_VALUE_OBJECT_ASSIGNMENT, language, aggregate);

    return TemplateParameters.with(PACKAGE_NAME, packageName)
            .and(STATE_NAME, stateName).and(STATE_DATA_OBJECT_NAME, dataName)
            .and(STATIC_FACTORY_METHODS, StaticFactoryMethod.from(aggregate))
            .and(MEMBERS, members).and(MEMBERS_ASSIGNMENT, membersAssignment)
            .and(DATA_OBJECT_QUALIFIED_NAME, CodeElementFormatter.qualifiedNameOf(packageName, dataName))
            .and(CONSTRUCTOR_PARAMETERS, Formatters.Arguments.DATA_OBJECT_CONSTRUCTOR.format(aggregate))
            .addImport(ContentQuery.findFullyQualifiedClassName(AGGREGATE_STATE, stateName, contents));
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
