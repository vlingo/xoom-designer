// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cli.option;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OptionValue {

  private final OptionName name;
  private final String value;

  public static Map<String, String> mapValues(final List<Option> options,
                                              final List<String> args) {
    return resolveValues(options, args).stream().collect(Collectors.toMap(value -> value.name.value(), OptionValue::value));
  }


  public static List<OptionValue> resolveValues(final List<Option> options,
                                                final List<String> args) {
    return options.stream().map(option -> resolveValue(option, args)).collect(Collectors.toList());
  }

  public static OptionValue resolveValue(final Option option,
                                         final List<String> args) {
    return OptionValue.with(option.name(), option.findValue(args));
  }

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