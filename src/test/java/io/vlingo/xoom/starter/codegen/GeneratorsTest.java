// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.codegen;

import org.junit.Test;

public class GeneratorsTest {

  @Test
  public void testCommandModelStateStoreProviderGenerator() throws Exception {
    final CommandModelStateStoreProviderGenerator generator = new CommandModelStateStoreProviderGenerator();

    generator.inputOfPackageName("com.beyondbusiness.auth.infra.persistence");

    generator.inputOf(new ImportHolder("com.beyondbusiness.auth.model.ProfileState"));
    generator.inputOf(new ImportHolder("com.beyondbusiness.auth.model.TenantState"));
    generator.inputOf(new ImportHolder("com.beyondbusiness.auth.model.UserState"));

    // TODO: Needs ActorInstantiator
    generator.inputOfStateStoreClassName("JDBCStateStoreActor");

    generator.inputOf(new StateAdapterHolder("TenantState"));
    generator.inputOf(new StateAdapterHolder("UserState"));
    generator.inputOf(new StateAdapterHolder("ProfileState"));

    generator.generate();
  }
}
