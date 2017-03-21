package com.opp.util;

import java.util.Map;
import java.util.function.Function;

import static java.util.EnumSet.allOf;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class DomainUtil {

    /**
     * Creates a Map of a String identifier to its respective enum given a provided enum class.
     * @param enumClass the enum class to map
     * @param identifierFunction the function to generate the key from a given enum
     * @param <E> the enum
     * @return Map of String keys to their respective enums
     */
    public static <E extends Enum<E>> Map<String, E> mapEnums(Class<E> enumClass, Function<E, String> identifierFunction) {
        return allOf(enumClass).stream().collect(toMap(identifierFunction, identity()));
    }

}
