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

namespace ${packageName};

<#macro textWarbleProjectable operation>
	private IProjectable Create${operation}(${dataObjectName} data, int version, String operation)
  {
		var state = new TextState(data.Id, typeof(${dataName}), 1, JsonSerialization.Serialized(data.To${dataName}()), version,
		Metadata.With(data.To${dataName}(), data.Id, operation));

		var projectionId = Guid.NewGuid().ToString();

    _valueToProjectionId.Add(data.Id, projectionId);

		return new TextProjectable(state, new List<IEntry>(), projectionId);
	}
</#macro>

public class ${projectionUnitTestName}: IDisposable
{
  private readonly World _world;
  private readonly IProjection _projection;
  private readonly Dictionary<string, string> _valueToProjectionId;

  public ${projectionUnitTestName}()
  {
    _world = World.StartWithDefaults("test-state-store-projection");
    var store = _world.ActorFor<IStateStore>(typeof(InMemoryStateStoreActor<TextState>), new NoOpDispatcher());
    _projection = _world.ActorFor<IProjection>(typeof(${projectionName}), store);
    StatefulTypeRegistry.RegisterAll<${dataObjectName}>(_world, store);
    _valueToProjectionId = new Dictionary<string, string>();
  }

<#list testCases as testCase>
	<#if testCase.factoryMethod>
  [Fact]
  public void ${testCase.methodName}()
  {
    <#list testCase.dataDeclarations as dataDeclaration>
		${dataDeclaration}
    </#list>
		var control = new CountingProjectionControl();
    var access = control.AfterCompleting(2);

    _projection.ProjectWith(Create${testCase.domainEventName}(firstData, 1, "${testCase.domainEventName}"), control);
    _projection.ProjectWith(Create${testCase.domainEventName}(secondData, 1, "${testCase.domainEventName}"), control);

    var confirmations = access.ReadFrom("confirmations");

    Assert.Equal(2, confirmations.Count);

    Assert.Equal(1, ValueOfProjectionIdFor(firstData.id, confirmations));
    Assert.Equal(1, ValueOfProjectionIdFor(secondData.id, confirmations));

    Assert.Equal(2, ((IDictionary) access.ReadFrom("confirmations")).Count);
  }
  <#else>

  [Fact]
	public void ${testCase.methodName}()
  {
    <#list testCase.dataDeclarations as dataDeclaration>
    ${dataDeclaration}
    </#list>
	  var control = new CountingProjectionControl();

		var accessControl = control.AfterCompleting(4);

    _projection.ProjectWith(Create${testCases?first.domainEventName}(firstData, 1, "${testCases?first.domainEventName}"), control);
    _projection.ProjectWith(Create${testCases?first.domainEventName}(secondData, 1, "${testCases?first.domainEventName}"), control);

    _projection.ProjectWith(Create${testCase.domainEventName}(firstData, 2, "${testCase.domainEventName}"), control);
    _projection.ProjectWith(Create${testCase.domainEventName}(secondData, 2, "${testCase.domainEventName}"), control);

		var confirmations = accessControl.ReadFrom("confirmations");

    Assert.Equal(4, confirmations.Count);

    Assert.Equal(1, ValueOfProjectionIdFor(firstData.id, confirmations));
    Assert.Equal(1, ValueOfProjectionIdFor(secondData.id, confirmations));

    Assert.Equal(4, ((IDictionary) accessControl.ReadFrom("confirmations")).Count);
	}
	</#if>
</#list>

  public void Dispose()
  {
    _world.Terminate();
  }

  private int ValueOfProjectionIdFor(string valueText, Dictionary<string, int> confirmations)
  {
    return confirmations.GetValueOrDefault(_valueToProjectionId.GetValueOrDefault(valueText));
  }
<#list testCases as testCase>

  <@textWarbleProjectable testCase.domainEventName/>
</#list>
}