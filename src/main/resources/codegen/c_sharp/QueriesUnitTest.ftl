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

public class ${queriesUnitTestName}
{
  private readonly IStateStore _stateStore;
  private readonly ${queriesName} _queries;

  public ${queriesUnitTestName}()
  {
    var world = World.StartWithDefaults("test-state-store-query");
    _stateStore = world.ActorFor<IStateStore>(typeof(InMemoryStateStoreActor<>), new NoOpDispatcher());
    StatefulTypeRegistry.RegisterAll(world, _stateStore, typeof(${dataObjectName}));
    _queries = world.ActorFor<${queriesName}>(typeof(${queriesActorName}), _stateStore);
  }

  <#list testCases as testCase>
  <#list testCase.dataDeclarations as dataDeclaration>
  ${dataDeclaration}
  </#list>

  [Fact]
  public void ${testCase.methodName}()
  {
    <#list testCase.preliminaryStatements as statement>
    ${statement}
    </#list>
    <#list testCase.statements as statement>

      <#list statement.resultAssignment as resultAssignment>
    ${resultAssignment}
      </#list>

      <#list statement.assertions as assertion>
    ${assertion}
      </#list>
    </#list>
  }

  </#list>
  [Fact]
  public void ${queryByIdMethodName}EmptyResult()
  {
    var result = _queries.${queryByIdMethodName}("1").Await();
    Assert.Equal("", result.Id);
  }

}