package ${packageName};

import io.vlingo.symbio.store.object.StateObject;

public final class ${stateClass} extends StateObject {
  public final String id;
  private String placeholderValue;

  public static ${stateClass} identifiedBy(final String id) {
    return new ${stateClass}(id);
  }

  public boolean doesNotExist() {
    return id == null;
  }

  public boolean isIdentifiedOnly() {
    return id != null && placeholderValue == null;
  }

  public String getId() {
    return id;
  }

  public String getPlaceholderValue() {
    return placeholderValue;
  }

  public ${stateClass} withPlaceholderValue(final String value) {
    return new ${stateClass}(id, value);
  }

  ${stateClass}(final long persistenceId, final long version, final String id, final String value) {
    super(persistenceId, version);
    this.id = id;
    this.placeholderValue = value;
  }

  ${stateClass}(final String id, final String value) {
    this.id = id;
    this.placeholderValue = value;
  }

  private ${stateClass}(final String id) {
    this(Unidentified, 0, id, null);
  }

  private ${stateClass}() {
    this(Unidentified, 0, null, null);
  }
}
