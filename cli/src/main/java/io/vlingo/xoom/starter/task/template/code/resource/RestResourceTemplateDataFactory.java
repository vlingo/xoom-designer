// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code.resource;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RestResourceTemplateDataFactory {

    private static final String REST_RESOURCES_SEPARATOR = ";";

    public static List<RestResourceTemplateData> build(final String basePackage,
                                                       final String projectPath,
                                                       final String restResourcesData) {
        final Function<String, RestResourceTemplateData> mapper =
                aggregateName -> new RestResourceTemplateData(aggregateName, basePackage, projectPath);

        return Arrays.asList(restResourcesData.split(REST_RESOURCES_SEPARATOR))
                .stream().map(mapper).collect(Collectors.toList());
    }

}