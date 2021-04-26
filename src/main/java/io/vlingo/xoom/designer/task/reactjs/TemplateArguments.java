package io.vlingo.xoom.designer.task.reactjs;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface TemplateArguments {

    default Map<String, Object> toMap() {
        return Stream.of(getClass().getFields())
                .collect(Collectors.toMap(
                    Field::getName,
                    field -> {
                        try {
                            return field.get(this);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException("Can't access template argument " + getClass().getName()
                                                            + "'s property " + field.getName(), e);
                        }
                    }
                ));
    }
}
