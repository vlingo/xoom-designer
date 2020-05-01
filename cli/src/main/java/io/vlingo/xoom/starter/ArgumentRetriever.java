package io.vlingo.xoom.starter;

import java.util.List;

public class ArgumentRetriever {

    public static String retrieve(final String argumentName, final List<String> args) {
        final int index = args.indexOf(argumentName);
        if(index > 0 && index + 1 < args.size()) {
            return args.get(index+1);
        }
        throw new ArgumentNotFoundException(argumentName);
    }

}
