package io.vlingo.xoom.starter;

public class ArgumentNotFoundException extends RuntimeException {

    public ArgumentNotFoundException(final String argumentName) {
        super(argumentName + " not found");
    }

}
