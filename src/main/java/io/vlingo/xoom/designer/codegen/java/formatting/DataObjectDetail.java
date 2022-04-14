// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.formatting;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.java.model.FieldDetail;

public class DataObjectDetail {

  public static final String DATA_OBJECT_NAME_SUFFIX = "Data";

  public static String resolveCollectionType(final CodeGenerationParameter field)  {
    final String collectionType = FieldDetail.resolveCollectionType(field);
    return adaptCollectionGenericsType(collectionType);
  }

  public static String adaptCollectionGenericsType(final String collectionType) {
    final int genericsTypeIndex = collectionType.indexOf("<") + 1;
    final String genericsType = collectionType.substring(genericsTypeIndex, collectionType.length()-1);
    if(FieldDetail.isScalar(genericsType) || FieldDetail.isDateTime(genericsType)) {
      return collectionType;
    }
    return collectionType.replace(genericsType, genericsType + DATA_OBJECT_NAME_SUFFIX);
  }

  public static Boolean isValidSuffix(final String suffix) {
    return DATA_OBJECT_NAME_SUFFIX.equals(suffix);
  }
}
