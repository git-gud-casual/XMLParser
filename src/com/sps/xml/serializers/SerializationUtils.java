package com.sps.xml.serializers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

class SerializationUtils {
    private static final Set<Class<?>> WRAPPER_TYPES = new HashSet(Arrays.asList(
            Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Void.class));
    public static boolean isWrapperType(Class<?> clazz) {
        return WRAPPER_TYPES.contains(clazz);
    }
}
