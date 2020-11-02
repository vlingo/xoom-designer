// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.restapi.data;

import java.util.List;

public class APIData {

    public final String rootPath;
    public final List<RouteData> routes;

    public APIData(final String rootPath,
                   final List<RouteData> routes) {
        this.rootPath = rootPath;
        this.routes = routes;
    }
}
