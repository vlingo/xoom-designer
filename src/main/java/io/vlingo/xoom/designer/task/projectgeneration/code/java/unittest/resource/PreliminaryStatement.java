// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.java.unittest.resource;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PreliminaryStatement {

  private static final String REST_ASSURED_READ = "%s result = given()%n" +
      "        .when()%n" +
      "        .get(\"%s\")%n" +
      "        .then()%n" +
      "        .statusCode(200)%n" +
      "        .extract()%n" +
      "        .body()%n" +
      "        .as(%s.class);%n";
  private static final String REST_ASSURED_READ_ALL = "%s[] result = given()%n" +
      "        .when()%n" +
      "        .get(\"%s\")%n" +
      "        .then()%n" +
      "        .statusCode(200)%n" +
      "        .extract()%n" +
      "        .body()%n" +
      "        .as(%s[].class);%n";
  private static final String REST_ASSURED_WRITE = "%s result = given()%n" +
      "        .when()%n" +
      "        .body(%s)%n" +
      "        .%s(\"%s\")%n" +
      "        .then()%n" +
      "        .statusCode(%s)%n" +
      "        .extract()%n" +
      "        .body()%n" +
      "        .as(%s.class);%n";
  private static final String REST_ASSURED_DELETE = "given()%n" +
      "        .when()%n" +
      "        .%s(\"%s\")%n" +
      "        .then()%n" +
      "        .statusCode(200);%n";

  public static List<String> with(final String aggregateUriRoot, final String valueObjectData, final String rootPath, final String rootMethod) {
    final String testDataVariableName = TestDataFormatter.formatLocalVariableName(1);
    String rootPathWithId = replaceRootPathWithIds(rootPath, testDataVariableName);

    switch (rootMethod) {
      case "get":
        if (rootPath.equals(aggregateUriRoot))
          return fetchAll(valueObjectData, rootPathWithId);
        return fetchById(valueObjectData, rootPathWithId);
      case "delete":
        return deleteById(rootPathWithId, rootMethod);
      default:
        return writeData(valueObjectData, rootPathWithId, rootMethod, testDataVariableName);
    }
  }

  private static String replaceRootPathWithIds(String rootPath, String testDataVariableName) {
    final String regex = "(\\{([^{]+)})";
    final Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(rootPath);
    String result = rootPath;
    while (matcher.find()) {
      String replaceWith = "\"+" + testDataVariableName + "." + matcher.group(2) + "+\"";
      result = result.replace(matcher.group(0), replaceWith);
    }
    return result;
  }

  private static List<String> writeData(String valueObjectData, String rootPath, String rootMethod, String testDataVariableName) {
    return Collections.singletonList(String.format(REST_ASSURED_WRITE, valueObjectData, testDataVariableName, rootMethod,
        rootPath, rootMethod.equals("post") ? 201 : 200, valueObjectData));
  }

  private static List<String> deleteById(String rootPath, String rootMethod) {
    return Collections.singletonList(String.format(REST_ASSURED_DELETE, rootMethod, rootPath));
  }

  private static List<String> fetchById(String valueObjectData, String rootPath) {
    return Collections.singletonList(String.format(REST_ASSURED_READ, valueObjectData, rootPath, valueObjectData));
  }

  private static List<String> fetchAll(String valueObjectData, String rootPath) {
    return Collections.singletonList(String.format(REST_ASSURED_READ_ALL, valueObjectData, rootPath, valueObjectData));
  }

}
