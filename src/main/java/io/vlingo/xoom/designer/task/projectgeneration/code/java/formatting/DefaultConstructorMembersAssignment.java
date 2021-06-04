// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.java.formatting;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.task.projectgeneration.CollectionMutation;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.FieldDetail;

import java.util.List;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.task.projectgeneration.Label.ALIAS;
import static io.vlingo.xoom.designer.task.projectgeneration.Label.COLLECTION_MUTATION;
import static java.util.stream.Collectors.toList;

public class DefaultConstructorMembersAssignment extends Formatters.Fields<List<String>> {

  @Override
  public List<String> format(final CodeGenerationParameter aggregate,
                             final Stream<CodeGenerationParameter> fields) {
    return fields.map(field -> {
      final CollectionMutation collectionMutation =
              field.retrieveRelatedValue(COLLECTION_MUTATION, CollectionMutation::withName);

      if(FieldDetail.isCollection(field) && !collectionMutation.isSingleParameterBased()) {
        return String.format("this.%s.addAll(%s);", field.value, field.value);
      }

      final String fieldAlias =
              field.hasAny(ALIAS) ? field.retrieveRelatedValue(ALIAS) : field.value;

      return String.format("this.%s = %s;", fieldAlias, fieldAlias);
    }).collect(toList());
  }

}
