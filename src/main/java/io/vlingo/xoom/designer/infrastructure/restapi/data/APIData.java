// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.restapi.data;

import java.util.List;

public class APIData {

    public final String rootPath;
    public final List<RouteData> routes;

    public APIData(final String rootPath,
                   final List<RouteData> routes) {
        this.rootPath = rootPath;
        this.routes = routes;
    }

    public List<String> validate(List<String> errorStrings) {
        if(rootPath==null) errorStrings.add("APIData.rootPath is null");
        if(routes==null) {
            errorStrings.add("APIData.routes is null");
        } else {
            routes.forEach(route ->
                route.validate(errorStrings)
            );
        }
        return errorStrings;
    }
}
