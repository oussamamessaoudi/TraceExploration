package me.oussamamessaoudi;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class LogElementHelper {

    public static String logElement(Method method, Object[] objects) throws IllegalAccessException {
        List<String> logs = new ArrayList<>(objects.length);
        final Annotation[][] paramAnnotations = method.getParameterAnnotations();
        Parameter[] parameters = method.getParameters();
        for (int i = 0, parametersLength = parameters.length; i < parametersLength; i++) {
            Parameter parameter = parameters[i];
            if (parameter.getAnnotation(ConfidentialField.class) != null) {
                logs.add(parameter.getName() + "***");
            } else {
                logs.add(parameter.getName() + "=" + logObject(objects[i]));
            }
        }
        return StringUtils.joinWith(", ", logs);
    }

    public static String logObject(Object object) throws IllegalAccessException {
        if (object == null) {
            return "null";
        }
        if (knowingTypes(object)) {
            return object.toString();
        }
        Field[] declaredFields = object.getClass().getDeclaredFields();
        List<String> logs = new ArrayList<>(declaredFields.length);
        for (Field declaredField : declaredFields) {
            if (declaredField.getAnnotation(ConfidentialField.class) != null) {
                logs.add(declaredField.getName() + "=******");
            } else {
                declaredField.setAccessible(true);
                logs.add(declaredField.getName() + "=" + logObject(declaredField.get(object)));
            }
        }
        return StringUtils.joinWith(", ", logs);
    }

    public static boolean knowingTypes(Object object) {
        return String.class.equals(object.getClass());
    }

}
