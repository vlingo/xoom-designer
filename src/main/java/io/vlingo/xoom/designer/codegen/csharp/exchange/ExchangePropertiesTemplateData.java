// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.exchange;

import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.TemplateParameter;

import java.util.List;
import java.util.stream.Collectors;

public class ExchangePropertiesTemplateData extends TemplateData {

  private final TemplateParameters parameters;

  public static TemplateData from(final List<Exchange> exchanges) {
    return new ExchangePropertiesTemplateData(exchanges);
  }

  private ExchangePropertiesTemplateData(final List<Exchange> exchanges) {
    this.parameters = TemplateParameters.with(TemplateParameter.EXCHANGES, exchanges)
        .and(TemplateParameter.INLINE_EXCHANGE_NAMES, exchanges.stream().map(exchange -> exchange.name).collect(Collectors.joining(";")));
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return CsharpTemplateStandard.TURBO_SETTINGS;
  }

}
