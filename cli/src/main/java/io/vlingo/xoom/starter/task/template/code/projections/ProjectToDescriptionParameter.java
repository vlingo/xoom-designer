// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code.projections;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectToDescriptionParameter {

    private final String projectionClassName;
    private final String joinedTypes;

    public ProjectToDescriptionParameter(final String projectionClassName, final List<String> sourceTypes) {
        this.projectionClassName = projectionClassName;
        this.joinedTypes = sourceTypes.stream().collect(Collectors.joining(", "));
    }

    public String getInitializationCommand() {
        return MessageFormat.format("ProjectToDescription.with({0}.class, {1})", projectionClassName, joinedTypes);
    }

}
