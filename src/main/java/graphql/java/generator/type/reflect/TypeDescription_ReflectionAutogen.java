package graphql.java.generator.type.reflect;

import graphql.java.generator.type.strategies.TypeDescriptionStrategy;

public class TypeDescription_ReflectionAutogen implements TypeDescriptionStrategy {

    @Override
    public String getTypeDescription(Object object) {
        if (object == null) {
            return null;
        }
        if (!(object instanceof Class<?>)) {
            object = object.getClass();
        }
        Class<?> clazz = (Class<?>) object;
        return "Autogenerated for class type: " + clazz.getCanonicalName();
    }
}
