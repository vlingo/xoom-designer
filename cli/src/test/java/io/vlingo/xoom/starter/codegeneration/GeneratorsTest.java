// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.codegeneration;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.vlingo.xoom.starter.task.template.StorageType;

public class GeneratorsTest {

  @Test
  public void testBootstrap() {
    final BootstrapGenerator generator = new BootstrapGenerator("com.beyond5.auth");

    generator.inputOfPackageName("com.beyond5.auth");

    generator.inputOf(new ImportHolder("com.beyond5.auth.resource.ProfileResource"));
    generator.inputOf(new ImportHolder("com.beyond5.auth.resource.TenantResource"));
    generator.inputOf(new ImportHolder("com.beyond5.auth.resource.UserResource"));

    generator.inputOfCommandModelJournalProvider(true);
    generator.inputOfCommandModelStateStoreProvider(false);
    generator.inputOfProjectionDispatcherProvider(true);

    generator.inputOfResource("ProfileResource");
    generator.inputOfResource("TenantResource");
    generator.inputOfResource("UserResource");

    System.out.println(generator.generate("Bootstrap"));
  }

  @Test
  public void testCommandModelJournalProvider() {
    final JournalProviderGenerator generator = new JournalProviderGenerator();

    generator.inputOfPackageName("com.beyond5.auth.infra.persistence");

    generator.inputOf(new ImportHolder("com.beyond5.auth.model.profile.ProfileDefined"));
    generator.inputOf(new ImportHolder("com.beyond5.auth.model.tenant.TenantDefined"));
    generator.inputOf(new ImportHolder("com.beyond5.auth.model.user.UserDefined"));

    generator.inputOfJournalStoreClassName("io.vlingo.symbio.store.journal.inmemory.InMemoryJournalActor");

    generator.inputOf(new EntryAdapterHolder("ProfileDefined"));
    generator.inputOf(new EntryAdapterHolder("TenantDefined"));
    generator.inputOf(new EntryAdapterHolder("UserDefined"));

    System.out.println(generator.generate("CommandModelJournalProvider"));
  }

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
  public void testEntryAdapterGenerator() throws Exception {
    final EntryAdapterGenerator profileEntryGenerator = new EntryAdapterGenerator("ProfileDefined");
    profileEntryGenerator.inputOfPackageName("com.beyond5.auth.infra.persistence");
    profileEntryGenerator.inputOf(new ImportHolder("com.beyond5.auth.model.profile.ProfileDefined"));
    System.out.println(profileEntryGenerator.generate("EntryAdapter"));

    final EntryAdapterGenerator tenantEntryGenerator = new EntryAdapterGenerator("TenantDefined");
    tenantEntryGenerator.inputOfPackageName("com.beyond5.auth.infra.persistence");
    tenantEntryGenerator.inputOf(new ImportHolder("com.beyond5.auth.model.tenant.TenantDefined"));
    System.out.println(tenantEntryGenerator.generate("EntryAdapter"));

    final EntryAdapterGenerator userEntryGenerator = new EntryAdapterGenerator("UserDefined");
    userEntryGenerator.inputOfPackageName("com.beyond5.auth.infra.persistence");
    userEntryGenerator.inputOf(new ImportHolder("com.beyond5.auth.model.user.UserDefined"));
    System.out.println(userEntryGenerator.generate("EntryAdapter"));
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
  public void testProjectionDispatcherProviderGenerator() {
    final List<ProjectToDescriptionHolder> holders =
            Arrays.asList(
                    new ProjectToDescriptionHolder("Profile", Arrays.asList("ProfileDefined")),
                    new ProjectToDescriptionHolder("Tenant", Arrays.asList("TenantDefined", "TenantActivated", "TenantDeactivated")),
                    new ProjectToDescriptionHolder("User", Arrays.asList("UserDefined", "UserPasswordChanged"))
                    );

    final ProjectionDispatcherProviderGenerator generator = new ProjectionDispatcherProviderGenerator(holders);

    generator.inputOfPackageName("com.beyond5.auth.infra.persistence");

    generator.inputOf(new ImportHolder("com.beyond5.auth.model.profile.ProfileDefined"));
    generator.inputOf(new ImportHolder("com.beyond5.auth.model.tenant.TenantDefined"));
    generator.inputOf(new ImportHolder("com.beyond5.auth.model.tenant.TenantActivated"));
    generator.inputOf(new ImportHolder("com.beyond5.auth.model.tenant.TenantDeactivated"));
    generator.inputOf(new ImportHolder("com.beyond5.auth.model.user.UserDefined"));
    generator.inputOf(new ImportHolder("com.beyond5.auth.model.user.UserPasswordChanged"));

    System.out.println(generator.generate("ProjectionDispatcherProvider"));
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

  @Test
  public void testValueData() {
    final DataTypeGenerator profileGenerator = new DataTypeGenerator("Profile");
    profileGenerator.inputOfPackageName("com.beyond5.auth.resource.data");
    profileGenerator.inputOf(new ImportHolder("com.beyond5.auth.model.profile.ProfileState"));
    System.out.println(profileGenerator.generate("ValueData"));

//    System.out.println(new DataTypeGenerator("Tenant").generate("ValueData"));
//    System.out.println(new DataTypeGenerator("User").generate("ValueData"));
  }
}
