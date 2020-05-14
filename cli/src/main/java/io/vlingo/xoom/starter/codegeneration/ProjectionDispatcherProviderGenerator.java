// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.codegeneration;

import java.util.ArrayList;
import java.util.List;

public class ProjectionDispatcherProviderGenerator extends ImportsGenerator {

  public ProjectionDispatcherProviderGenerator(final List<ProjectToDescriptionHolder> projectToDescriptionHolders) {
    final List<String> projectToDescriptions = new ArrayList<>();

    final int size = projectToDescriptionHolders.size();

    for (int idx = 0; idx < projectToDescriptionHolders.size(); ++idx) {
      final String parameterSeparator = idx == (size - 1) ? "" : ",";
      final ProjectToDescriptionHolder holder = projectToDescriptionHolders.get(idx);
      final StringBuilder builder = new StringBuilder().append(holder.getProjectToDescription()).append(parameterSeparator);
      projectToDescriptions.add(builder.toString());
    }

    this.input.put("projectToDescriptions", projectToDescriptions);
  }
}
