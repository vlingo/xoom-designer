// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.bootstrap;

import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.content.ContentQuery;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard.AUTO_DISPATCH_RESOURCE_HANDLER;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard.REST_RESOURCE;
import static java.util.stream.Collectors.toList;

public class RestResource {

  private final String className;
  private final String objectName;
  private final boolean last;

  public static List<RestResource> from(final List<Content> contents) {
    final Set<String> classNames =
            ContentQuery.findClassNames(contents, REST_RESOURCE,
                    AUTO_DISPATCH_RESOURCE_HANDLER);

    final Iterator<String> iterator = classNames.iterator();

    return IntStream.range(0, classNames.size()).mapToObj(index ->
            new RestResource(iterator.next(), index,
                    classNames.size())).collect(toList());
  }

  private RestResource(final String restResourceName,
                       final int resourceIndex,
                       final int numberOfResources) {
    this.className = restResourceName;
    this.objectName = CodeElementFormatter.simpleNameToAttribute(restResourceName);
    this.last = resourceIndex == numberOfResources - 1;
  }

  public String getClassName() {
    return className;
  }

  public String getObjectName() {
    return objectName;
  }

  public boolean isLast() {
    return last;
  }

}
