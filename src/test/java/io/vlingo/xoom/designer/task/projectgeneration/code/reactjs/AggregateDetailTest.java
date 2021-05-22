package io.vlingo.xoom.designer.task.projectgeneration.code.reactjs;

public class AggregateDetailTest {

//    @Test
//    public void test1() throws TemplateException, IOException {
//        final AggregateData aggregateData = new AggregateData(
//            "Pet",
//            new APIData(
//                    "/pets",
//                    asList(
//                            new RouteData("/", "POST", "register", false),
//                            new RouteData("/{id}/rename", "PATCH", "rename", true)
//                    )
//            ),
//                asList(
//                        new DomainEventData("PetRegistered", asList("name")),
//                        new DomainEventData("PetRenamed", asList("name"))
//                ),
//                asList(new StateFieldData("id", "string", null), new StateFieldData("name", "string", null)),
//                asList(
//                        new AggregateMethodData("register", asList("name"), true, "PetRegistered"),
//                        new AggregateMethodData("rename", asList("name"), false, "PetRenamed")
//                ),
//                null,
//                null
//        );
//
//        StringWriter output = new StringWriter();
//        codeGenerator.generateWith("AggregateDetail", new AggregateDetailArguments(aggregateData, Collections.EMPTY_MAP), output);
//        String result = output.toString();
//
//        assertTrue(result.contains("import PetRename from \"./PetRename\";"));
//        assertTrue(result.contains("const Pet = () => {"));
//        assertTrue(result.contains("axios.get('/pets/'+id)"));
//        assertTrue(result.contains("const _rename = useCallback((e) => {"));
//        assertTrue(result.contains("setCurrentModal(<PetRename "));
//        assertTrue(result.contains("<h1 className=\"h2\">Pet</h1>"));
//        assertTrue(result.contains("<button type=\"button\" className=\"btn btn-sm btn-outline-secondary\" onClick={_rename}>Rename</button>"));
//        assertTrue(containsPattern(Pattern.compile("<tbody>\\s*<tr><td>Id</td><td>\\{item\\?\\.id}</td></tr>\\s*<tr><td>Name</td><td>\\{item\\?\\.name}</td></tr>\\s*</tbody>"), result));
//        assertTrue(result.contains("export default Pet;"));
//    }
}
