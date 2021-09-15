// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java.schemata;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.CodeGenerationProperties;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.model.valueobject.ValueObjectDetail;

import java.util.List;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.codegen.java.TemplateParameter.*;
import static java.util.stream.Collectors.toList;

public class ValueObjectSpecificationTemplateData extends TemplateData {

  private final TemplateParameters parameters;

  public static List<TemplateData> from(final List<CodeGenerationParameter> exchanges,
                                        final List<CodeGenerationParameter> valueObjects) {
    final Stream<CodeGenerationParameter> publishedValueObjects =
            ValueObjectDetail.findPublishedValueObjects(exchanges, valueObjects);

    return ValueObjectDetail.orderByDependency(publishedValueObjects)
            .map(ValueObjectSpecificationTemplateData::new)
            .collect(toList());
  }

  private ValueObjectSpecificationTemplateData(final CodeGenerationParameter valueObject) {
    this.parameters =
            TemplateParameters.with(SCHEMA_CATEGORY, CodeGenerationProperties.DATA_SCHEMA_CATEGORY)
                    .and(SCHEMATA_SPECIFICATION_NAME, valueObject.value)
                    .and(FIELD_DECLARATIONS, generateFieldDeclarations(valueObject))
                    .and(SCHEMATA_FILE, true);
  }

  private List<String> generateFieldDeclarations(final CodeGenerationParameter valueObject) {
    return valueObject.retrieveAllRelated(Label.VALUE_OBJECT_FIELD)
            .map(Schema::resolveFieldDeclaration).collect(toList());
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return JavaTemplateStandard.SCHEMATA_SPECIFICATION;
  }
}
