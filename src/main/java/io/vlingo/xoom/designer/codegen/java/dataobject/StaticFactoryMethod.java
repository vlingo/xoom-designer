// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.dataobject;

import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.formatting.Formatters;

import java.beans.Introspector;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.vlingo.xoom.designer.codegen.java.model.MethodScope.INSTANCE;
import static io.vlingo.xoom.designer.codegen.java.model.MethodScope.STATIC;

public class StaticFactoryMethod {

  public final String parameters;
  public final String dataObjectName;
  public final String constructorInvocation;
  public final String singleArgumentName;
  public final List<String> valueObjectInitializers = new ArrayList<>();

  public static List<StaticFactoryMethod> from(final CodeGenerationParameter parent) {
    return Arrays.asList(new StaticFactoryMethod(parent, Arguments.SINGLE),
            new StaticFactoryMethod(parent, Arguments.ALL));
  }

  private StaticFactoryMethod(final CodeGenerationParameter parent,
                              final Arguments staticFactoryMethodArguments) {
    this.dataObjectName = JavaTemplateStandard.DATA_OBJECT.resolveClassname(parent.value);
    this.parameters = resolveMethodParameters(parent, staticFactoryMethodArguments);
    this.constructorInvocation = resolveConstructorInvocation(parent, staticFactoryMethodArguments);
    this.valueObjectInitializers.addAll(resolveValueObjectInitializers(parent, staticFactoryMethodArguments));
    this.singleArgumentName = resolveSingleArgumentName(parent, staticFactoryMethodArguments);
  }

  private String resolveMethodParameters(final CodeGenerationParameter parent,
                                         final Arguments staticFactoryMethodArguments) {
    if (staticFactoryMethodArguments.isSingleArg()) {
      final String carrier = resolveCarrierName(parent);
      return String.format("final %s %s", carrier, Introspector.decapitalize(carrier));
    }
    return Formatters.Arguments.DATA_OBJECT_CONSTRUCTOR.format(parent);
  }

  private String resolveConstructorInvocation(final CodeGenerationParameter parent,
                                              final Arguments staticFactoryMethodArguments) {
    if (staticFactoryMethodArguments.isSingleArg()) {
      return String.format("from(%s)", Formatters.Arguments.DATA_OBJECT_CONSTRUCTOR_INVOCATION.format(parent, STATIC));
    }
    return String.format("new %s(%s)", dataObjectName, Formatters.Arguments.DATA_OBJECT_CONSTRUCTOR_INVOCATION.format(parent, INSTANCE));
  }

  private List<String> resolveValueObjectInitializers(final CodeGenerationParameter parent,
                                                      final Arguments staticFactoryMethodArguments) {
    if (staticFactoryMethodArguments.isAllArgs()) {
      return Collections.emptyList();
    }
    return Formatters.Variables.format(Formatters.Variables.Style.DATA_OBJECT_STATIC_FACTORY_METHOD_ASSIGNMENT, Dialect.findDefault(), parent);
  }

  private String resolveCarrierName(final CodeGenerationParameter parent) {
    if (parent.isLabeled(Label.AGGREGATE)) {
      return JavaTemplateStandard.AGGREGATE_STATE.resolveClassname(parent.value);
    }
    if (parent.isLabeled(Label.VALUE_OBJECT)) {
      return parent.value;
    }
    throw new IllegalArgumentException("Unable to resolve carrier name from " + parent.label);
  }

  private String resolveSingleArgumentName(final CodeGenerationParameter parent,
                                           final Arguments staticFactoryMethodArguments) {
    if (staticFactoryMethodArguments.isSingleArg()) {
      return Introspector.decapitalize(resolveCarrierName(parent));
    }
    return "";
  }

  private enum Arguments {
    SINGLE,
    ALL;

    boolean isSingleArg() {
      return equals(SINGLE);
    }

    boolean isAllArgs() {
      return equals(ALL);
    }
  }

}
