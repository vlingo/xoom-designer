// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.parameters;

import java.util.HashMap;
import java.util.Map;

public class ExchangeReceiver {

  public final String schema;
  public final String aggregateMethod;
  private final Map<String, String> schemaFieldsMapping = new HashMap<>();

  public ExchangeReceiver(final String schema,
                          final String aggregateMethod,
                          final Map<String, String> schemaFieldsMapping) {
    this.schema = schema;
    this.aggregateMethod = aggregateMethod;
    this.schemaFieldsMapping.putAll(schemaFieldsMapping);
  }

}
