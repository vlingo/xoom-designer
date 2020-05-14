// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.codegeneration;

import java.util.ArrayList;
import java.util.List;

public class JournalProviderGenerator extends ImportsGenerator {
  protected final List<EntryAdapterHolder> entryAdaptersHolder;

  public JournalProviderGenerator() {
    super();

    this.entryAdaptersHolder = new ArrayList<>();
    input.put("entryAdapters", entryAdaptersHolder);
  }

  public void inputOf(final EntryAdapterHolder entryAdapterHolder) {
    entryAdaptersHolder.add(entryAdapterHolder);
  }

  public void inputOfJournalStoreClassName(final String journalStoreClassName) {
    input.put("journalStoreClassName", journalStoreClassName);
  }
}
