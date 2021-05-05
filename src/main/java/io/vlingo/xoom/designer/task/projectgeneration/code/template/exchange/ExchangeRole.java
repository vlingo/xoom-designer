// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.exchange;

import freemarker.template.utility.StringUtil;

public enum ExchangeRole {

  CONSUMER,
  PRODUCER;

  public static ExchangeRole of(final String roleName) {
    return valueOf(roleName.toUpperCase());
  }

  public boolean isConsumer() {
    return equals(CONSUMER);
  }

  public boolean isProducer() {
    return equals(PRODUCER);
  }

  public String formatName() {
    return StringUtil.capitalize(name().toLowerCase());
  }
}
