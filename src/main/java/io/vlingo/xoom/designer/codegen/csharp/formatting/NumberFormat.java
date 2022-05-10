// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.csharp.formatting;

import com.ibm.icu.text.RuleBasedNumberFormat;

import static com.ibm.icu.text.RuleBasedNumberFormat.SPELLOUT;
import static java.util.Locale.ENGLISH;

public class NumberFormat {

  public static String toOrdinal(final int number) {
    return new RuleBasedNumberFormat(ENGLISH, SPELLOUT).format(number, "%spellout-ordinal");
  }
}
