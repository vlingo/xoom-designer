// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.csharp.formatting;

import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.MethodScope;
import io.vlingo.xoom.designer.codegen.csharp.dataobject.EventBasedDataObjectInitializer;
import io.vlingo.xoom.designer.codegen.csharp.model.valueobject.ValueObjectInitializer;
import io.vlingo.xoom.designer.codegen.java.formatting.DataObjectDetail;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.codegen.csharp.DataObjectDetail.DATA_OBJECT_NAME_SUFFIX;

public class Formatters {

  public interface Arguments {
    Arguments AGGREGATE_METHOD_INVOCATION = new AggregateMethodInvocation("stage");
    Arguments SIGNATURE_DECLARATION = new SignatureDeclaration();
    Arguments VALUE_OBJECT_CONSTRUCTOR_INVOCATION = new ValueObjectConstructorInvocation();
    Arguments DATA_OBJECT_CONSTRUCTOR = new DataObjectConstructor();
    Arguments DATA_OBJECT_CONSTRUCTOR_INVOCATION = new DataObjectConstructorInvocation();
    Arguments SOURCED_STATED_METHOD_INVOCATION = new SourcedStateMethodInvocation();
    Arguments DOMAIN_EVENT_CONSTRUCTOR_INVOCATION = new DomainEventConstructorInvocation("_state");
    Arguments QUERIES_METHOD_INVOCATION = new QueriesMethodInvocation();

    default String format(final CodeGenerationParameter parameter) {
      return format(parameter, MethodScope.INSTANCE);
    }

    String format(final CodeGenerationParameter parameter, final MethodScope scope);
  }

  public abstract static class Variables<T> {
    public static <T> T format(final Style style, final Dialect dialect, final CodeGenerationParameter parent) {
      if (parent.isLabeled(Label.AGGREGATE) || parent.isLabeled(Label.DOMAIN_EVENT)) {
        return format(style, dialect, parent, parent.retrieveAllRelated(Label.STATE_FIELD));
      }
      else if (parent.isLabeled(Label.VALUE_OBJECT)) {
        return format(style, dialect, parent, parent.retrieveAllRelated(Label.VALUE_OBJECT_FIELD));
      }
      throw new UnsupportedOperationException("Unable to format fields from " + parent.label);
    }

    @SuppressWarnings("unchecked")
    public static <T> T format(final Style style, final Dialect dialect, final CodeGenerationParameter parent,
                               final Stream<CodeGenerationParameter> fields) {
      final Function<Dialect, Variables<?>> instantiator = INSTANTIATORS.get(style);
      return (T) instantiator.apply(dialect).format(parent, fields);
    }

    protected abstract T format(final CodeGenerationParameter parameter, final Stream<CodeGenerationParameter> fields);

    public enum Style {
      DATA_TO_VALUE_OBJECT_TRANSLATION, DATA_OBJECT_STATIC_FACTORY_METHOD_ASSIGNMENT, EVENT_BASED_DATA_OBJECT_INITIALIZER, VALUE_OBJECT_INITIALIZER
    }

    private static final Map<Style, Function<Dialect, Variables<?>>> INSTANTIATORS = Collections.unmodifiableMap(
        new HashMap<Style, Function<Dialect, Variables<?>>>() {{
          put(Variables.Style.DATA_TO_VALUE_OBJECT_TRANSLATION, lang -> new ValueObjectInitializer("this"));
          put(Variables.Style.DATA_OBJECT_STATIC_FACTORY_METHOD_ASSIGNMENT, dialect -> new DataObjectStaticFactoryMethodAssignment());
          put(Variables.Style.EVENT_BASED_DATA_OBJECT_INITIALIZER, lang -> new EventBasedDataObjectInitializer("typedEvent"));
          put(Variables.Style.VALUE_OBJECT_INITIALIZER, lang -> new ValueObjectInitializer(DataObjectDetail.DATA_OBJECT_NAME_SUFFIX.toLowerCase()));
        }});
  }

  public abstract static class Fields<T> {
    public static <T> T format(final Style style, final Dialect dialect, final CodeGenerationParameter parent) {
      if (parent.isLabeled(Label.AGGREGATE) || parent.isLabeled(Label.DOMAIN_EVENT)) {
        return format(style, dialect, parent, parent.retrieveAllRelated(Label.STATE_FIELD));
      } else if (parent.isLabeled(Label.VALUE_OBJECT)) {
        return format(style, dialect, parent, parent.retrieveAllRelated(Label.VALUE_OBJECT_FIELD));
      }
      throw new UnsupportedOperationException("Unable to format fields from " + parent.label);
    }

    @SuppressWarnings("unchecked")
    public static <T> T format(final Style style, final Dialect dialect, final CodeGenerationParameter parent,
                               final Stream<CodeGenerationParameter> fields) {
      final Function<Dialect, Fields<?>> instantiator = INSTANTIATORS.get(style);
      return (T) instantiator.apply(dialect).format(parent, fields);
    }

    protected abstract T format(final CodeGenerationParameter parameter, final Stream<CodeGenerationParameter> fields);

    public enum Style {
      ASSIGNMENT, MEMBER_DECLARATION, DATA_OBJECT_MEMBER_DECLARATION, DATA_VALUE_OBJECT_ASSIGNMENT, SELF_ALTERNATE_REFERENCE, ALTERNATE_REFERENCE_WITH_DEFAULT_VALUE
    }

    private static final Map<Style, Function<Dialect, Fields<?>>> INSTANTIATORS = Collections.unmodifiableMap(
        new HashMap<Style, Function<Dialect, Fields<?>>>() {{
          put(Style.ASSIGNMENT, lang -> new DefaultConstructorMembersAssignment());
          put(Style.MEMBER_DECLARATION, Member::new);
          put(Style.DATA_OBJECT_MEMBER_DECLARATION, lang -> new DataMember(lang, DATA_OBJECT_NAME_SUFFIX));
          put(Style.DATA_VALUE_OBJECT_ASSIGNMENT, lang -> new DataObjectConstructorAssignment());
          put(Style.SELF_ALTERNATE_REFERENCE, lang -> AlternateReference.handlingSelfReferencedFields());
          put(Style.ALTERNATE_REFERENCE_WITH_DEFAULT_VALUE, lang -> AlternateReference.handlingDefaultFieldsValue());
        }});
  }
}
