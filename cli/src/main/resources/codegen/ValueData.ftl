package ${packageName};

import java.util.ArrayList;
import java.util.List;

<#list imports as import>
import ${import.fullyQualifiedClassName};
</#list>

public class ${type}Data {
  public final String id;
  public final String placeholderValue;

  public static ${type}Data empty() {
    return new ${type}Data("", "");
  }

  public static ${type}Data from(final ${type}State state) {
    return new ${type}Data(state.id, state.placeholderValue);
  }

  public static List<${type}Data> from(final List<${type}State> states) {
    final List<${type}Data> data = new ArrayList<>(states.size());

    for (final ${type}State state : states) {
      data.add(${type}Data.from(state));
    }

    return data;
  }

  public static ${type}Data from(final String id, final String placeholderValue) {
    return new ${type}Data(id, placeholderValue);
  }

  public static ${type}Data just(final String placeholderValue) {
    return new ${type}Data("", placeholderValue);
  }

  @Override
  public String toString() {
    return "${type}Data [id=" + id + " placeholderValue=" + placeholderValue + "]";
  }

  private ${type}Data(final String id, final String placeholderValue) {
    this.id = id;
    this.placeholderValue = placeholderValue;
  }
}
