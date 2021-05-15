package io.vlingo.xoom.designer.task.projectgeneration.gui.request;

import io.vlingo.xoom.actors.testkit.AccessSafely;
import io.vlingo.xoom.symbio.Entry;
import io.vlingo.xoom.symbio.State;
import io.vlingo.xoom.symbio.store.dispatch.Dispatchable;
import io.vlingo.xoom.symbio.store.dispatch.Dispatcher;
import io.vlingo.xoom.symbio.store.dispatch.DispatcherControl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public final class MockDispatcher implements Dispatcher<Dispatchable<Entry<String>, State<?>>> {

  private AccessSafely access;
  private AtomicInteger dispatched = new AtomicInteger(0);
  private List<Entry<String>> entries = new CopyOnWriteArrayList<>();

  public MockDispatcher() {
    super();
    this.access = afterCompleting(0);
  }

  public AccessSafely afterCompleting(final int times) {
    access = AccessSafely
      .afterCompleting(times)
      .writingWith("dispatched", (Integer increment) -> dispatched.addAndGet(increment))
      .readingWith("dispatched", () -> dispatched.get());

    return access;
  }

  @Override
  public void controlWith(final DispatcherControl control) {
  }

  @Override
  public void dispatch(final Dispatchable<Entry<String>, State<?>> dispatchable) {
    access.writeUsing("dispatched", 1);
  }
}
