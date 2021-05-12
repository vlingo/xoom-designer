// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.resource;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.ROUTE_PATH;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.URI_ROOT;


public class PathFormatter {

  public static String formatAbsoluteRoutePath(final CodeGenerationParameter routeParameter) {
    final String routePath = routeParameter.retrieveRelatedValue(ROUTE_PATH);
    final String uriRoot = routeParameter.parent().retrieveRelatedValue(URI_ROOT);
    return formatAbsoluteRoutePath(uriRoot, routePath);
  }

  public static String formatRelativeRoutePath(final CodeGenerationParameter routeParameter) {
    final String routePath = routeParameter.retrieveRelatedValue(ROUTE_PATH);
    if (routePath.isEmpty() || removeSurplusesSlashes(routePath).equals("/")) {
      return "";
    }
    if (routePath.endsWith("/")) {
      return routePath.substring(0, routePath.length() - 1);
    }
    return routePath;
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
      return routePath;
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
    return cleanPath;
  }

}
