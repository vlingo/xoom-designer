// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.option;

public enum OptionName {

    TAG("tag"),
    CURRENT_DIRECTORY("currentDirectory"),
    PUBLISHER("publisher");

    private final String name;

    OptionName(final String name) {
        this.name = name;
    }

    public String literal() {
        return name;
    }

    public String withPreffix() {
        return "--" + name;
    }

}
