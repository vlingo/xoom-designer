// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.restapi.data;

public class ReceiverData {

    public final String schema;
    public final String aggregateMethod;

    public ReceiverData(final String schema, final String aggregateMethod) {
        this.schema = schema;
        this.aggregateMethod = aggregateMethod;
    }
}
