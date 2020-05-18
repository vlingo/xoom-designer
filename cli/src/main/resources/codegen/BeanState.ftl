package ${packageName};

import io.vlingo.symbio.store.object.StateObject;

public final class ${stateName} extends StateObject {
  public final String id;
  private String placeholderValue;

  public static ${stateName} identifiedBy(final String id) {
    return new ${stateName}(id);
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

  public ${stateName} withPlaceholderValue(final String value) {
    return new ${stateName}(id, value);
  }

  ${stateName}(final long persistenceId, final long version, final String id, final String value) {
    super(persistenceId, version);
    this.id = id;
    this.placeholderValue = value;
  }

  ${stateName}(final String id, final String value) {
    this.id = id;
    this.placeholderValue = value;
  }

  private ${stateName}(final String id) {
    this(Unidentified, 0, id, null);
  }

  private ${stateName}() {
    this(Unidentified, 0, null, null);
  }
}
