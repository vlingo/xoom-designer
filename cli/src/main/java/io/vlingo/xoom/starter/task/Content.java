package io.vlingo.xoom.starter.task;

import io.vlingo.xoom.starter.task.template.TemplateGenerationException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Content {

    public final Object subject;
    public final File file;
    public final String text;

    private Content(final Object subject,
                    final File file,
                    final String text) {
        this.subject = subject;
        this.file = file;
        this.text = text;
    }

    public static Content with(final Object subject,
                               final File file,
                               final String text) {
        return new Content(subject, file, text);
    }

    public void create() {
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
            Files.write(file.toPath(), text.getBytes());
        } catch (IOException e) {
            throw new TemplateGenerationException(e);
        }
    }

    public boolean isAbout(final Object subject) {
        return this.subject.equals(subject);
    }

}
