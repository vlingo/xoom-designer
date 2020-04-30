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

    generator.inputOfPackageName("com.beyond5.auth.infra.persistence");

    generator.inputOf(new ImportHolder("com.beyond5.auth.model.profile.ProfileState"));
    generator.inputOf(new ImportHolder("com.beyond5.auth.model.tenant.TenantState"));
    generator.inputOf(new ImportHolder("com.beyond5.auth.model.user.UserState"));

    // TODO: Needs ActorInstantiator
    generator.inputOfStateStoreClassName("io.vlingo.symbio.store.journal.jdbc.JDBCStateStoreActor");

    generator.inputOf(new StateAdapterHolder("TenantState"));
    generator.inputOf(new StateAdapterHolder("UserState"));
    generator.inputOf(new StateAdapterHolder("ProfileState"));

    generator.generate("CommandModelStateStoreProvider");
  }

  @Test
  public void testStateAdapterGenerator() throws Exception {
    final StateAdapterGenerator profileStateGenerator = new StateAdapterGenerator("ProfileState");
    profileStateGenerator.inputOfPackageName("com.beyond5.auth.infra.persistence");
    profileStateGenerator.inputOf(new ImportHolder("com.beyond5.auth.model.profile.ProfileState"));
    profileStateGenerator.generate("StateAdapter");

    final StateAdapterGenerator tenantStateGenerator = new StateAdapterGenerator("TenantState");
    tenantStateGenerator.inputOfPackageName("com.beyond5.auth.infra.persistence");
    tenantStateGenerator.inputOf(new ImportHolder("com.beyond5.auth.model.tenant.TenantState"));
    tenantStateGenerator.generate("StateAdapter");

    final StateAdapterGenerator userStateGenerator = new StateAdapterGenerator("UserState");
    userStateGenerator.inputOfPackageName("com.beyond5.auth.infra.persistence");
    userStateGenerator.inputOf(new ImportHolder("com.beyond5.auth.model.user.UserState"));
    userStateGenerator.generate("StateAdapter");
  }

  @Test
  public void testBeanState() throws Exception {
    final StateGenerator valueStateGenerator = new StateGenerator("ProfileState");
    valueStateGenerator.inputOfPackageName("com.beyond5.auth.model.profile");
    valueStateGenerator.generate("BeanState");
  }

  @Test
  public void testValueState() throws Exception {
    final StateGenerator valueStateGenerator = new StateGenerator("UserState");
    valueStateGenerator.inputOfPackageName("com.beyond5.auth.model.user");
    valueStateGenerator.generate("ValueState");
  }

  @Test
  public void testDomainEvent() throws Exception {
    final DomainEventGenerator domainEventGenerator = new DomainEventGenerator("TenantRegistered");
    domainEventGenerator.inputOfPackageName("com.beyond5.auth.model.tenant");
    domainEventGenerator.generate("DomainEvent");
  }

  @Test
  public void testPlaceholderDefinedEvent() throws Exception {
    final DomainEventGenerator domainEventGenerator = new PlaceholderDefinedEventGenerator("Tenant");
    domainEventGenerator.inputOfPackageName("com.beyond5.auth.model.tenant");
    domainEventGenerator.generate("PlaceholderDefinedEvent");
  }

  @Test
  public void testEventSourcedAggregate() throws Exception {
    final AggregateGenerator aggregateGenerator = new AggregateGenerator("Tenant", "TenantState");
    aggregateGenerator.inputOfPackageName("com.beyond5.auth.model.tenant");
    aggregateGenerator.generate("EventSourcedEntity");
  }

  @Test
  public void testObjectAggregate() throws Exception {
    final AggregateGenerator aggregateGenerator = new AggregateGenerator("Profile", "ProfileState");
    aggregateGenerator.inputOfPackageName("com.beyond5.auth.model.profile");
    aggregateGenerator.generate("ObjectEntity");
  }

  @Test
  public void testStatefulAggregate() throws Exception {
    final AggregateGenerator aggregateGenerator = new AggregateGenerator("User", "UserState");
    aggregateGenerator.inputOfPackageName("com.beyond5.auth.model.user");
    aggregateGenerator.generate("StatefulEntity");
  }
}
