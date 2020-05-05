package io.vlingo.xoom.starter.codegen;

public class AggregateProtocolCodeGenerator extends BaseGenerator {

    private static AggregateProtocolCodeGenerator instance;

    private AggregateProtocolCodeGenerator() {
        this.template = CodeTemplate.AGGREGATE_PROTOCOL.filename;
    }

    public String generate(final String aggregateProtocolName,
                           final String packageName) {
        input.put("aggregateProtocolName", aggregateProtocolName);
        input.put("packageName", packageName);
        return generate();
    }

    public static AggregateProtocolCodeGenerator instance() {
        if(instance == null) {
            instance = new AggregateProtocolCodeGenerator();
        }
        return instance;
    }

}
