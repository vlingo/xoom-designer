// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code;

import java.io.File;
import java.nio.file.Paths;

public class DomainEventTemplateData {

    public final String name;
    public final String packageName;
    public final String absolutePath;

    public DomainEventTemplateData(final String name, final String packageName, final String absolutePath) {
        this.name = name.trim();
        this.packageName = packageName;
        this.absolutePath = absolutePath;
    }

    public File file() {
        return new File(Paths.get(absolutePath, name + ".java").toString());
    }

}
