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
import io.vlingo.xoom.designer.codegen.csharp.model.MethodScope;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class Formatters {

  public interface Arguments {
    Arguments AGGREGATE_METHOD_INVOCATION = new AggregateMethodInvocation("stage");
    Arguments SIGNATURE_DECLARATION = new SignatureDeclaration();

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
      throw new UnsupportedOperationException("Unable to format fields from " + parent.label);
    }

    @SuppressWarnings("unchecked")
    public static <T> T format(final Style style, final Dialect dialect, final CodeGenerationParameter parent,
                               final Stream<CodeGenerationParameter> fields) {
      final Function<Dialect, Variables<?>> instantiator = INSTANTIATORS.get(style);
      return (T) instantiator.apply(dialect).format(parent, fields);
    }

    protected abstract T format(final CodeGenerationParameter parameter, final Stream<CodeGenerationParameter> fields);

    public enum Style {}

    @SuppressWarnings("serial")
    private static final Map<Style, Function<Dialect, Variables<?>>> INSTANTIATORS = Collections.unmodifiableMap(
        new HashMap<Style, Function<Dialect, Variables<?>>>() {});
  }

  public abstract static class Fields<T> {
    public static <T> T format(final Style style, final Dialect dialect, final CodeGenerationParameter parent) {
      if (parent.isLabeled(Label.AGGREGATE) || parent.isLabeled(Label.DOMAIN_EVENT)) {
        return format(style, dialect, parent, parent.retrieveAllRelated(Label.STATE_FIELD));
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
      ASSIGNMENT, MEMBER_DECLARATION, SELF_ALTERNATE_REFERENCE, ALTERNATE_REFERENCE_WITH_DEFAULT_VALUE
    }

    @SuppressWarnings("serial")
    private static final Map<Style, Function<Dialect, Fields<?>>> INSTANTIATORS = Collections.unmodifiableMap(
        new HashMap<Style, Function<Dialect, Fields<?>>>() {{
          put(Style.ASSIGNMENT, lang -> new DefaultConstructorMembersAssignment());
          put(Style.MEMBER_DECLARATION, lang -> new Member(lang));
          put(Style.SELF_ALTERNATE_REFERENCE, lang -> AlternateReference.handlingSelfReferencedFields());
          put(Style.ALTERNATE_REFERENCE_WITH_DEFAULT_VALUE, lang -> AlternateReference.handlingDefaultFieldsValue());
        }});
  }
}
