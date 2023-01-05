// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.restapi.data;

import java.util.ArrayList;
import java.util.List;

public class DomainEventData {

    public final String name;
    public final List<String> fields = new ArrayList<>();

    public DomainEventData(final String name,
                           final List<String> fields) {
        this.name = name;
        this.fields.addAll(fields);
    }

    public List<String> validate(List<String> errorStrings) {
        if(name==null) errorStrings.add("DomainEventData.name is null");
        if(fields==null) errorStrings.add("DomainEventData.fields is null");
        return errorStrings;
    }
}
