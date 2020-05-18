package ${packageName};

import java.util.ArrayList;
import java.util.List;
import ${stateFullyQualifiedClassName};

public class ${dataName} {
  public final String id;
  public final String placeholderValue;

  public static ${dataName} empty() {
    return new ${dataName}("", "");
  }

  public static ${dataName} from(final ${stateClass} state) {
    return new ${dataName}(state.id, state.placeholderValue);
  }

  public static List<${dataName}> from(final List<${stateClass}> states) {
    final List<${dataName}> data = new ArrayList<>(states.size());

    for (final ${stateClass} state : states) {
      data.add(${dataName}.from(state));
    }

    return data;
  }

  public static ${dataName} from(final String id, final String placeholderValue) {
    return new ${dataName}(id, placeholderValue);
  }

  public static ${dataName} just(final String placeholderValue) {
    return new ${dataName}("", placeholderValue);
  }

  @Override
  public String toString() {
    return "${dataName} [id=" + id + " placeholderValue=" + placeholderValue + "]";
  }

  private ${dataName}(final String id, final String placeholderValue) {
    this.id = id;
    this.placeholderValue = placeholderValue;
  }
}
