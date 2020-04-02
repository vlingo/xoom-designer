package io.vlingo.xoomstarter.template;

public class ShellCommandExecutor {

    private static final String ARCHETYPE_GENERATION_COMMAND =
            "cmd.exe /c cd D:\\temp && mvn archetype:generate -B " +
                    "-DarchetypeGroupId=org.apache.maven.archetypes " +
                    "-DarchetypeArtifactId=maven-archetype-quickstart " +
                    "-DarchetypeVersion=1.1 " +
                    "-DgroupId=io.vlingo " +
                    "-DartifactId=starter-example " +
                    "-Dversion=1.0-SNAPSHOT " +
                    "-Dpackage=io.vlingo.starterexample ";


}
