package io.vlingo.xoom.starter.archetype;

public class ArchetypeNotFoundException extends RuntimeException {

    public ArchetypeNotFoundException() {
        super("Unable to find a Template based on properties. Please check if it contains all required properties.");
    }
}
