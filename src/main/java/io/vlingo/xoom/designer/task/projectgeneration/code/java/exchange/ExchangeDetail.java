// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.java.exchange;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.aggregate.AggregateDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.schemata.Schema;

import java.util.stream.Stream;

public class ExchangeDetail {

  public static Stream<CodeGenerationParameter> findInvolvedStateFieldsOnReceivers(final CodeGenerationParameter exchange) {
    final CodeGenerationParameter aggregate = exchange.parent();
    return exchange.retrieveAllRelated(Label.RECEIVER).flatMap(receiver -> {
      final String methodName = receiver.retrieveRelatedValue(Label.MODEL_METHOD);
      return AggregateDetail.findInvolvedStateFields(aggregate, methodName);
    });
  }

  public static Stream<String> findConsumedQualifiedEventNames(final CodeGenerationParameter exchange) {
    if(exchange.retrieveRelatedValue(Label.ROLE, ExchangeRole::of).isProducer()) {
      return Stream.empty();
    }
    return exchange.retrieveAllRelated(Label.RECEIVER)
            .map(receiver -> receiver.retrieveOneRelated(Label.SCHEMA).<Schema>object())
            .map(schema -> schema.qualifiedName()).distinct();
  }

}
