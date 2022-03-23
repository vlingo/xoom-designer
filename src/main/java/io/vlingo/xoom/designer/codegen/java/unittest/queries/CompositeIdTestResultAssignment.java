package io.vlingo.xoom.designer.codegen.java.unittest.queries;

import io.vlingo.xoom.common.TriFunction;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.storage.QueriesDetail;

class CompositeIdTestResultAssignment {

  private static final CompositeIdTestResultAssignment QUERY_BY_COMPOSITE_ID_TEST_CASE_EXECUTION =
      new CompositeIdTestResultAssignment(CompositeIdTestResultAssignment::resolveQueryByIdResultAssignment);

  private static final CompositeIdTestResultAssignment QUERY_ALL_COMPOSITE_ID_TEST_CASE_EXECUTION =
      new CompositeIdTestResultAssignment(CompositeIdTestResultAssignment::resolveQueryAllResultAssignment,
          CompositeIdTestResultAssignment::resolveQueryAllFilteredResultAssignment);

  private final TriFunction<Integer, String, String, String> mainResultFormatting;
  private final TriFunction<Integer, String, String, String> filteredResultFormatting;

  public CompositeIdTestResultAssignment(TriFunction<Integer, String, String, String> resultAssignmentPattern) {
    this(resultAssignmentPattern, (d, a, b) -> "");
  }

  public static CompositeIdTestResultAssignment forMethod(final String testCaseMethod) {
    if (TestCaseName.ofMethod(testCaseMethod).equals(TestCaseName.QUERY_BY_ID)) {
      return QUERY_BY_COMPOSITE_ID_TEST_CASE_EXECUTION;
    }
    return QUERY_ALL_COMPOSITE_ID_TEST_CASE_EXECUTION;
  }

  private CompositeIdTestResultAssignment(final TriFunction<Integer, String, String, String> resultAssignmentResolver,
                                          final TriFunction<Integer, String, String, String> filteredResultAssignmentPattern) {
    this.mainResultFormatting = resultAssignmentResolver;
    this.filteredResultFormatting = filteredResultAssignmentPattern;
  }

  public String formatMainResult(final int dataIndex,final String compositeId, final String aggregateName) {
    return mainResultFormatting.apply(dataIndex, compositeId, aggregateName);
  }

  public String formatFilteredResult(final int dataIndex,final String compositeId, final String aggregateName) {
    return filteredResultFormatting.apply(dataIndex, compositeId, aggregateName);
  }

  private static String resolveQueryByIdResultAssignment(final int dataIndex, final String compositeId, final String aggregateName) {
    final String dataObjectName =
        JavaTemplateStandard.DATA_OBJECT.resolveClassname(aggregateName);

    final String queryMethodName =
        QueriesDetail.resolveQueryByIdMethodName(aggregateName);

    final String variableName =
        TestDataFormatter.formatLocalVariableName(dataIndex);

    return String.format("final %s %s = queries.%s(%s).await();",
        dataObjectName, variableName, queryMethodName, compositeId + ", \"" + dataIndex + "\"");
  }

  private static String resolveQueryAllResultAssignment(final int dataIndex, final String compositeId, final String aggregateName) {
    final String dataObjectName =
        JavaTemplateStandard.DATA_OBJECT.resolveClassname(aggregateName);

    final String queryMethodName =
        QueriesDetail.resolveQueryAllMethodName(aggregateName);

    if (dataIndex > 1)
      return String.format("results = queries.%s(%s).await();", queryMethodName, compositeId);

    return String.format("final Collection<%s> results = queries.%s(%s).await();", dataObjectName, queryMethodName, compositeId);
  }

  private static String resolveQueryAllFilteredResultAssignment(final int dataIndex, final String compositeId, final String aggregateName) {
    final String dataObjectName =
        JavaTemplateStandard.DATA_OBJECT.resolveClassname(aggregateName);

    final String variableName =
        TestDataFormatter.formatLocalVariableName(dataIndex);

    return String.format("final %s %s = results.stream().filter(data -> data.id.equals(\"%s\")).findFirst().orElseThrow(RuntimeException::new);",
        dataObjectName, variableName, dataIndex);
  }
}