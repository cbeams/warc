package org.iokit.core.config;


import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;




import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Reflector { // TODO: remove

    public <T> void configure(Object node, Class<T> targetType, Consumer<T> visitor) throws Exception {
        configure(node, t -> targetType.isAssignableFrom(t.getClass()), visitor);
    }

    public <T> void configure(Object node, Predicate<T> predicate, Consumer<T> visitor) throws Exception {

        Class<?> nodeType = node.getClass();

        for (Field field : getDeclaredFieldsIncludingInherited(nodeType)) {
            field.setAccessible(true);
            T curNode = (T) field.get(node);
            if (curNode == null)
                continue;
            if (!curNode.getClass().getName().startsWith("java."))
                configure(curNode, predicate, visitor);
            if (predicate.test(curNode))
                visitor.accept(curNode);
        }
    }

    public static Set<Field> getDeclaredFieldsIncludingInherited(Class<?> clazz) {
        if (clazz == null)
            throw new IllegalArgumentException("expecting Class parameter not to be null");
        Set<Field> declaredFields = getDeclaredFieldsIgnoringSyntheticAndStatic(clazz);

        // get fields declared in superclass
        Class<?> superclazz = clazz.getSuperclass();
        while (superclazz != null && !superclazz.getName().startsWith("java.lang")) {
            declaredFields.addAll(getDeclaredFieldsIgnoringSyntheticAndStatic(superclazz));
            superclazz = superclazz.getSuperclass();
        }
        return declaredFields;
    }

    private static Set<Field> getDeclaredFieldsIgnoringSyntheticAndStatic(Class<?> clazz) {
        Set<Field> fields = new LinkedHashSet<>();
        Collections.addAll(fields, clazz.getDeclaredFields());
        fields.removeIf(next -> next.isSynthetic() || Modifier.isStatic(next.getModifiers()));
        return fields;
    }
}
