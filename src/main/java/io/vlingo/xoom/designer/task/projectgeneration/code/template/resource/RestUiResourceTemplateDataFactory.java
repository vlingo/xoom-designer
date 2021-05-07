// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.resource;

import io.vlingo.xoom.turbo.codegen.content.Content;
import io.vlingo.xoom.turbo.codegen.language.Language;
import io.vlingo.xoom.turbo.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.turbo.codegen.template.TemplateData;

import java.util.Collections;
import java.util.List;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.*;

public class RestUiResourceTemplateDataFactory {

  public static List<TemplateData> build(final CodeGenerationParameters parameters, final List<Content> contents) {
    final String basePackage = parameters.retrieveValue(PACKAGE);
    final Language language = parameters.retrieveValue(LANGUAGE, Language::valueOf);
    return Collections.singletonList(new RestUiResourceTemplateData(basePackage, language));
  }

}
