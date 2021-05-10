package io.vlingo.xoom.designer.task.reactjs;

import freemarker.template.TemplateException;
import io.vlingo.xoom.designer.task.projectgeneration.restapi.data.*;
import io.vlingo.xoom.turbo.codegen.CodeGenerationException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReactJSProjectGenerator {

    private final ReactJSCodeGenerator codeGenerator;
    private final GenerationSettingsData settings;
    private final TemplateCustomFunctions fns = new TemplateCustomFunctions();

    public ReactJSProjectGenerator(GenerationSettingsData settings) {
        this.settings = settings;
        try {
            this.codeGenerator = new ReactJSCodeGenerator();
        } catch (IOException e) {
            throw new RuntimeException("Can't instantiate ReactJSCodeGenerator", e);
        }
    }

    public void generate() {
        long startTime = System.currentTimeMillis();

        final Map<String, List<ValueObjectFieldData>> valueObjectDataMap = Collections.unmodifiableMap(
                settings.model.valueObjectSettings.stream()
                        .collect(Collectors.toMap(valueObjectData -> valueObjectData.name, o -> o.fields))
        );

        final Path outputRootDir = Paths.get(settings.projectDirectory).resolve(Paths.get("src", "main", "frontend"));
        final Path publicDir = outputRootDir.resolve("public");
        final Path srcDir = outputRootDir.resolve("src");
        final Path componentsDir = srcDir.resolve("components");
        final Path utilsDir = srcDir.resolve("utils");

        try{
            writeFile("gitignore", null, outputRootDir.resolve(".gitignore"));
            writeFile("package.json", new IndexPageArguments(settings.context), outputRootDir.resolve("package.json"));
            writeFile("index.html", new IndexPageArguments(settings.context), publicDir.resolve("index.html"));

            writeFile("index", null, srcDir.resolve("index.js"));
            writeFile("index.css", null, srcDir.resolve("index.css"));
            writeFile("App", new AppArguments(settings.model.aggregateSettings), srcDir.resolve("App.js"));

            writeFile("FormHandler", null, utilsDir.resolve("FormHandler.js"));
            writeFile("Header", new HeaderArguments(settings.context), componentsDir.resolve("Header.js"));
            writeFile("Sidebar", new SidebarArguments(settings.model.aggregateSettings), componentsDir.resolve("Sidebar.js"));
            writeFile("Home", null, componentsDir.resolve("Home.js"));
            writeFile("FormModal", null, componentsDir.resolve("FormModal.js"));
            writeFile("LoadingOrFailed", null, componentsDir.resolve("LoadingOrFailed.js"));

            for (AggregateData aggregateData : settings.model.aggregateSettings) {
                final String pluralAggregateName = fns.makePlural(aggregateData.aggregateName);
                final String capitalizedAggregateName = fns.capitalize(aggregateData.aggregateName);
                final Path aggregateItemRootDir = componentsDir.resolve(fns.decapitalize(pluralAggregateName));

                writeFile(
                        "AggregateDetail",
                        new AggregateDetailArguments(aggregateData, valueObjectDataMap),
                        aggregateItemRootDir.resolve(capitalizedAggregateName+".js")
                );
                writeFile(
                        "AggregateList",
                        new AggregateListArguments(aggregateData, valueObjectDataMap),
                        aggregateItemRootDir.resolve(pluralAggregateName+".js")
                );
                for (AggregateMethodData methodData : aggregateData.methods) {
                    writeFile(
                            "AggregateMethod",
                            new AggregateMethodArguments(methodData, aggregateData, valueObjectDataMap),
                            aggregateItemRootDir.resolve(capitalizedAggregateName + fns.capitalize(methodData.name) + ".js")
                    );
                }
            }
        }catch (IOException | TemplateException e){
            throw new CodeGenerationException(e);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Front-End ReactJS Application generated in " + (endTime - startTime)/1000D + " seconds");
    }

    private void writeFile(String templatePath, Object argument, Path outputPath) throws IOException, TemplateException {
        try(Writer writer = openFileWriter(outputPath)){
            codeGenerator.generateWith(templatePath, argument, writer);
        }
    }

    private Writer openFileWriter(Path path) throws IOException {
        final Path parentDir = path.getParent();
        if (Files.notExists(parentDir)) {
            Files.createDirectories(parentDir);
        } else {
            if (!Files.isDirectory(parentDir)) {
                throw new IOException("Output dir " + path + " is exist but not a dir");
            }
        }
        if (Files.notExists(path)) {
            Files.createFile(path);
        } else {
            if (!Files.isRegularFile(path)) {
                throw new IOException("Output file " + path + " is exist but not a regular file");
            }
        }
        return new FileWriter(path.toFile(), false);
    }
}
