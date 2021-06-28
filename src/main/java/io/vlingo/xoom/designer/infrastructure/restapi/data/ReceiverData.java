// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.restapi.data;

import java.util.ArrayList;
import java.util.List;

public class ReceiverData {

    public final String schema;
    public final String aggregateMethod;
    public final List<SchemaFieldMappingData> fieldsMapping = new ArrayList<>();

    public ReceiverData(final String schema,
                        final String aggregateMethod,
                        final List<SchemaFieldMappingData> fieldsMapping) {
        this.schema = schema;
        this.aggregateMethod = aggregateMethod;
        this.fieldsMapping.addAll(fieldsMapping);
    }
}
