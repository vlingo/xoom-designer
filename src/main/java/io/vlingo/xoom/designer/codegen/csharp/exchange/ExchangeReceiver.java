// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.exchange;

import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.AggregateDetail;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.MethodScope;
import io.vlingo.xoom.designer.codegen.csharp.ValueObjectDetail;
import io.vlingo.xoom.designer.codegen.csharp.formatting.AggregateMethodInvocation;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.util.List;
import java.util.stream.Collectors;

public class ExchangeReceiver {

  public final String modelActor;
  public final String modelProtocol;
  public final String modelMethod;
  public final String modelVariable;
  public final String localTypeName;
  public final String modelMethodParameters;
  public final String innerClassName;
  public final String localTypeQualifiedName;
  public final boolean dispatchToFactoryMethod;

  public static List<ExchangeReceiver> from(final CodeGenerationParameter exchange, List<CodeGenerationParameter> valueObjects) {
    return exchange.retrieveAllRelated(Label.RECEIVER)
            .map(receiver -> new ExchangeReceiver(exchange, receiver, valueObjects))
            .collect(Collectors.toList());
  }

  private ExchangeReceiver(final CodeGenerationParameter exchange,
                           final CodeGenerationParameter receiver, List<CodeGenerationParameter> valueObjects) {
    final CodeElementFormatter codeElementFormatter = ComponentRegistry.withName("cSharpCodeFormatter");

    final CodeGenerationParameter aggregateMethod =
            AggregateDetail.methodWithName(exchange.parent(), receiver.retrieveRelatedValue(Label.MODEL_METHOD));

    final Schema schema = receiver.retrieveOneRelated(Label.SCHEMA).object();

    this.modelMethod = aggregateMethod.value;
    this.localTypeName = schema.simpleClassName();
    this.modelProtocol = exchange.parent(Label.AGGREGATE).value;
    this.innerClassName = schema.innerReceiverClassName();
    this.modelActor = CsharpTemplateStandard.AGGREGATE.resolveClassname(modelProtocol);
    this.modelVariable = codeElementFormatter.simpleNameToAttribute(modelProtocol);
    this.modelMethodParameters = resolveModelMethodParameters(aggregateMethod, valueObjects);
    this.dispatchToFactoryMethod = aggregateMethod.retrieveRelatedValue(Label.FACTORY_METHOD, Boolean::valueOf);
    this.localTypeQualifiedName = schema.qualifiedName();
  }

  private String resolveModelMethodParameters(final CodeGenerationParameter method, List<CodeGenerationParameter> valueObjects) {
    final boolean factoryMethod = method.retrieveRelatedValue(Label.FACTORY_METHOD, Boolean::valueOf);
    final MethodScope methodScope = factoryMethod ? MethodScope.STATIC : MethodScope.INSTANCE;

    CodeGenerationParameter stateField = AggregateDetail.stateFieldWithName(method.parent(), method.retrieveRelatedValue(Label.METHOD_PARAMETER));
    if(isNullObjectOrNotValueObject(stateField, valueObjects))
      return AggregateMethodInvocation.accessingParametersFromConsumedEvent("stage").format(method, methodScope);

    CodeGenerationParameter valueObject = ValueObjectDetail.valueObjectOf(stateField.retrieveRelatedValue(Label.FIELD_TYPE), valueObjects.stream());
    return AggregateMethodInvocation.accessingValueObjectParametersFromConsumedEvent("stage", valueObject).format(method, methodScope);
  }

  private boolean isNullObjectOrNotValueObject(final CodeGenerationParameter stateField,
                                               final List<CodeGenerationParameter> valueObjects) {
    if(stateField == null)
      return true;

    return valueObjects.stream()
        .noneMatch(vo -> vo.value.equals((stateField.retrieveRelatedValue(Label.FIELD_TYPE)))) && !ValueObjectDetail.isValueObject(stateField);
  }


}
