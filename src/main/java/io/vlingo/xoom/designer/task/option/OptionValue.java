// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.option;

public class OptionValue {

    private final OptionName name;
    private final String value;

    public static OptionValue with(final OptionName name, final String value) {
        return new OptionValue(name, value);
    }

    private OptionValue(final OptionName name, final String value) {
        this.name = name;
        this.value = value;
    }

    public boolean hasName(final OptionName name) {
        return this.name.equals(name);
    }

    public String value() {
        return value;
    }
}