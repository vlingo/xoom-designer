package ${packageName};

import io.vlingo.xoom.actors.testkit.AccessSafely;
import io.vlingo.xoom.symbio.Entry;
import io.vlingo.xoom.symbio.State;
import io.vlingo.xoom.symbio.store.dispatch.Dispatchable;
import io.vlingo.xoom.symbio.store.dispatch.Dispatcher;
import io.vlingo.xoom.symbio.store.dispatch.DispatcherControl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class ${dispatcherName} implements Dispatcher<Dispatchable<Entry<String>, State<?>>> {

  private AccessSafely access;

  private List<Entry<String>> entries = new CopyOnWriteArrayList<>();

  public ${dispatcherName}() {
    super();
    this.access = afterCompleting(0);
  }

  public AccessSafely afterCompleting(final int times) {
    access = AccessSafely
      .afterCompleting(times)
      .writingWith("appendedAll", (List<Entry<String>> appended) -> entries.addAll(appended))
      .readingWith("appendedAt", (Integer index) -> entries.get(index))
      .readingWith("entriesCount", () -> entries.size());

    return access;
  }

  @Override
  public void controlWith(final DispatcherControl control) {

  }

  @Override
  public void dispatch(final Dispatchable<Entry<String>, State<?>> dispatchable) {
    access.writeUsing("appendedAll", dispatchable.entries());
  }
}
