// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
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
import io.vlingo.xoom.designer.codegen.java.model.aggregate.AggregateDetail;

import java.util.List;

import static io.vlingo.xoom.designer.codegen.java.TemplateParameter.*;
import static java.util.stream.Collectors.toList;

public class DomainEventSpecificationTemplateData extends TemplateData {

  private final TemplateParameters parameters;

  public static List<TemplateData> from(final List<CodeGenerationParameter> exchanges) {
    return exchanges.stream().filter(exchange -> exchange.hasAny(Label.DOMAIN_EVENT))
            .flatMap(exchange -> exchange.retrieveAllRelated(Label.DOMAIN_EVENT))
            .map(DomainEventSpecificationTemplateData::new)
            .collect(toList());
  }

  private DomainEventSpecificationTemplateData(final CodeGenerationParameter event) {
    this.parameters =
            TemplateParameters.with(SCHEMA_CATEGORY, CodeGenerationProperties.EVENT_SCHEMA_CATEGORY)
                    .and(SCHEMATA_SPECIFICATION_NAME, event.value).and(SCHEMATA_FILE, true)
                    .and(FIELD_DECLARATIONS, generateFieldDeclarations(event));
  }

  private List<String> generateFieldDeclarations(final CodeGenerationParameter event) {
    final CodeGenerationParameter aggregate = event.parent(Label.AGGREGATE);
    return AggregateDetail.eventWithName(aggregate, event.value)
            .retrieveAllRelated(Label.STATE_FIELD)
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
