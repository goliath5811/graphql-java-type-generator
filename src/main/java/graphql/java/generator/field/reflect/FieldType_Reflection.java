package graphql.java.generator.field.reflect;

import graphql.introspection.Introspection.TypeKind;
import graphql.java.generator.UnsharableBuildContextStorer;
import graphql.java.generator.field.strategies.FieldTypeStrategy;
import graphql.schema.GraphQLType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class FieldType_Reflection
        extends UnsharableBuildContextStorer
        implements FieldTypeStrategy {

    private final Logger logger = LoggerFactory.getLogger(FieldType_Reflection.class);

    @Override
    public GraphQLType getTypeOfField(
            Object object, TypeKind typeKind) {
        if (object instanceof Field) {
            Field field = (Field) object;
            Class<?> fieldClazz = field.getType();
            Type genericType = field.getGenericType();
            return getTypeOfFieldFromSignature(
                    fieldClazz, genericType, field.toGenericString(), typeKind);
        }

        if (object instanceof Method) {
            Method method = (Method) object;
            Class<?> returnTypeClazz = method.getReturnType();

            if (returnTypeClazz.isArray()) {
                logger.debug("getTypeOfField(): returnTypeClass is an array");
            }

            Type genericType = method.getGenericReturnType();
            logger.debug("isarray {}", returnTypeClazz.isArray());

            if (returnTypeClazz.isArray()) {
                // Create the array type, and have Java convert it to a List?
//                returnTypeClazz = List.class;
//                genericType = ;
            }
            logger.debug("getTypeOfField(): returnTypeClass: {}, genericType: {}", returnTypeClazz, genericType);

            return getTypeOfFieldFromSignature(
                    returnTypeClazz, genericType, method.toGenericString(), typeKind);
        }
//        if (object instanceof Field) {
//            Field field = (Field) object;
//            Class<?> fieldClazz = field.getType();
//            Type genericType = field.getGenericType();
//            return getTypeOfFieldFromSignature(
//                    fieldClazz, genericType, field.toGenericString(), typeKind);
//        }
//
//        if (object instanceof Method) {
//            Method method = (Method) object;
//            Class<?> returnTypeClazz = method.getReturnType();
//
//            if (returnTypeClazz.isArray()) {
//                logger.debug("getTypeOfField(): returnTypeClass is an array");
//            }
//
//            Type genericType = method.getGenericReturnType();
//            logger.debug("getTypeOfField(): returnTypeClass: {}, genericType: {}", returnTypeClazz, genericType);
//            logger.debug("isarray {}", returnTypeClazz.isArray());
//            return getTypeOfFieldFromSignature(
//                    returnTypeClazz, genericType, method.toGenericString(), typeKind);
//        }

        return null;
    }
    
    protected GraphQLType getTypeOfFieldFromSignature(
            Class<?> typeClazz, Type genericType,
            String name, TypeKind typeKind) {

        ParameterizedType pType = null;
        //attempt GraphQLList from types
        if (genericType instanceof ParameterizedType) {
            pType = (ParameterizedType) genericType;
        }
        logger.debug("getTypeOfFieldFromSignature(): instanceOf: {}, pType: {}", genericType instanceof ParameterizedType, pType);

        return getContext().getParameterizedType(typeClazz, pType, typeKind);
    }
}
