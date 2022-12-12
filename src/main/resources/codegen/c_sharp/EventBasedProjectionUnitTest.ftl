using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Linq;
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

namespace ${packageName};

<#macro textWarbleProjectable domainEventName dataObjectParams version>
  private IProjectable Create${domainEventName}(${dataName} data)
  {
    var eventData = new ${domainEventName}(${dataObjectParams});

    var textEntry = new TextEntry(typeof(${domainEventName}), 1, JsonSerialization.Serialized(eventData), ${version}, Metadata.WithObject(eventData));

    var projectionId = Guid.NewGuid().ToString();
    _valueToProjectionId.TryAdd(data.Id, projectionId);

    return new TextProjectable(null, new[] { textEntry }, projectionId);
  }
</#macro>

<#macro factoryMethodTestStatements domainEventName>

    var control = new CountingProjectionControl();
    var access = control.AfterCompleting(2);
    _projection.ProjectWith(Create${domainEventName}(firstData.To${dataName}()), control);
    _projection.ProjectWith(Create${domainEventName}(secondData.To${dataName}()), control);
    var confirmations = access.ReadFrom<IDictionary<string, int>>("confirmations");

    Assert.Equal(2, confirmations.Count);
    Assert.Equal(1, ValueOfProjectionIdFor(firstData.Id, confirmations));
    Assert.Equal(1, ValueOfProjectionIdFor(secondData.Id, confirmations));

    var interest = new CountingReadResultInterest<${dataObjectName}>();
    var interestAccess = interest.AfterCompleting(1);
    _stateStore.Read<${dataObjectName}>(firstData.Id, interest);
    var item = interestAccess.ReadFrom<string, ${dataObjectName}>("item", firstData.Id);
</#macro>

<#macro factoryMethodTestAssertion>

    interest = new CountingReadResultInterest<${dataObjectName}>();
    interestAccess = interest.AfterCompleting(1);
    _stateStore.Read<${dataObjectName}>(secondData.Id, interest);
    item = interestAccess.ReadFrom<string, ${dataObjectName}>("item", secondData.Id);
    Assert.Equal(secondData.Id, item.Id);
</#macro>

<#macro updateTestStatements domainEventName withExampleRegistration>
  <#if withExampleRegistration>
    RegisterExample${aggregateProtocolName}(firstData.To${dataName}(), secondData.To${dataName}());
  </#if>

    var control = new CountingProjectionControl();
    var access = control.AfterCompleting(1);
    _projection.ProjectWith(Create${domainEventName}(firstData.To${dataName}()), control);
    var confirmations = access.ReadFrom<IDictionary<string, int>>("confirmations");

    Assert.Equal(1, confirmations.Count);
    Assert.Equal(1, ValueOfProjectionIdFor(firstData.Id, confirmations));

    var interest = new CountingReadResultInterest<${dataObjectName}>();
    var interestAccess = interest.AfterCompleting(1);
    _stateStore.Read<${dataObjectName}>(firstData.Id, interest);
    var item = interestAccess.ReadFrom<string, ${dataObjectName}>("item", firstData.Id);
</#macro>
public class ${projectionUnitTestName}
{

  private readonly World _world;
  private readonly IStateStore _stateStore;
  private readonly IProjection _projection;
  private readonly ConcurrentDictionary<string, string> _valueToProjectionId;

  public ${projectionUnitTestName}()
  {
    _world = World.StartWithDefaults("test-state-store-projection");
    var dispatcher = new NoOpDispatcher();
    _valueToProjectionId = new ConcurrentDictionary<string, string>();
    _stateStore =_world.ActorFor<IStateStore>(typeof(InMemoryStateStoreActor<TextState>), dispatcher);
    var statefulTypeRegistry = StatefulTypeRegistry.RegisterAll(_world, _stateStore, typeof(${dataObjectName}));
    QueryModelStateStoreProvider.Using(_world.Stage, statefulTypeRegistry);
    _projection = _world.ActorFor<IProjection>(typeof(${projectionName}), _stateStore);
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
  private int ValueOfProjectionIdFor(string valueText, IDictionary<string, int> confirmations) =>
      ((ConcurrentDictionary<string, int>) confirmations).GetValueOrDefault(_valueToProjectionId.GetValueOrDefault(valueText));

<#list testCases as testCase>
<#if testCase.factoryMethod>
  <@textWarbleProjectable testCase.domainEventName testCase.dataObjectParams 1/>
<#else>
  <@textWarbleProjectable testCase.domainEventName testCase.dataObjectParams 2/>
</#if>

</#list>
}
