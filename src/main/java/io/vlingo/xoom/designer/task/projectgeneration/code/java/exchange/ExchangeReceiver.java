// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java.exchange;

import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.formatting.AggregateMethodInvocation;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.formatting.Formatters;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.MethodScope;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.aggregate.AggregateDetail;

import java.util.List;
import java.util.stream.Collectors;

public class ExchangeReceiver {

  public final String schemaTypeName;
  public final String localTypeName;
  public final String modelActor;
  public final String modelProtocol;
  public final String modelMethod;
  public final String modelVariable;
  public final String modelMethodParameters;
  public final List<String> valueObjectInitializers;
  public final boolean dispatchToFactoryMethod;

  public static List<ExchangeReceiver> from(final Dialect dialect,
                                            final CodeGenerationParameter exchange,
                                            final List<CodeGenerationParameter> valueObjects) {
    return exchange.retrieveAllRelated(Label.RECEIVER)
            .map(receiver -> new ExchangeReceiver(dialect, exchange, receiver, valueObjects))
            .collect(Collectors.toList());
  }

  private ExchangeReceiver(final Dialect dialect,
                           final CodeGenerationParameter exchange,
                           final CodeGenerationParameter receiver,
                           final List<CodeGenerationParameter> valueObjects) {
    final CodeGenerationParameter aggregateMethod =
            AggregateDetail.methodWithName(exchange.parent(), receiver.retrieveRelatedValue(Label.MODEL_METHOD));

    this.modelMethod = aggregateMethod.value;
    this.modelProtocol = exchange.parent(Label.AGGREGATE).value;
    this.modelActor = JavaTemplateStandard.AGGREGATE.resolveClassname(modelProtocol);
    this.modelVariable = CodeElementFormatter.simpleNameToAttribute(modelProtocol);
    this.localTypeName = JavaTemplateStandard.DATA_OBJECT.resolveClassname(exchange.parent().value);
    this.modelMethodParameters = resolveModelMethodParameters(aggregateMethod);
    this.schemaTypeName = Formatter.formatSchemaTypeName(receiver.retrieveOneRelated(Label.SCHEMA));
    this.dispatchToFactoryMethod = aggregateMethod.retrieveRelatedValue(Label.FACTORY_METHOD, Boolean::valueOf);
    this.valueObjectInitializers = resolveValueObjectInitializers(dialect, receiver, valueObjects);
  }

  private String resolveModelMethodParameters(final CodeGenerationParameter method) {
    final boolean factoryMethod = method.retrieveRelatedValue(Label.FACTORY_METHOD, Boolean::valueOf);
    final MethodScope methodScope = factoryMethod ? MethodScope.STATIC : MethodScope.INSTANCE;
    return AggregateMethodInvocation.handlingDataObject("stage").format(method, methodScope);
  }

  private List<String> resolveValueObjectInitializers(final Dialect dialect,
                                                      final CodeGenerationParameter receiver,
                                                      final List<CodeGenerationParameter> valueObjects) {
    final CodeGenerationParameter aggregate = receiver.parent(Label.AGGREGATE);
    final CodeGenerationParameter method =
            AggregateDetail.methodWithName(aggregate, receiver.retrieveRelatedValue(Label.MODEL_METHOD));

    return Formatters.Variables.format(Formatters.Variables.Style.VALUE_OBJECT_INITIALIZER, dialect, method, valueObjects.stream());
  }

}
