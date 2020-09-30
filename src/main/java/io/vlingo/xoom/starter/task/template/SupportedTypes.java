// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public enum SupportedTypes {

    INTEGER("int"),
    LONG("Long"),
    FLOAT("Float"),
    DOUBLE("Double"),
    STRING("String"),
    BOOLEAN("Boolean");

    private final String publicName;

    SupportedTypes(final String publicName) {
        this.publicName = publicName;
    }

    public static List<String> names(){
        return Stream.of(values()).map(type -> type.publicName).collect(toList());
    }
}
