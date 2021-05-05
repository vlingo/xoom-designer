// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.formatting;

import io.vlingo.xoom.designer.task.projectgeneration.code.template.dataobject.EventBasedDataObjectInitializer;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.model.MethodScope;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.model.valueobject.ValueObjectInitializer;
import io.vlingo.xoom.turbo.codegen.language.Language;
import io.vlingo.xoom.turbo.codegen.parameter.CodeGenerationParameter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.*;

public class Formatters {

  public interface Arguments {

    Arguments AGGREGATE_METHOD_INVOCATION = new AggregateMethodInvocation("stage");
    Arguments QUERIES_METHOD_INVOCATION = new QueriesMethodInvocation();
    Arguments VALUE_OBJECT_CONSTRUCTOR_INVOCATION = new ValueObjectConstructorInvocation();
    Arguments DATA_OBJECT_CONSTRUCTOR = new DataObjectConstructor();
    Arguments DATA_OBJECT_CONSTRUCTOR_INVOCATION = new DataObjectConstructorInvocation();
    Arguments SOURCED_STATED_METHOD_INVOCATION = new SourcedStateMethodInvocation();
    Arguments SIGNATURE_DECLARATION = new SignatureDeclaration();

    default String format(final CodeGenerationParameter parameter) {
      return format(parameter, MethodScope.INSTANCE);
    }

    String format(final CodeGenerationParameter parameter, final MethodScope scope);
  }

  public abstract static class Variables<T> {

    public static <T> T format(final Variables.Style style,
                               final Language language,
                               final CodeGenerationParameter parent) {
      if (parent.isLabeled(AGGREGATE)) {
        return format(style, language, parent, parent.retrieveAllRelated(STATE_FIELD));
      } else if (parent.isLabeled(VALUE_OBJECT)) {
        return format(style, language, parent, parent.retrieveAllRelated(VALUE_OBJECT_FIELD));
      }
      throw new UnsupportedOperationException("Unable to format fields from " + parent.label);
    }

    @SuppressWarnings("unchecked")
    public static <T> T format(final Variables.Style style,
                               final Language language,
                               final CodeGenerationParameter parent,
                               final Stream<CodeGenerationParameter> fields) {
      final Function<Language, Variables<?>> instantiator = INSTANTIATORS.get(style);
      return (T) instantiator.apply(language).format(parent, fields);
    }

    protected abstract T format(final CodeGenerationParameter parameter, final Stream<CodeGenerationParameter> fields);

    public enum Style {
      VALUE_OBJECT_INITIALIZER, EVENT_BASED_DATA_OBJECT_INITIALIZER, DATA_OBJECT_STATIC_FACTORY_METHOD_ASSIGNMENT
    }

    @SuppressWarnings("serial")
    private static Map<Style, Function<Language, Variables<?>>> INSTANTIATORS = Collections.unmodifiableMap(
            new HashMap<Style, Function<Language, Variables<?>>>() {{
              put(Variables.Style.VALUE_OBJECT_INITIALIZER, lang -> new ValueObjectInitializer("data"));
              put(Variables.Style.EVENT_BASED_DATA_OBJECT_INITIALIZER, lang -> new EventBasedDataObjectInitializer("typedEvent"));
              put(Variables.Style.DATA_OBJECT_STATIC_FACTORY_METHOD_ASSIGNMENT, language -> new DataObjectStaticFactoryMethodAssignment());
            }});
  }

  public abstract static class Fields<T> {

    public static <T> T format(final Style style,
                               final Language language,
                               final CodeGenerationParameter parent) {
      if (parent.isLabeled(AGGREGATE)) {
        return format(style, language, parent, parent.retrieveAllRelated(STATE_FIELD));
      } else if (parent.isLabeled(VALUE_OBJECT)) {
        return format(style, language, parent, parent.retrieveAllRelated(VALUE_OBJECT_FIELD));
      }
      throw new UnsupportedOperationException("Unable to format fields from " + parent.label);
    }

    @SuppressWarnings("unchecked")
    public static <T> T format(final Style style,
                               final Language language,
                               final CodeGenerationParameter parent,
                               final Stream<CodeGenerationParameter> fields) {
      final Function<Language, Fields<?>> instantiator = INSTANTIATORS.get(style);
      return (T) instantiator.apply(language).format(parent, fields);
    }

    protected abstract T format(final CodeGenerationParameter parameter, final Stream<CodeGenerationParameter> fields);

    public enum Style {
      ASSIGNMENT, MEMBER_DECLARATION, DATA_OBJECT_MEMBER_DECLARATION, DATA_VALUE_OBJECT_ASSIGNMENT,
      STATE_BASED_ASSIGNMENT, SELF_ALTERNATE_REFERENCE, ALTERNATE_REFERENCE_WITH_DEFAULT_VALUE
    }

    @SuppressWarnings("serial")
    private static Map<Style, Function<Language, Fields<?>>> INSTANTIATORS = Collections.unmodifiableMap(
            new HashMap<Style, Function<Language, Fields<?>>>() {{
              put(Style.ASSIGNMENT, lang -> new DefaultConstructorMembersAssignment());
              put(Style.MEMBER_DECLARATION, lang -> new Member(lang));
              put(Style.DATA_OBJECT_MEMBER_DECLARATION, lang -> new Member(lang, "Data"));
              put(Style.STATE_BASED_ASSIGNMENT, lang -> new DefaultConstructorMembersAssignment("state"));
              put(Style.DATA_VALUE_OBJECT_ASSIGNMENT, lang -> new DataObjectConstructorAssignment());
              put(Style.SELF_ALTERNATE_REFERENCE, lang -> AlternateReference.handlingSelfReferencedFields());
              put(Style.ALTERNATE_REFERENCE_WITH_DEFAULT_VALUE, lang -> AlternateReference.handlingDefaultFieldsValue());
            }});
  }
}
