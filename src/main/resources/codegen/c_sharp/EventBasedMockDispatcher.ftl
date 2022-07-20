using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Linq;
using Vlingo.Xoom.Actors.TestKit;
using Vlingo.Xoom.Symbio;
using Vlingo.Xoom.Symbio.Store.Dispatch;

namespace ${packageName};

public class ${dispatcherName} : IDispatcher
{

  private AccessSafely _access;

  private readonly ConcurrentQueue<IEntry> _entries = new ConcurrentQueue<IEntry>();
  private readonly ConcurrentQueue<IState> _states = new ConcurrentQueue<IState>();

  public ${dispatcherName}() : base()
  {
    this._access = AfterCompleting(0);
  }

  public AccessSafely AfterCompleting(int times) {
    _access = AccessSafely
      .AfterCompleting(times)
      .WritingWith<List<IEntry>>("appendedEntriesAll", appendedEntries => appendedEntries.ForEach(_entries.Enqueue))
      .WritingWith<IState>("appendedState", appendedState => _states.Enqueue(appendedState))
      .ReadingWith("appendedEntryAt", (int index) => _entries.ElementAtOrDefault(index))
      .ReadingWith("appendedStateAt", (int index) => _states.ElementAtOrDefault(index))
      .ReadingWith("entriesCount", () => _entries.Count)
      .ReadingWith("statesCount", () => _states.Count);

    return _access;
  }

  public void ControlWith(IDispatcherControl control)
  {
  }

  public void Dispatch(Dispatchable dispatchable)
  {
    _access.WriteUsing("appendedAll", dispatchable.Entries);
    _access.WriteUsing("appendedState", dispatchable.State.Get());
  }
}
