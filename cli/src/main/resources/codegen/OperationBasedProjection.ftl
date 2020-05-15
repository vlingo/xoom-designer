package ${packageName};

<#list imports as import>
import ${import.fullyQualifiedClassName};
</#list>

import io.vlingo.lattice.model.projection.Projectable;
import io.vlingo.lattice.model.projection.StateStoreProjectionActor;

public class ${entityType}ProjectionActor extends StateStoreProjectionActor<${entityType}Data> {
  private String becauseOf;

  public ${entityType}ProjectionActor() {
    super(QueryModelStateStoreProvider.instance().store);
  }

  @Override
  protected ${entityType}Data currentDataFor(Projectable projectable) {
    becauseOf = projectable.becauseOf()[0];

    // TODO: set state and current
    final ${entityType}State state = projectable.object();
    final ${entityType}Data current = ${entityType}Data.from(state);

    return current;
  }

  @Override
  protected ${entityType}Data merge(${entityType}Data previousData, int previousVersion, ${entityType}Data currentData, int currentVersion) {
    ${entityType}Data merged;

    switch (becauseOf) {
    case "CreationCase1":   // TODO: replace with operation text
      merged = currentData; // TODO: confirm creational merge 
      break;
    case "ChangeCase2":     // TODO: replace with operation text
      merged = currentData; // TODO: implement actual merge
      break;
    case "ChangeCase3":     // TODO: replace with operation text
      merged = currentData; // TODO: implement actual merge
      break;
    default:
      merged = currentData;
    }

    return merged;
  }
}
