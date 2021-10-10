// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cli.option;

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

  public static Option of(final OptionName name) {
    return new Option(name, "", false);
  }

  public static Option of(final OptionName name, final String defaultValue) {
    return new Option(name, defaultValue, false);
  }

  public static Option required(final OptionName name) {
    return new Option(name, null, true);
  }

  public String findValue(final List<String> args) {
    final int index = args.indexOf(name.withPrefix());
    if(index > 0 && index + 1 < args.size()) {
      final String value = args.get(index+1);
      if(value != null) {
        return value;
      }
    }
    if (isRequired()) {
      throw new RequiredOptionNotFoundException(name.value());
    }
    return defaultValue;
  }

  public OptionName name() {
    return name;
  }

  private boolean isRequired() {
    return required;
  }

}
