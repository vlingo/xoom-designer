package io.vlingo.xoom.designer.task.projectgeneration.code.reactjs;

public class AggregateMethodTest {

//    @Test
//    public void test1() throws Exception {
//        final AggregateMethodArguments arguments = new AggregateMethodArguments(
//                new AggregateMethodData("register", asList("name"), true, "PetRegistered"),
//                new AggregateData(
//                "Pet",
//                        new APIData(
//                    "/pets",
//                        asList(
//                                new RouteData("/", "POST", "register", false),
//                                new RouteData("/{id}/rename", "PATCH", "rename", true)
//                        )
//                ),
//                    asList(
//                            new DomainEventData("PetRegistered", asList("name")),
//                            new DomainEventData("PetRenamed", asList("name"))
//                    ),
//                    asList(new StateFieldData("id", "string", null), new StateFieldData("name", "string", null)),
//                    asList(
//                            new AggregateMethodData("register", asList("name"), true, "PetRegistered"),
//                            new AggregateMethodData("rename", asList("name"), false, "PetRenamed")
//                    ),
//                    null,
//                    null
//            ),
//                Collections.EMPTY_MAP
//        );
//
//        String result = generate("AggregateMethod", arguments);
//
//        assertTrue(result.contains("const PetRegister = ({id = null, defaultForm, complete}) => {"));
//        assertTrue(result.contains("const url = applyData('/pets', form);"));
//        assertTrue(result.contains("axios.post(url, form)"));
//        assertTrue(result.contains("<FormModal title={\"Register\"}"));
//        assertTrue(result.contains("<input id='name' name={'name'}"));
//        assertTrue(result.contains("export default PetRegister;"));
//    }
}
