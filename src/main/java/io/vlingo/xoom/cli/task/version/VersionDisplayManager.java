package io.vlingo.xoom.cli.task.version;

import io.vlingo.xoom.cli.task.Task;
import io.vlingo.xoom.cli.task.TaskManager;

import java.util.List;

import static io.vlingo.xoom.cli.task.Task.K8S;
import static io.vlingo.xoom.cli.task.Task.VERSION;

public class VersionDisplayManager implements TaskManager<List<String>> {

  @Override
  public void run(final List<String> args) {
    System.out.println(getClass().getPackage().getImplementationVersion());
  }

}
