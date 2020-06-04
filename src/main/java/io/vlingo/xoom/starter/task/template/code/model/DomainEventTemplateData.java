// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code.model;

import io.vlingo.xoom.starter.task.template.code.CodeTemplateParameters;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard;
import io.vlingo.xoom.starter.task.template.code.TemplateData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter.*;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.DOMAIN_EVENT;

public class DomainEventTemplateData extends TemplateData {

    private static final String PLACEHOLDER_EVENT_NAME_SUFFIX = "PlaceholderDefined";

    private final String name;
    private final String absolutePath;
    private final CodeTemplateParameters parameters;

    public static List<TemplateData> from(final String aggregateProtocolName,
                                          final String packageName,
                                          final String projectPath,
                                          final List<String> eventsNames) {
        final String defaultPlaceholderEventName =
                aggregateProtocolName + PLACEHOLDER_EVENT_NAME_SUFFIX;

        final List<TemplateData> eventsTemplateData = new ArrayList<>();

        eventsTemplateData.add(new DomainEventTemplateData(packageName,
                aggregateProtocolName, projectPath, true));

        eventsTemplateData.addAll(eventsNames.stream().map(eventName ->
                new DomainEventTemplateData(packageName, eventName,
                        projectPath, false))
                .collect(Collectors.toList()));

        return eventsTemplateData;
    }

    private DomainEventTemplateData(final String packageName,
                                    final String domainEventName,
                                    final String projectPath,
                                    final Boolean placeholder) {
        this.name = domainEventName;
        this.absolutePath = resolveAbsolutePath(packageName, projectPath);
        this.parameters =
                CodeTemplateParameters.with(PACKAGE_NAME, packageName).and(PLACEHOLDER_EVENT, placeholder)
                        .andResolve(DOMAIN_EVENT_NAME, param -> DOMAIN_EVENT.resolveClassname(domainEventName, param));
    }

    @Override
    public CodeTemplateParameters parameters() {
        return parameters;
    }

    public File file() {
        return buildFile(absolutePath, name);
    }

    @Override
    public CodeTemplateStandard standard() {
        return DOMAIN_EVENT;
    }

}
