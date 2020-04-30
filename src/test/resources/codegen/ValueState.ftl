package ${packageName};

public final class ${stateClass} {
  public final String id;
  public final String placeholderValue;

  public static ${stateClass} identifiedBy(final String id) {
    return new ${stateClass}(id);
  }

  public boolean doesNotExist() {
    return id == null;
  }

  public boolean isIdentifiedOnly() {
    return id != null && placeholderValue == null;
  }

  public ${stateClass} withPlaceholderValue(final String value) {
    return new ${stateClass}(this.id, value);
  }

  private ${stateClass}(final String id, final String value) {
    this.id = id;
    this.placeholderValue = value;
  }

  private ${stateClass}(final String id) {
    this(id, null);
  }

  private ${stateClass}() {
    this(null, null);
  }
}
