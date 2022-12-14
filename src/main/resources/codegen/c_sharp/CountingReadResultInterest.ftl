using System.Collections.Concurrent;
using System.Collections.Generic;
using Vlingo.Xoom.Actors.TestKit;
using Vlingo.Xoom.Common;
using Vlingo.Xoom.Symbio;
using Vlingo.Xoom.Symbio.Store;
using Vlingo.Xoom.Symbio.Store.State;

namespace ${packageName};

public class CountingReadResultInterest<T> : IReadResultInterest
{
  private AccessSafely _access;
  private readonly ConcurrentDictionary<string, T> _items = new ConcurrentDictionary<string, T>();

  public void ReadResultedIn<TState>(IOutcome<StorageException, Result> outcome, string? id, TState state,
  int stateVersion, Metadata? metadata, object? @object)
  {
    _access.WriteUsing("item", id, state);
  }

  public void ReadResultedIn<TState>(IOutcome<StorageException, Result> outcome, IEnumerable<TypedStateBundle> bundles, object? @object)
  {
  }

  public AccessSafely AfterCompleting(int times)
  {
    _access = AccessSafely.AfterCompleting(times);
    _access.WritingWith<string, T>("item", (key, item) => _items.TryAdd(key, item));
    _access.ReadingWith<string>("item", key => _items.GetValueOrDefault(key));
    return _access;
  }
}