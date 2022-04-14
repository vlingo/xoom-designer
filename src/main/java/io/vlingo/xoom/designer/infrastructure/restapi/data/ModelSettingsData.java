// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.restapi.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModelSettingsData {

    public final PersistenceData persistenceSettings;
    public final List<AggregateData> aggregateSettings = new ArrayList<>();
    public final List<ValueObjectData> valueObjectSettings = new ArrayList<>();

    public ModelSettingsData(final PersistenceData persistenceSettings,
                             final List<AggregateData> aggregateSettings,
                             final List<ValueObjectData> valueObjectSettings) {
        this.persistenceSettings = persistenceSettings;
        this.aggregateSettings.addAll(aggregateSettings);
        this.valueObjectSettings.addAll(valueObjectSettings);
    }

    public List<String> validate(final List<String> errorStrings) {
        if(persistenceSettings==null) {
            errorStrings.add("ModelSettingsData.persistence is null");
        } else {
            persistenceSettings.validate(errorStrings);
        }
        if(aggregateSettings==null) {
            errorStrings.add("ModelSettingsData.aggregateSettings is null");
        } else {
            aggregateSettings.forEach(aggregateSetting ->
                aggregateSetting.validate(errorStrings)
            );
        }
        if(valueObjectSettings != null && !valueObjectSettings.isEmpty()) {
            final Map<String, List<ValueObjectFieldData>> fieldsByType =
                    valueObjectSettings.stream().collect(Collectors.toMap(vo -> vo.name, vo -> vo.fields));

            for(final ValueObjectData vo : valueObjectSettings) {
                try {
                    validateValueObjectsRecursion(vo.name, vo.fields, fieldsByType);
                } catch (final IllegalStateException ex) {
                    errorStrings.add(ex.getMessage());
                    break;
                }
            }
        }
        return errorStrings;
    }

    private void validateValueObjectsRecursion(final String parentValueObjectType,
                                               final List<ValueObjectFieldData> fields,
                                               final Map<String, List<ValueObjectFieldData>> fieldsByType) {
        for(final ValueObjectFieldData field : fields) {
            if (parentValueObjectType.equals(field.type)) {
                throw new IllegalStateException("Recursive Value Object relationship");
            } else if (fieldsByType.containsKey(field.type)) {
                validateValueObjectsRecursion(parentValueObjectType, fieldsByType.get(field.type), fieldsByType);
            }
        }
    }
}
