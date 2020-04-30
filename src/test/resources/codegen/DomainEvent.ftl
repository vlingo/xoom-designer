package ${packageName};

import io.vlingo.lattice.model.DomainEvent;

public final class ${domainEventName} extends DomainEvent {
  public final String id;

  public ${domainEventName}(final String id) {
    this.id = id;
  }
}
