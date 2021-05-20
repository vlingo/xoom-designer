package ${packageName}

import java.util.ArrayList
import java.util.List
import java.util.stream.Collectors
import ${stateQualifiedClassName}

public class ${dataName} {
  <#list members as member>
  ${member}
  </#list>

  public companion object {
    public fun from(state: ${stateName}): ${dataName} {
      return ${dataName}(state)
    }

    public fun from(states: List<${stateName}>): List<${dataName}> {
      return states.stream().map(${dataName}::from).collect(Collectors.toList())
    }

    public fun empty(): ${dataName} {
      return ${dataName}(${stateName}.identifiedBy(""))
    }
  }

  constructor (state: ${stateName}) {
    <#list membersAssignment as assignment>
    ${assignment}
    </#list>
  }

}
