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

  public ${dispatcherName}() : base()
  {
    this._access = AfterCompleting(0);
  }

  public AccessSafely AfterCompleting(int times) {
    _access = AccessSafely
      .AfterCompleting(times)
      .WritingWith("appendedAll", (List<IEntry> appended) => appended.ForEach(_entries.Enqueue))
      .ReadingWith("appendedAt", (int index)=>_entries.ElementAtOrDefault(index))
      .ReadingWith("entriesCount", () => _entries.Count);

    return _access;
  }

  public void ControlWith(IDispatcherControl control)
  {
  }

  public void Dispatch(Dispatchable dispatchable)
  {
    _access.WriteUsing("appendedAll", dispatchable.Entries);
  }
}
