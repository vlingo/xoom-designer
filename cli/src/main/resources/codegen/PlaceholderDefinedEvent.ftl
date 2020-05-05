package ${packageName};

import io.vlingo.lattice.model.DomainEvent;

public final class ${domainEventName} extends DomainEvent {
  public final String id;
  public final String placeholderValue;

  public ${domainEventName}(final String id, final String placeholderValue) {
    this.id = id;
    this.placeholderValue = placeholderValue;
  }
}
