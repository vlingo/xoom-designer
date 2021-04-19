// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ComponentRegistry {

  private static final Map<String, Object> COMPONENTS = new HashMap<>();

  public static void register(final Class<?> componentClass, final Object componentInstance) {
    COMPONENTS.put(componentClass.getCanonicalName(), componentInstance);
  }

  public static void register(final String componentName, final Object componentInstance) {
    COMPONENTS.put(componentName, componentInstance);
  }

  public static <T> T withType(final Class<T> componentClass) {
    return withName(componentClass.getCanonicalName());
  }

  public static <T> T withName(final String name) {
    return (T) COMPONENTS.get(name);
  }

}
