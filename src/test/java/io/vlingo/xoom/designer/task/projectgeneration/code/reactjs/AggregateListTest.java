package io.vlingo.xoom.designer.task.projectgeneration.code.reactjs;

public class AggregateListTest {

//    @Test
//    public void test1() throws TemplateException, IOException {
//        final AggregateData aggregateData = new AggregateData(
//                "Pet",
//                new APIData(
//                        "/pets",
//                        asList(
//                                new RouteData("/", "POST", "register", false),
//                                new RouteData("/{id}/rename", "PATCH", "rename", true)
//                        )
//                ),
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
//        String result = generate("AggregateList", new AggregateListArguments(aggregateData, Collections.EMPTY_MAP));
//
//        assertTrue(result.contains("import PetRegister from \"./PetRegister\";"));
//        assertTrue(result.contains("const EMPTY_FORM = { name: '' };"));
//        assertTrue(result.contains("const Pets = () => {"));
//        assertTrue(containsPattern(Pattern.compile("<button type=\"button\" className=\"btn btn-sm btn-outline-secondary\" onClick=\\{_register}>Register</button>"), result));
//        assertTrue(result.contains("axios.get('/pets')"));
//        assertTrue(result.contains("const _register = useCallback((e) => {"));
//        assertTrue(result.contains("setCurrentModal(<PetRegister "));
//        assertTrue(result.contains("<h1 className=\"h2\">Pets</h1>"));
//        assertTrue(containsPattern(Pattern.compile("<tr>\\s*<th>Id</th>\\s*<th>Name</th>\\s*</tr>"), result));
//        assertTrue(containsPattern(Pattern.compile("<Link to=\\{\"/app/pets/\"\\+item\\.id}>\\{item\\.id} </Link>"), result));
//        assertTrue(result.contains("<td>{item?.name}</td>"));
//        assertTrue(result.contains("export default Pets;"));
//    }
//
//    @Test
//    public void test2() throws TemplateException, IOException {
//        final AggregateData aggregateData = new AggregateData(
//                "Customer",
//                new APIData(
//                        "/customers",
//                        asList(
//                                new RouteData("/", "POST", "register", false),
//                                new RouteData("/{id}/rename", "PATCH", "rename", true),
//                                new RouteData("/{id}/contact", "PATCH", "changeContact", true)
//                        )
//                ),
//                asList(
//                        new DomainEventData("CustomerRegistered", asList("name", "contact")),
//                        new DomainEventData("CustomerRenamed", asList("name")),
//                        new DomainEventData("CustomerContactChanged", asList("contact"))
//                ),
//                asList(
//                        new StateFieldData("id", "string", null),
//                        new StateFieldData("name", "string", null),
//                        new StateFieldData("contact", "Contact", null)
//                ),
//                asList(
//                        new AggregateMethodData("register", asList("name", "contact"), true, "CustomerRegistered"),
//                        new AggregateMethodData("rename", asList("name"), false, "CustomerRenamed"),
//                        new AggregateMethodData("changeContact", asList("contact"), false, "CustomerRenamed")
//                ),
//                null,
//                null
//        );
//
//        Map<String, List<ValueObjectFieldData>> valuStringListMap = new HashMap<>();
//        valuStringListMap.put("Contact", asList(
//                new ValueObjectFieldData("phone", "string", null),
//                new ValueObjectFieldData("address", "string", null)
//        ));
//
//        String result = generate("AggregateList", new AggregateListArguments(aggregateData, valuStringListMap));
//
//        assertTrue(result.contains("import CustomerRegister from \"./CustomerRegister\";"));
//        assertTrue(result.contains("const EMPTY_FORM = { name: '', contact: { phone: '', address: '' } };"));
//        assertTrue(result.contains("const Customers = () => {"));
//        assertTrue(containsPattern(Pattern.compile("<button type=\"button\" className=\"btn btn-sm btn-outline-secondary\" onClick=\\{_register}>Register</button>"), result));
//        assertTrue(result.contains("axios.get('/customers')"));
//        assertTrue(result.contains("const _register = useCallback((e) => {"));
//        assertTrue(result.contains("setCurrentModal(<CustomerRegister "));
//        assertTrue(result.contains("<h1 className=\"h2\">Customers</h1>"));
//        assertTrue(containsPattern(Pattern.compile("<tr>\\s*<th>Id</th>\\s*<th>Name</th>\\s*<th>Contact Phone</th>\\s*<th>Contact Address</th>\\s*</tr>"), result));
//        assertTrue(containsPattern(Pattern.compile("<Link to=\\{\"/app/customers/\"\\+item\\.id}>\\{item\\.id} </Link>"), result));
//        assertTrue(result.contains("<td>{item?.name}</td>"));
//        assertTrue(result.contains("<td>{item?.contact?.phone}</td>"));
//        assertTrue(result.contains("<td>{item?.contact?.address}</td>"));
//        assertTrue(result.contains("export default Customers;"));
//    }
}
