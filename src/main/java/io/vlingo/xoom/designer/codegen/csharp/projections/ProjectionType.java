package io.vlingo.xoom.designer.codegen.csharp.projections;

public enum ProjectionType {

  NONE("None"),
  CUSTOM("Custom"),
  EVENT_BASED("Event"),
  OPERATION_BASED("Operation");

  public final String sourceName;

  ProjectionType(final String sourceName) {
    this.sourceName = sourceName;
  }

  public static ProjectionType of(final String projection) {
    if(projection.isEmpty())
      return NONE;
    return valueOf(projection.toUpperCase());
  }

  public boolean isEventBased() {
    return equals(EVENT_BASED);
  }

  public boolean isOperationBased() {
    return equals(OPERATION_BASED);
  }

  public boolean isProjectionEnabled() {
    return !equals(NONE);
  }
}
