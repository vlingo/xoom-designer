using System;
using System.Collections;
using System.Collections.Generic;
using Vlingo.Xoom.Actors;
using Vlingo.Xoom.Lattice.Model.Projection;
using Vlingo.Xoom.Lattice.Model.Stateful;
using Vlingo.Xoom.Symbio.Store.Dispatch;
using Vlingo.Xoom.Symbio.Store.State;
using Vlingo.Xoom.Symbio.Store.State.InMemory;
using Vlingo.Xoom.Common.Serialization;
using Vlingo.Xoom.Symbio;
<#list imports as import>
using ${import.qualifiedClassName};
</#list>
using Xunit;

package ${packageName};

<#macro textWarbleProjectable domainEventName dataObjectParams version>
  private IProjectable Create${domainEventName}(${dataName} data)
  {
    var eventData = new ${domainEventName}(${dataObjectParams});

    var textEntry = new BaseEntry.TextEntry<${domainEventName}>(1, JsonSerialization.Serialized(eventData), ${version}, Metadata.WithObject(eventData));

    var projectionId = Guid.NewGuid().ToString();
    _valueToProjectionId.Add(dataId, projectionId);

    return new TextProjectable(null, new List<IEntry>(textEntry), projectionId);
  }
</#macro>

<#macro factoryMethodTestStatements domainEventName>

    var control = new CountingProjectionControl();
    var access = control.AfterCompleting(2);
    projection.ProjectWith(Create${domainEventName}(firstData.To${dataName}()), control);
    projection.ProjectWith(Create${domainEventName}(secondData.To${dataName}()), control);
    var confirmations = access.ReadFrom("confirmations");

    Assert.Equal(2, confirmations.Count);
    Assert.Equal(1, ValueOfProjectionIdFor(firstData.Id, confirmations));
    Assert.Equal(1, ValueOfProjectionIdFor(secondData.Id, confirmations));

    var interest = new CountingReadResultInterest();
    var interestAccess = interest.AfterCompleting(1);
    _stateStore.Read<${dataObjectName}>(firstData.Id, interest);
    var item = interestAccess.ReadFrom("item", firstData.Id);
</#macro>

<#macro factoryMethodTestAssertion>

    interest = new CountingReadResultInterest();
    interestAccess = interest.AfterCompleting(1);
    _stateStore.Read<${dataObjectName}>(secondData.Id, interest);
    item = interestAccess.ReadFrom("item", secondData.Id);
    Assert.Equal(secondData.Id, item.Id);
</#macro>

<#macro updateTestStatements domainEventName withExampleRegistration>
  <#if withExampleRegistration>
    RegisterExample${aggregateProtocolName}(firstData.To${dataName}(), secondData.To${dataName}());
  </#if>

    var control = new CountingProjectionControl();
    var access = control.AfterCompleting(1);
    _projection.ProjectWith(Create${domainEventName}(firstData.To${dataName}()), control);
    var confirmations = access.ReadFrom("confirmations");

    assertEquals(1, confirmations.size());
    assertEquals(1, ValueOfProjectionIdFor(firstData.id, confirmations));

    var interest = new CountingReadResultInterest();
    var interestAccess = interest.AfterCompleting(1);
    _stateStore.Read<${dataObjectName}>(firstData.Id, interest);
    var item = interestAccess.ReadFrom("item", firstData.Id);
</#macro>
public class ${projectionUnitTestName}
{

  private readonly World _world;
  private readonly StateStore _stateStore;
  private readonly IProjection _projection;
  private readonly Dictionary<string, String> _valueToProjectionId;

  public ${projectionUnitTestName}()
  {
    _world = World.StartWithDefaults("test-state-store-projection");
    var dispatcher = new NoOpDispatcher();
    _valueToProjectionId = new ConcurrentHashMap<>();
    _stateStore = world.ActorFor<StateStore>(typeof(InMemoryStateStoreActor), dispatcher);
    var statefulTypeRegistry = StatefulTypeRegistry.RegisterAll(_world, _stateStore, typeof(${dataObjectName}));
    QueryModelStateStoreProvider.Using(_world.stage(), statefulTypeRegistry);
    _projection = world.ActorFor<IProjection>(typeof(${projectionName}), _stateStore);
  }

<#if testCases?filter(testCase -> testCase.factoryMethod)?has_content>
  private void RegisterExample${aggregateProtocolName}(${dataName} firstData, ${dataName} secondData)
  {
    var control = new CountingProjectionControl();
    var access = control.AfterCompleting(2);
    _projection.ProjectWith(Create${domainEventName}(firstData), control);
    _projection.ProjectWith(Create${domainEventName}(secondData), control);
  }
</#if>

  <#list testCases as testCase>
  [Fact]
  public void ${testCase.methodName}()
  {
    <#list testCase.dataDeclarations as dataDeclaration>
    ${dataDeclaration}
    </#list>
    <#if testCase.factoryMethod>
      <@factoryMethodTestStatements testCase.domainEventName />
    <#else>
      <@updateTestStatements testCase.domainEventName testCases?filter(testCase -> testCase.factoryMethod)?has_content/>
    </#if>

    <#list testCase.statements as statement>
      <#list statement.assertions as assertion>
    ${assertion}
      </#list>
      <#if testCase.factoryMethod>
        <@factoryMethodTestAssertion />
        <#list statement.secondAssertions as assertion>
    ${assertion}
        </#list>
      </#if>
    </#list>
  }

  </#list>
  private int ValueOfProjectionIdFor(string valueText, Dictionary<string, int> confirmations) =>
      confirmations.GetValueOrDefault(_valueToProjectionId.GetValueOrDefault(valueText));

<#list testCases as testCase>
<#if testCase.factoryMethod>
  <@textWarbleProjectable testCase.domainEventName testCase.dataObjectParams 1/>
<#else>
  <@textWarbleProjectable testCase.domainEventName testCase.dataObjectParams 2/>
</#if>

</#list>
}
