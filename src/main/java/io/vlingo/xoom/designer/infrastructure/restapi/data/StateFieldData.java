// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.restapi.data;

import java.util.List;

public class StateFieldData {

    public final String name;
    public final String type;
    public final String collectionType;

    public StateFieldData(final String name,
                          final String type,
                          final String collectionType) {
        this.name = name;
        this.type = type;
        this.collectionType = collectionType;
    }

    public List<String> validate(List<String> errorStrings) {
        if(name==null) errorStrings.add("StateFieldData.name is null");
        if(type==null) errorStrings.add("StateFieldData.type is null");
        return errorStrings;
    }
}
