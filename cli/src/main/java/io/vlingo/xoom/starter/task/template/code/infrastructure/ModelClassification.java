// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code.infrastructure;

public enum ModelClassification {

    SINGLE("SingleModel"),
    COMMAND("CommandModel"),
    QUERY("QueryModel");

    public final String title;

    ModelClassification(String title) {
        this.title = title;
    }

    public boolean isQueryModel() {
        return equals(QUERY);
    }

    public boolean isSingle() {
        return equals(SINGLE);
    }
}
