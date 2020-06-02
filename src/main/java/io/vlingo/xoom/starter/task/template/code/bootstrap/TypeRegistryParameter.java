// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code.bootstrap;

import io.vlingo.xoom.starter.task.template.code.storage.StorageType;

import java.util.List;
import java.util.stream.Collectors;

public class TypeRegistryParameter {

    private final String className;
    private final String objectName;

    public static List<TypeRegistryParameter> from(final StorageType storageType, final Boolean useCQRS) {
        return storageType.findRelatedStorageTypes(useCQRS)
                .map(relatedStorageType ->
                        new TypeRegistryParameter(relatedStorageType.typeRegistryClassName,
                                relatedStorageType.typeRegistryObjectName())
                ).collect(Collectors.toList());
    }

    private TypeRegistryParameter(String className, String objectName) {
        this.className = className;
        this.objectName = objectName;
    }

    public String getClassName() {
        return className;
    }

    public String getObjectName() {
        return objectName;
    }

}
