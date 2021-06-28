// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.java.unittest.resource;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.unittest.TestDataValueGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class TestCase {

    public static final int TEST_DATA_SET_SIZE = 2;

    private final String methodName;
    private final String dataDeclaration;
    private final List<TestStatement> statements = new ArrayList<>();
    private final List<String> preliminaryStatements = new ArrayList<>();
    private final String rootMethod;

    public static List<TestCase> from(final CodeGenerationParameter aggregate, List<CodeGenerationParameter> valueObjects) {
        return aggregate.retrieveAllRelated(Label.ROUTE_SIGNATURE)
                .map(signature -> new TestCase(signature, aggregate, valueObjects))
                .collect(Collectors.toList());
    }

    private TestCase(final CodeGenerationParameter signature, final CodeGenerationParameter aggregate,
                     List<CodeGenerationParameter> valueObjects) {
        final TestDataValueGenerator.TestDataValues testDataValues = TestDataValueGenerator
                .with(TEST_DATA_SET_SIZE, "data", aggregate, valueObjects).generate();

        final String dataObjectType = JavaTemplateStandard.DATA_OBJECT.resolveClassname(aggregate.value);
        this.methodName = signature.value;
        this.dataDeclaration = DataDeclaration.generate(signature.value, aggregate, valueObjects, testDataValues);
        this.rootMethod = signature.retrieveRelatedValue(Label.ROUTE_METHOD).toLowerCase(Locale.ROOT);
        this.preliminaryStatements.addAll(PreliminaryStatement.with(aggregate.retrieveRelatedValue(Label.URI_ROOT), dataObjectType, rootPath(signature, aggregate), rootMethod));
        this.statements.addAll(TestStatement.with(rootPath(signature, aggregate), rootMethod, aggregate, valueObjects, testDataValues));
    }

    private String rootPath(CodeGenerationParameter signature, CodeGenerationParameter aggregate) {
        String uriRoot = aggregate.retrieveRelatedValue(Label.URI_ROOT);
        return signature.retrieveRelatedValue(Label.ROUTE_PATH).startsWith(uriRoot) ? signature.retrieveRelatedValue(Label.ROUTE_PATH) : uriRoot + signature.retrieveRelatedValue(Label.ROUTE_PATH);
    }

    public String getMethodName() {
        return methodName;
    }

    public String getRootMethod() {
        return rootMethod;
    }

    public boolean isGetRootMethod(){
        return !getRootMethod().equals("post");
    }
    public String getDataDeclaration() {
        return dataDeclaration;
    }

    public List<TestStatement> getStatements() {
        return statements;
    }

    public List<String> getPreliminaryStatements() {
        return preliminaryStatements;
    }


}