// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.option;

import io.vlingo.xoom.designer.ArgumentNotFoundException;
import io.vlingo.xoom.designer.ArgumentRetriever;

import java.util.List;

public class Option {

    private final OptionName name;
    private final String defaultValue;
    private final boolean required;

    public Option(final OptionName name,
                  final String defaultValue,
                  final boolean required) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.required = required;
    }

    public static final Option of(final OptionName name, final String defaultValue) {
        return new Option(name, defaultValue, false);
    }

    public static final Option required(final OptionName name) {
        return new Option(name, null, true);
    }

    public String findValue(final List<String> args) {
        try {
            return ArgumentRetriever.retrieve(name.withPreffix(), args);
        } catch(final ArgumentNotFoundException exception) {
            if(isRequired()) {
                throw new RequiredOptionNotFoundException(name.literal());
            }
            return defaultValue;
        }
    }

    public OptionName name() {
        return name;
    }

    private boolean isRequired() {
        return required;
    }

}
