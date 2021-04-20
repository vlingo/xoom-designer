// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneation.gui.steps;

import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.option.OptionValue;
import io.vlingo.xoom.designer.task.projectgeneration.ProjectGenerationInformation;
import io.vlingo.xoom.designer.task.projectgeneration.gui.steps.GenerationTargetResolverStep;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static io.vlingo.xoom.designer.Configuration.*;
import static io.vlingo.xoom.designer.task.Agent.TERMINAL;
import static io.vlingo.xoom.designer.task.option.OptionName.TARGET;

public class GenerationTargetResolverStepStepTest {

  private final GenerationTargetResolverStep step = new GenerationTargetResolverStep();

  @Test
  public void testThatGenerationTargetIsResolvedWithEmptyOption() {
    final OptionValue optionValue = OptionValue.with(TARGET, "");
    step.process(TaskExecutionContext.executedFrom(TERMINAL).withOptions(Arrays.asList(optionValue)));
    Assertions.assertFalse(ProjectGenerationInformation.load().requiresCompression());
  }

  @Test
  public void testThatGenerationTargetIsResolvedWithZipOption() {
    final OptionValue optionValue = OptionValue.with(TARGET, XOOM_DESIGNER_GENERATION_TARGET_ZIP);
    step.process(TaskExecutionContext.executedFrom(TERMINAL).withOptions(Arrays.asList(optionValue)));
    Assertions.assertTrue(ProjectGenerationInformation.load().requiresCompression());
  }

  @Test
  public void testThatGenerationTargetIsResolvedWithFileSystemOption() {
    final OptionValue optionValue = OptionValue.with(TARGET, XOOM_DESIGNER_GENERATION_TARGET_FS);
    step.process(TaskExecutionContext.executedFrom(TERMINAL).withOptions(Arrays.asList(optionValue)));
    Assertions.assertFalse(ProjectGenerationInformation.load().requiresCompression());
  }

  @BeforeEach
  public void setUp() {
    ProjectGenerationInformation.reset();
    System.clearProperty(XOOM_DESIGNER_GENERATION_TARGET);
  }
}
