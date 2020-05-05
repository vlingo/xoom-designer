package io.vlingo.xoom.starter.task;

import java.io.File;

public class OutputResource {

    public final File file;
    public final String content;

    private OutputResource(final File file, final String content) {
        this.file = file;
        this.content = content;
    }

    public static OutputResource of(final File file, final String content) {
        return new OutputResource(file, content);
    }
}
