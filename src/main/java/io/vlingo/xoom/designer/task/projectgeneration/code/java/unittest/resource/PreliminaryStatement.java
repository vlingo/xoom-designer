// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.java.unittest.resource;

import java.util.Collections;
import java.util.List;

public class PreliminaryStatement {

	private static final String REST_ASSURED_READ = "%s result = given()\r\n" +
			"        .when()\r\n" +
			"        .get(\"%s\")\r\n" +
			"        .then()\r\n" +
			"        .statusCode(200)\r\n" +
			"        .extract()\r\n" +
			"        .body()\r\n" +
			"        .as(%s.class);\r\n";
	private static final String REST_ASSURED_READ_ALL = "%s[] result = given()\r\n" +
			"        .when()\r\n" +
			"        .get(\"%s\")\r\n" +
			"        .then()\r\n" +
			"        .statusCode(200)\r\n" +
			"        .extract()\r\n" +
			"        .body()\r\n" +
			"        .as(%s[].class);\r\n";
	private static final String REST_ASSURED_WRITE = "%s result = given()\r\n" +
			"        .when()\r\n" +
			"        .body(%s)\r\n" +
			"        .%s(\"%s\")\r\n" +
			"        .then()\r\n" +
			"        .statusCode(%s)\r\n" +
			"        .extract()\r\n" +
			"        .body()\r\n" +
			"        .as(%s.class);\r\n";
	private static final String REST_ASSURED_DELETE = "given()\r\n" +
			"        .when()\r\n" +
			"        .%s(\"%s\")\r\n" +
			"        .then()\r\n" +
			"        .statusCode(200);\r\n";

	public static List<String> with(final String aggregateUriRoot, final String valueObjectData, final String rootPath, final String rootMethod) {
		final String testDataVariableName = TestDataFormatter.formatLocalVariableName(1);
		String rootPathWithId = rootPath.replace("{id}", "\"+" + testDataVariableName + ".id" + "+\"");
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
