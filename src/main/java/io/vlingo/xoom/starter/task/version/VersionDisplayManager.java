package io.vlingo.xoom.starter.task.version;

import io.vlingo.xoom.starter.task.TaskManager;

import java.util.List;

public class VersionDisplayManager implements TaskManager<List<String>> {

    @Override
    public void run(final List<String> args) {
        System.out.println(getClass().getPackage().getImplementationVersion());
    }

}
