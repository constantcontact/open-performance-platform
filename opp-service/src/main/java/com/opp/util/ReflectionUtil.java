package com.opp.util;

import com.opp.exception.BadRequestException;
import com.google.common.base.CaseFormat;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

/**
 * Created by ctobe on 9/1/16.
 */
public class ReflectionUtil {

    /**
     * Gets a property value from an object
     * @param instance
     * @param propertyName
     * @param convertToCamelCase
     * @return
     */
    public static Optional<String> getPropertyValueFromObject(Object instance, String propertyName, boolean convertToCamelCase){
        propertyName = (convertToCamelCase) ? CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, propertyName) : propertyName;
        String propertyValue = null;
        try {
            propertyValue = BeanUtils.getProperty(instance, propertyName);
        } catch (NoSuchMethodException e) {
            throw new BadRequestException("Invalid Query Param = " + propertyName);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(propertyValue);
    }

    /**
     * Sets a property value on an object
     * @param instance
     * @param propertyName
     * @param propertyValue
     */
    public static void setProperty(Object instance, String propertyName, Object propertyValue){
        try {
            PropertyUtils.setProperty(instance, propertyName, propertyValue);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if an object has a property
     * @param instance
     * @param propertyName
     * @return
     */
    public static boolean hasProperty(Object instance, String propertyName){
        return PropertyUtils.isReadable(instance, propertyName);
    }
}
