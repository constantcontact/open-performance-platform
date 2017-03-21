package com.opp.dao.util;

import com.opp.exception.ResourceNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import static java.util.stream.Collectors.joining;


public class SelectUtils {

    public static <T> T getOrThrowNotFound(Supplier<T> supplier) {
        return getWithNotFoundSupplier(supplier, () -> {
            throw new ResourceNotFoundException();
        });
    }

    public static <T> T getOrThrowNotFound(Supplier<T> supplier, String errorMessage) {
        return getWithNotFoundSupplier(supplier, () -> {
            throw new ResourceNotFoundException(errorMessage);
        });
    }

    public static <T> Optional<T> getOptional(Supplier<T> supplier) {
        T result = getWithNotFoundSupplier(supplier, () -> null);
        return Optional.ofNullable(result);
    }


    public static <T> T getOrReturnNull(Supplier<T> supplier) {
        return getWithNotFoundSupplier(supplier, () -> null);
    }

    public static <T> List<T> getOrReturnEmpty(Supplier<List<T>> supplier) {
        return getWithNotFoundSupplier(supplier, () -> Collections.emptyList());
    }

    /**
     * Returns the String, "(?,?,...,?)", where the number of '?'s is equal to the number of ids passed in the Set.
     * @param ids
     * @return
     */
    public static String inClause(Set<?> ids) {
        return "(" + ids.stream().map(id -> "?").collect(joining(",")) + ")";
    }

    private static <T> T getWithNotFoundSupplier(Supplier<T> foundSupplier, Supplier<T> notFoundSupplier) {
        try {
            return foundSupplier.get();
        } catch (EmptyResultDataAccessException e) {
            return notFoundSupplier.get();
        }
    }

}
