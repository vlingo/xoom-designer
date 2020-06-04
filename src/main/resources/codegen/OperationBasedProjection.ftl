package ${packageName};

<#list imports as import>
import ${import.qualifiedClassName};
</#list>

import io.vlingo.lattice.model.projection.Projectable;
import io.vlingo.lattice.model.projection.StateStoreProjectionActor;

public class ${projectionName} extends StateStoreProjectionActor<${dataName}> {
  private String becauseOf;

  public ${projectionName}() {
    super(${storeProviderName}.instance().store);
  }

  @Override
  protected ${dataName} currentDataFor(Projectable projectable) {
    becauseOf = projectable.becauseOf()[0];

    // TODO: set state and current
    final ${stateName} state = projectable.object();
    final ${dataName} current = ${dataName}.from(state);

    return current;
  }

  @Override
  protected ${dataName} merge(${dataName} previousData, int previousVersion, ${dataName} currentData, int currentVersion) {
    ${dataName} merged;

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
