package io.vlingo.xoom.designer.cli.version;

import io.vlingo.xoom.designer.cli.TaskManager;

import java.util.List;

public class VersionDisplayManager implements TaskManager<List<String>> {

  @Override
  public void run(final List<String> args) {
    System.out.println(getClass().getPackage().getImplementationVersion());
  }

}
