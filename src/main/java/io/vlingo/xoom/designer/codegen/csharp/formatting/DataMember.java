package io.vlingo.xoom.designer.codegen.csharp.formatting;

import io.vlingo.xoom.codegen.dialect.Dialect;

public class DataMember extends Member {
  public DataMember(Dialect lang, String dataObjectNameSuffix) {
    super(lang, dataObjectNameSuffix);
    FIELD_MEMBER_INSTANTIATION = "%s {get; set;} = %s;";
    FIELD_MEMBER_DECLARATION = "%s {get; set;}";
  }
}
