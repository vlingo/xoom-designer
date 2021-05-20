package ${packageName};

import io.vlingo.xoom.actors.testkit.AccessSafely;
import io.vlingo.xoom.symbio.Entry;
import io.vlingo.xoom.symbio.State;
import io.vlingo.xoom.symbio.store.dispatch.Dispatchable;
import io.vlingo.xoom.symbio.store.dispatch.Dispatcher;
import io.vlingo.xoom.symbio.store.dispatch.DispatcherControl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class ${dispatcherName} implements Dispatcher<Dispatchable<Entry<String>, State.TextState>> {

  private AccessSafely access;

  private List<String> states = new CopyOnWriteArrayList<>();

  public ${dispatcherName}() {
    super();
    this.access = afterCompleting(0);
  }

  public AccessSafely afterCompleting(final int times) {
    access = AccessSafely
      .afterCompleting(times)
      .writingWith("stored", (String type) -> states.add(type))
      .readingWith("storedAt", (Integer index) -> states.get(index))
      .readingWith("storeCount", () -> states.size());

    return access;
  }

  @Override
  public void controlWith(final DispatcherControl control) {

  }

  @Override
  public void dispatch(final Dispatchable<Entry<String>, State.TextState> dispatchable) {
    access.writeUsing("stored", dispatchable.state().get().type);
  }
}
