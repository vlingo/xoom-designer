// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code.bootstrap;

import io.vlingo.xoom.starter.task.Content;
import io.vlingo.xoom.starter.task.ContentQuery;

import java.util.List;
import java.util.stream.Collectors;

import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.REST_RESOURCE;

public class RestResourceParameter {

    private final String classNames;
    private final boolean exist;

    public RestResourceParameter(final List<String> classNames) {
        this.exist = !classNames.isEmpty();
        this.classNames = classNames.stream()
                .map(className -> className.concat(".class"))
                .collect(Collectors.joining(", "));
    }

    public static RestResourceParameter from(final List<Content> contents) {
        final List<String> classNames =
                ContentQuery.findClassNames(REST_RESOURCE, contents);

        return new RestResourceParameter(classNames);
    }

    public String getClassNames() {
        return classNames;
    }

    public boolean getExist() {
        return exist;
    }
}
