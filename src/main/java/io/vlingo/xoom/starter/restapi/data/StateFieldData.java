// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.restapi.data;

public class StateFieldData {

    public final String name;
    public final String type;

    public StateFieldData(final String name,
                          final String type) {
        this.name = name;
        this.type = type;
    }
}
