using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using Vlingo.Xoom.Actors;
using Vlingo.Xoom.Common;
using Vlingo.Xoom.Lattice.Model.Stateful;
using Vlingo.Xoom.Symbio.Store;
using Vlingo.Xoom.Symbio.Store.Dispatch;
using Vlingo.Xoom.Symbio.Store.State;
using Vlingo.Xoom.Symbio.Store.State.InMemory;
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
  private readonly IWriteResultInterest _interest;

  public ${queriesUnitTestName}()
  {
    var world = World.StartWithDefaults("test-state-store-query");
    _interest = new NoOpWriterInterest();
    _stateStore = world.ActorFor<IStateStore>(typeof(InMemoryStateStoreActor<TextState>), new NoOpDispatcher());
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

public class NoOpWriterInterest : IWriteResultInterest
{
  public void WriteResultedIn<TState, TSource>(IOutcome<StorageException, Result> outcome, string id, TState state, int stateVersion, IEnumerable<TSource> sources,
    object? @object)
  {
  }
}