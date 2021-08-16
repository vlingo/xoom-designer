// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.userinterface;

import io.vlingo.xoom.designer.task.OptionValue;
import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.projectgeneration.GenerationTarget;
import io.vlingo.xoom.turbo.ComponentRegistry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static io.vlingo.xoom.designer.task.OptionName.TARGET;

public class GenerationTargetRegistrationStepTest {

  private final GenerationTargetRegistrationStep step = new GenerationTargetRegistrationStep();

  @Test
  public void testThatGenerationTargetIsRegisteredWithEmptyOption() {
    final OptionValue optionValue = OptionValue.with(TARGET, "");
    step.process(TaskExecutionContext.withOptions(Arrays.asList(optionValue)));
    Assertions.assertEquals(GenerationTarget.FILESYSTEM, ComponentRegistry.withType(GenerationTarget.class));
  }

  @Test
  public void testThatGenerationTargetIsRegisteredWithZipOption() {
    final OptionValue optionValue = OptionValue.with(TARGET, "zip-download");
    step.process(TaskExecutionContext.withOptions(Arrays.asList(optionValue)));
    Assertions.assertEquals(GenerationTarget.ZIP, ComponentRegistry.withType(GenerationTarget.class));
  }

  @Test
  public void testThatGenerationTargetIsRegisteredWithFileSystemOption() {
    final OptionValue optionValue = OptionValue.with(TARGET, "filesystem");
    step.process(TaskExecutionContext.withOptions(Arrays.asList(optionValue)));
    Assertions.assertEquals(GenerationTarget.FILESYSTEM, ComponentRegistry.withType(GenerationTarget.class));
  }

  @BeforeEach
  public void setUp() {
    ComponentRegistry.clear();
  }
}
