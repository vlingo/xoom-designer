package io.vlingo.xoom.starter.codegen;

public class RestResourceCodeGenerator extends BaseGenerator {

    private static RestResourceCodeGenerator instance;

    private RestResourceCodeGenerator() {
    }

    public String generate(final String aggregateName,
                           final String packageName) {
        this.template = CodeTemplate.REST_RESOURCE.filename;
        input.put("aggregateName", aggregateName);
        input.put("packageName", packageName);
        return generate();
    }

    public static RestResourceCodeGenerator instance() {
        if(instance == null) {
            instance = new RestResourceCodeGenerator();
        }
        return instance;
    }

}
