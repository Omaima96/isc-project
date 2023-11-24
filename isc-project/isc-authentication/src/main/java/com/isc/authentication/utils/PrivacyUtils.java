package com.isc.authentication.utils;

import com.isc.authentication.utils.annotation.SensitiveData;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

@Component
public class PrivacyUtils {

    public Object[] hideSensitveFields(Object[] objects) {

        try {
            if (objects != null) {
                return Arrays.stream(objects).map(object -> {
                    if (object != null) {
                        Class<?> clazz = object.getClass();
                        Object instanceObject = null;
                        if (!clazz.equals(String.class) && !clazz.equals(Integer.class) && !clazz.equals(Sort.Direction.class) && !clazz.equals(Long.class)) {
                            try {
                                instanceObject = clazz.getDeclaredConstructor().newInstance();
                                Field[] field = clazz.getDeclaredFields();
                                Field[] auxField = instanceObject.getClass().getDeclaredFields();
                                for (int i = 0; i < clazz.getDeclaredFields().length; i++) {
                                    field[i].setAccessible(true);
                                    auxField[i].setAccessible(true);
                                    if (field[i].getAnnotation(SensitiveData.class) != null) {
                                        auxField[i].set(instanceObject, "******");
                                    } else if (!field[i].getName().equals("serialVersionUID")) {
                                        auxField[i].set(instanceObject, field[i].get(object));
                                    }
                                }
                            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                                e.printStackTrace();
                            }
                            return instanceObject;
                        }
                        return object;
                    }
                    return "";
                }).toArray();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
