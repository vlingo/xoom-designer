// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.bootstrap;

import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.content.ContentQuery;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class RestResource {

  public final String className;
  public final String objectName;
  public final boolean last;

  public static List<RestResource> from(final List<Content> contents) {

    final Set<String> classNames =
            ContentQuery.findClassNames(contents, JavaTemplateStandard.REST_RESOURCE, JavaTemplateStandard.REST_UI_RESOURCE,
                    JavaTemplateStandard.AUTO_DISPATCH_RESOURCE_HANDLER, JavaTemplateStandard.AUTO_DISPATCH_RESOURCE_UI_HANDLER);

    final Iterator<String> iterator = classNames.iterator();

    final CodeElementFormatter codeElementFormatter = ComponentRegistry.withName("defaultCodeFormatter");

    return IntStream.range(0, classNames.size()).mapToObj(index ->
            new RestResource(codeElementFormatter, iterator.next(), index,
                    classNames.size())).collect(toList());
  }

  private RestResource(final CodeElementFormatter codeElementFormatter,
                       final String restResourceName,
                       final int resourceIndex,
                       final int numberOfResources) {
    this.className = restResourceName;
    this.objectName = codeElementFormatter.simpleNameToAttribute(restResourceName);
    this.last = resourceIndex == numberOfResources - 1;
  }

}
