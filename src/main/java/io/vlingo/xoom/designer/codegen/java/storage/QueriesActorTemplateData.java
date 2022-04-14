// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.storage;

import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.TemplateParameter;

public class QueriesActorTemplateData extends TemplateData {

  private final String aggregateProtocol;
  private final TemplateParameters parameters;

  protected QueriesActorTemplateData(final String aggregateProtocol,
                                     final TemplateParameters parameters) {
    this.aggregateProtocol = aggregateProtocol;
    this.parameters =
            parameters.and(TemplateParameter.QUERIES_ACTOR_NAME, JavaTemplateStandard.QUERIES_ACTOR.resolveClassname(aggregateProtocol));
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return JavaTemplateStandard.QUERIES_ACTOR;
  }

  @Override
  public String filename() {
    return standard().resolveFilename(aggregateProtocol, parameters);
  }

}