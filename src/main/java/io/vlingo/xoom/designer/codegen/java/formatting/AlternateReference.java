// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java.formatting;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.CollectionMutation;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.model.FieldDetail;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class AlternateReference extends Formatters.Fields<String> {

  private final boolean supportSelfReferencedFields;
  private final Function<CodeGenerationParameter, String> absenceHandler;

  private AlternateReference(final Function<CodeGenerationParameter, String> absenceHandler) {
    this(absenceHandler, true);
  }

  private AlternateReference(final Function<CodeGenerationParameter, String> absenceHandler,
                             final boolean supportSelfReferencedFields) {
    this.absenceHandler = absenceHandler;
    this.supportSelfReferencedFields = supportSelfReferencedFields;
  }

  static AlternateReference handlingSelfReferencedFields() {
    return new AlternateReference(field -> "this." + field.value);
  }

  static AlternateReference handlingDefaultFieldsValue() {
    return new AlternateReference(field -> FieldDetail.resolveDefaultValue(field.parent(Label.AGGREGATE), field.value));
  }

  @Override
  public String format(final CodeGenerationParameter param,
                       final Stream<CodeGenerationParameter> fields) {
    final List<CodeGenerationParameter> presentFields = fields.collect(toList());

    final Function<CodeGenerationParameter, String> mapper = field ->
            isPresent(field, presentFields) ? field.value : absenceHandler.apply(field);

    return param.retrieveAllRelated(Label.STATE_FIELD).map(mapper).collect(Collectors.joining(", "));
  }

  private boolean isPresent(final CodeGenerationParameter field,
                            final List<CodeGenerationParameter> presentFields) {

    return presentFields.stream().anyMatch(present -> {

      final CollectionMutation collectionMutation =
              present.retrieveRelatedValue(Label.COLLECTION_MUTATION, CollectionMutation::withName);

      if(supportSelfReferencedFields && !collectionMutation.shouldReplaceWithMethodParameter()) {
        return false;
      }

      return present.value.equals(field.value);
    });
  }
}
