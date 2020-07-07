// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code.bootstrap;

import io.vlingo.xoom.starter.task.Content;
import io.vlingo.xoom.starter.task.ContentQuery;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.IntStream;

import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.REST_RESOURCE;
import static java.util.stream.Collectors.toList;

public class RestResourcesParameter {

    private final String className;
    private final String objectName;
    private final boolean last;

    public static List<RestResourcesParameter> from(final List<Content> contents) {
        final List<String> classNames =
                ContentQuery.findClassNames(REST_RESOURCE, contents);

        return IntStream.range(0, classNames.size()).mapToObj(index ->
                new RestResourcesParameter(classNames.get(index), index,
                        classNames.size())).collect(toList());
    }

    private RestResourcesParameter(final String restResourceName,
                                  final int resourceIndex,
                                  final int numberOfResources) {
        this.className = restResourceName;
        this.objectName = formatObjectName(restResourceName);
        this.last = resourceIndex == numberOfResources - 1;
    }

    private String formatObjectName(final String restResourceName) {
        return StringUtils.uncapitalize(restResourceName);
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
