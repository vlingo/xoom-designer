package io.vlingo.xoom.designer.codegen.java.unittest.resource;

import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.content.ContentQuery;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.TemplateParameter;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.util.List;

public class RestResourceUnitTestTemplateData extends TemplateData {
	private final String packageName;
	private final String aggregateName;
	private final TemplateParameters parameters;

	public RestResourceUnitTestTemplateData(String basePackage, CodeGenerationParameter aggregateParameter,
	                                        List<Content> contents, List<CodeGenerationParameter> valueObjects) {
		this.aggregateName = aggregateParameter.value;
		this.packageName = resolvePackage(basePackage);
		this.parameters = loadParameters(aggregateParameter, contents, valueObjects);
	}

	private TemplateParameters loadParameters(final CodeGenerationParameter aggregate, final List<Content> contents,
	                                          List<CodeGenerationParameter> valueObjects) {
		final String dataObjectName = JavaTemplateStandard.DATA_OBJECT.resolveClassname(aggregate.value);

		return TemplateParameters.with(TemplateParameter.PACKAGE_NAME, packageName)
				.and(TemplateParameter.REST_RESOURCE_UNIT_TEST_NAME, standard().resolveClassname(aggregateName))
				.and(TemplateParameter.DATA_OBJECT_NAME, dataObjectName)
				.and(TemplateParameter.URI_ROOT, aggregate.retrieveRelatedValue(Label.URI_ROOT))
				.and(TemplateParameter.TEST_CASES, TestCase.from(aggregate, valueObjects))
				.addImport(resolveImports(dataObjectName, contents))
				.and(TemplateParameter.PRODUCTION_CODE, false)
				.and(TemplateParameter.UNIT_TEST, true);
	}

	private String resolveImports(final String dataObjectName, final List<Content> contents) {
		final String dataObjectPackage =
				ContentQuery.findPackage(JavaTemplateStandard.DATA_OBJECT, dataObjectName, contents);

		final CodeElementFormatter codeElementFormatter =
						ComponentRegistry.withName("defaultCodeFormatter");

		return codeElementFormatter.importAllFrom(dataObjectPackage);
	}

	private String resolvePackage(final String basePackage) {
		return String.format("%s.%s.%s", basePackage, "infrastructure", "resource");
	}

	@Override
	public TemplateStandard standard() {
		return JavaTemplateStandard.REST_RESOURCE_UNIT_TEST;
	}

	@Override
	public TemplateParameters parameters() {
		return parameters;
	}

	@Override
	public String filename() {
		return standard().resolveFilename(aggregateName, parameters);
	}
}
