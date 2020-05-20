package ${packageName};

public final class ${stateName} {
  public final String id;
  public final String placeholderValue;

  public static ${stateName} identifiedBy(final String id) {
    return new ${stateName}(id);
  }

  public boolean doesNotExist() {
    return id == null;
  }

  public boolean isIdentifiedOnly() {
    return id != null && placeholderValue == null;
  }

  public ${stateName} withPlaceholderValue(final String value) {
    return new ${stateName}(this.id, value);
  }

  private ${stateName}(final String id, final String value) {
    this.id = id;
    this.placeholderValue = value;
  }

  private ${stateName}(final String id) {
    this(id, null);
  }

  private ${stateName}() {
    this(null, null);
  }
}
