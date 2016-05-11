package graphql.java.generator.type;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.Scalars;
import graphql.java.generator.*;
import graphql.schema.*;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@SuppressWarnings({"unchecked", "serial"})
public class TypeGeneratorListOfListTest {
    private static Logger logger = LoggerFactory.getLogger(
            TypeGeneratorListOfListTest.class);

    ITypeGenerator generator = DefaultBuildContext.reflectionContext;

    @Test
    public void testGeneratedListOfList() {
        logger.debug("testGeneratedListOfList");
        Object objectType = generator.getOutputType(ClassWithListOfList.class);
        assertClassWithListOfList(objectType);
    }

    @Before
    public void before() {
        DefaultBuildContext.defaultTypeRepository.clear();
    }

    public static void assertClassWithListOfList(Object objectType) {
        Assert.assertThat(objectType, instanceOf(GraphQLObjectType.class));
        Assert.assertThat(objectType, not(instanceOf(GraphQLList.class)));
        GraphQLFieldDefinition field = ((GraphQLObjectType) objectType)
                .getFieldDefinition("listOfListOfInts");

        Assert.assertThat(field, notNullValue());
        GraphQLOutputType outputType = field.getType();
        assertListOfListOfInt(outputType);
    }

    @Test
    public void testGeneratedList() {
        logger.debug("testGeneratedList");
        Object objectType = generator.getOutputType(ClassWithList.class);
        assertListOfInt(objectType, "integerList");
    }

    public static void assertListOfInt(Object objectType, String fieldName) {
        Assert.assertThat(objectType, instanceOf(GraphQLObjectType.class));
        Assert.assertThat(objectType, not(instanceOf(GraphQLList.class)));
        GraphQLFieldDefinition fieldDefinition = ((GraphQLObjectType) objectType).getFieldDefinition(fieldName);
        Assert.assertThat(fieldDefinition, notNullValue());
        GraphQLOutputType outputType = fieldDefinition.getType();

        Assert.assertThat(outputType, CoreMatchers.instanceOf(GraphQLList.class));
        GraphQLType wrappedType = ((GraphQLList) outputType).getWrappedType();
        Assert.assertThat(wrappedType, instanceOf(GraphQLScalarType.class));
    }

    @Test
    public void testArray() {
        logger.debug("testArray");
        Object objectType = generator.getOutputType(ClassWithArray.class);
        assertListOfInt(objectType, "integerArray");
    }

    @Test
    public void testGeneratedListOfListOfList() {
        logger.debug("testGeneratedListOfListOfList");
        Object objectType = generator.getOutputType(ClassWithListOfListOfList.class);
        Assert.assertThat(objectType, instanceOf(GraphQLObjectType.class));
        Assert.assertThat(objectType, not(instanceOf(GraphQLList.class)));
        GraphQLFieldDefinition field = ((GraphQLObjectType) objectType)
                .getFieldDefinition("listOfListOfListOfInts");

        Assert.assertThat(field, notNullValue());
        GraphQLOutputType listType = field.getType();
        Assert.assertThat(listType, instanceOf(GraphQLList.class));
        GraphQLType wrappedType = ((GraphQLList) listType).getWrappedType();
        assertListOfListOfInt(wrappedType);
    }

    public static void assertListOfListOfInt(GraphQLType type) {
        Assert.assertThat(type, instanceOf(GraphQLList.class));
        GraphQLType wrappedType = ((GraphQLList) type).getWrappedType();
        Assert.assertThat(wrappedType, instanceOf(GraphQLList.class));
        GraphQLType integerType = ((GraphQLList) wrappedType).getWrappedType();
        Assert.assertThat(integerType, instanceOf(GraphQLScalarType.class));
    }

    @Test
    public void testCanonicalListOfList() {
        logger.debug("testCanonicalListOfList");
        List<List<Integer>> listOfListOfInts = new ArrayList<List<Integer>>() {{
            add(new ArrayList<Integer>() {{
                add(0);
            }});
        }};

        GraphQLObjectType queryType = newObject()
                .name("testQuery")
                .field(newFieldDefinition()
                        .type(new GraphQLList(new GraphQLList(Scalars.GraphQLInt)))
                        .name("testObj")
                        .staticValue(listOfListOfInts)
                        .build())
                .build();
        GraphQLSchema listTestSchema = GraphQLSchema.newSchema()
                .query(queryType)
                .build();

        String queryString =
        "{" +
        "  testObj" +
        "}";
        ExecutionResult queryResult = new GraphQL(listTestSchema).execute(queryString);
        assertThat(queryResult.getErrors(), is(empty()));
        Map<String, Object> resultMap = (Map<String, Object>) queryResult.getData();
        logger.debug("testCanonicalListOfList resultMap {}",
                TypeGeneratorTest.prettyPrint(resultMap));
        Object testObj = resultMap.get("testObj");
        Assert.assertThat(testObj, instanceOf(List.class));
        assertThat(((List<List<Integer>>) testObj),
                equalTo(listOfListOfInts));

    }
}
