// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java.unittest.resource;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.java.unittest.TestDataValueGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class TestStatement {

	private final List<String> assertions = new ArrayList<>();

	private final List<String> resultAssignment = new ArrayList<>();

	public static List<TestStatement> with(final String rootPath,
	                                       final String rootMethod,
	                                       final CodeGenerationParameter aggregate,
	                                       final List<CodeGenerationParameter> valueObjects,
	                                       final TestDataValueGenerator.TestDataValues testDataValues) {
		if (rootMethod.equals("delete"))
			return Collections.emptyList();
		return Collections.singletonList(new TestStatement(1, rootPath, rootMethod, aggregate,
				valueObjects, testDataValues));
	}

	private TestStatement(final int dataIndex,final String rootPath,
	                      final String rootMethod,
	                      final CodeGenerationParameter aggregate,
	                      final List<CodeGenerationParameter> valueObjects,
	                      final TestDataValueGenerator.TestDataValues testDataValues) {
		this.assertions.addAll(generateAssertions(dataIndex, aggregate, rootPath, rootMethod, valueObjects, testDataValues));
	}

	@SuppressWarnings("unused")
  private List<String> generateExecutions(final int dataIndex,
	                                        final String testMethodName,
	                                        final CodeGenerationParameter aggregate) {
		final TestResultAssignment formatter =
				TestResultAssignment.forMethod(testMethodName);

		return Stream.of(formatter.formatMainResult(dataIndex, aggregate.value),
				formatter.formatFilteredResult(dataIndex, aggregate.value))
				.filter(assignment -> !assignment.isEmpty()).collect(toList());
	}

	private List<String> generateAssertions(final int dataIndex,
	                                        final CodeGenerationParameter aggregate,final String rootPath,
	                                        final String rootMethod, final List<CodeGenerationParameter> valueObjects,
	                                        final TestDataValueGenerator.TestDataValues testDataValues) {
		return Assertions.from(dataIndex, aggregate,  rootPath, rootMethod, valueObjects, testDataValues);
	}

	public List<String> getAssertions() {
		return assertions;
	}

	public List<String> getResultAssignment() {
		return resultAssignment;
	}

}
