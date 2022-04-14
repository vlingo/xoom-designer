// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.reactjs;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.TextExpectation;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.designer.codegen.Label;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StaticFilesGenerationStepTest {

  @Test
  public void testThatStaticFilesAreGenerated() {
    final CodeGenerationParameters parameters =
            CodeGenerationParameters.from(
                    CodeGenerationParameter.of(Label.GROUP_ID, "io.vlingo"),
                    CodeGenerationParameter.of(Label.ARTIFACT_ID, "xoom-app"),
                    CodeGenerationParameter.of(Label.ARTIFACT_VERSION, "1.0.0"),
                    CodeGenerationParameter.of(Label.PACKAGE, "io.vlingo.xoomapp")
            );

    final CodeGenerationContext context = CodeGenerationContext.with(parameters);

    new StaticFilesGenerationStep().process(context);

    final Content gitIgnore = context.findContent(ReactJsTemplateStandard.GIT_IGNORE);
    final Content formHandler = context.findContent(ReactJsTemplateStandard.FORM_HANDLER, "FormHandler");
    final Content formModal = context.findContent(ReactJsTemplateStandard.FORM_MODAL, "FormModal");
    final Content index = context.findContent(ReactJsTemplateStandard.INDEX, "index");
    final Content loadingOrFailed = context.findContent(ReactJsTemplateStandard.LOADING_OR_FAILED, "LoadingOrFailed");
    final Content styleSheetIndex = context.findContent(ReactJsTemplateStandard.STYLE_SHEET_INDEX, "index");
    final Content home = context.findContent(ReactJsTemplateStandard.HOME, "Home");
    final Content htmlIndex = context.findContent(ReactJsTemplateStandard.HTML_INDEX, "index");
    final Content packageConfig = context.findContent(ReactJsTemplateStandard.PACKAGE_CONFIG, "package");

    Assertions.assertTrue(gitIgnore.contains(TextExpectation.onReactJs().read("git-ignore")));
    Assertions.assertTrue(formHandler.contains(TextExpectation.onReactJs().read("form-handler")));
    Assertions.assertTrue(formModal.contains(TextExpectation.onReactJs().read("form-modal")));
    Assertions.assertTrue(index.contains(TextExpectation.onReactJs().read("index")));
    Assertions.assertTrue(loadingOrFailed.contains(TextExpectation.onReactJs().read("loading-or-failed")));
    Assertions.assertTrue(styleSheetIndex.contains(TextExpectation.onReactJs().read("style-sheet-index")));
    Assertions.assertTrue(home.contains(TextExpectation.onReactJs().read("home")));
    Assertions.assertTrue(htmlIndex.contains(TextExpectation.onReactJs().read("html-index")));
    Assertions.assertTrue(packageConfig.contains(TextExpectation.onReactJs().read("package-config")));
  }

}
