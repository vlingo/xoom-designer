// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.codegeneration;

import io.vlingo.xoom.starter.task.template.StorageType;
import org.junit.jupiter.api.Test;

public class GeneratorsTest {

  @Test
  public void testCommandModelStateStoreProviderGenerator() throws Exception {
    final StateStoreProviderGenerator generator = new StateStoreProviderGenerator();

    generator.inputOfPackageName("com.beyond5.auth.infra.persistence");

    generator.inputOf(new ImportHolder("com.beyond5.auth.model.profile.ProfileState"));
    generator.inputOf(new ImportHolder("com.beyond5.auth.model.tenant.TenantState"));
    generator.inputOf(new ImportHolder("com.beyond5.auth.model.user.UserState"));

    // TODO: Needs ActorInstantiator
    generator.inputOfStateStoreClassName("io.vlingo.symbio.store.journal.jdbc.JDBCStateStoreActor");

    generator.inputOf(new StateAdapterHolder("TenantState"));
    generator.inputOf(new StateAdapterHolder("UserState"));
    generator.inputOf(new StateAdapterHolder("ProfileState"));

    System.out.println(generator.generate("CommandModelStateStoreProvider"));
  }

  @Test
  public void testQueryModelStateStoreProviderGenerator() throws Exception {
    final StateStoreProviderGenerator generator = new StateStoreProviderGenerator();

    generator.inputOfPackageName("com.beyond5.auth.infra.persistence");

    generator.inputOf(new ImportHolder("com.beyond5.auth.model.profile.ProfileState"));
    generator.inputOf(new ImportHolder("com.beyond5.auth.model.tenant.TenantState"));
    generator.inputOf(new ImportHolder("com.beyond5.auth.model.user.UserState"));

    // TODO: Needs ActorInstantiator
    generator.inputOfStateStoreClassName("io.vlingo.symbio.store.journal.jdbc.JDBCStateStoreActor");

    generator.inputOf(new StateAdapterHolder("TenantState"));
    generator.inputOf(new StateAdapterHolder("UserState"));
    generator.inputOf(new StateAdapterHolder("ProfileState"));

    // TODO: Needs pre-generated Queries and QueriesActor for, but with no methods:
    //       final Queries queries = stage.actorFor(Queries.class, QueriesActor.class, store);

    System.out.println(generator.generate("QueryModelStateStoreProvider"));
  }

  @Test
  public void testStateAdapterGenerator() throws Exception {
    final StateAdapterGenerator profileStateGenerator = new StateAdapterGenerator("ProfileState");
    profileStateGenerator.inputOfPackageName("com.beyond5.auth.infra.persistence");
    profileStateGenerator.inputOf(new ImportHolder("com.beyond5.auth.model.profile.ProfileState"));
    System.out.println(profileStateGenerator.generate("StateAdapter"));

    final StateAdapterGenerator tenantStateGenerator = new StateAdapterGenerator("TenantState");
    tenantStateGenerator.inputOfPackageName("com.beyond5.auth.infra.persistence");
    tenantStateGenerator.inputOf(new ImportHolder("com.beyond5.auth.model.tenant.TenantState"));
    System.out.println(tenantStateGenerator.generate("StateAdapter"));

    final StateAdapterGenerator userStateGenerator = new StateAdapterGenerator("UserState");
    userStateGenerator.inputOfPackageName("com.beyond5.auth.infra.persistence");
    userStateGenerator.inputOf(new ImportHolder("com.beyond5.auth.model.user.UserState"));
    System.out.println(userStateGenerator.generate("StateAdapter"));
  }

  @Test
  public void testBeanState() throws Exception {
    System.out.println(StateCodeGenerator.instance().generate("ProfileState", "com.beyond5.auth.model.profile", StorageType.OBJECT_STORE));
  }

  @Test
  public void testValueState() throws Exception {
    System.out.println(StateCodeGenerator.instance().generate("ValueState", "com.beyond5.auth.model.user", StorageType.JOURNAL));
  }

  @Test
  public void testDomainEvent() throws Exception {
    System.out.println(DomainEventCodeGenerator.instance().generate("TenantRegistered", "com.beyond5.auth.model.tenant"));
  }

  @Test
  public void testPlaceholderDefinedEvent() throws Exception {
    System.out.println(DomainEventCodeGenerator.instance().generate("PlaceholderDefinedEvent", "com.beyond5.auth.model.tenant"));
  }

}
