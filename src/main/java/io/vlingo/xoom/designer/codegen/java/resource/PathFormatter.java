// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.resource;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.Label;


public class PathFormatter {

  public static String formatAbsoluteRoutePath(final CodeGenerationParameter routeParameter) {
    final String routePath = routeParameter.retrieveRelatedValue(Label.ROUTE_PATH);
    final String uriRoot = routeParameter.parent().retrieveRelatedValue(Label.URI_ROOT);
    return formatAbsoluteRoutePath(uriRoot, routePath);
  }

  public static String formatRelativeRoutePath(final CodeGenerationParameter routeParameter) {
    final String routePath = routeParameter.retrieveRelatedValue(Label.ROUTE_PATH);
    final String cleanRoutePath = removeSurplusesSlashes(routePath);
    if (cleanRoutePath.isEmpty() || removeSurplusesSlashes(cleanRoutePath).equals("/")) {
      return "";
    }
    return cleanRoutePath;
  }

  public static String formatRootPath(final String uriRoot) {
    return removeSurplusesSlashes(String.format("/%s", uriRoot));
  }

  public static String formatAbsoluteRoutePath(final String rootPath, final String routePath) {
    if (routePath.isEmpty() || routePath.equals("/")) {
      return rootPath;
    } else if (!routePath.startsWith(rootPath)) {
      return prependRootPath(rootPath, routePath);
    } else {
      return removeSurplusesSlashes(routePath);
    }
  }

  private static String prependRootPath(final String rootPath, final String routePath) {
    return removeSurplusesSlashes(String.format("%s/%s", rootPath, routePath));
  }

  private static String removeSurplusesSlashes(final String path) {
    String cleanPath = path;
    while (cleanPath.contains("//")) {
      cleanPath = cleanPath.replaceAll("//", "/");
    }
    if (cleanPath.length() > 1 && cleanPath.endsWith("/")) {
      return cleanPath.substring(0, cleanPath.length() - 1);
    }
    return cleanPath;
  }

}
