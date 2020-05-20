// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.codegeneration;

public class EntryAdapterHolder {
  private final String entryAdapterClass;
  private final String sourceClass;

  public EntryAdapterHolder(final String sourceClass) {
    this.sourceClass = sourceClass;
    this.entryAdapterClass = sourceClass + "Adapter";
  }

  public String getEntryAdapterClass() {
    return entryAdapterClass;
  }

  public String getSourceClass() {
    return sourceClass;
  }
}
