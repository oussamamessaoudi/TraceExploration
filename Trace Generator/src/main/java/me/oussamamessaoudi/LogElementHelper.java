package me.oussamamessaoudi;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public class LogElementHelper {

    public String logElement(Method method, Object[] objects) throws IllegalAccessException {
        List<String> logs = new ArrayList<>(objects.length);
        Parameter[] parameters = method.getParameters();
        for (int i = 0, parametersLength = parameters.length; i < parametersLength; i++) {
            Parameter parameter = parameters[i];
            var annotation = parameter.getAnnotation(ConfidentialField.class);
            if (annotation != null) {
                logs.add(parameter.getName() + "=" + annotation.value());
            } else {
                logs.add(parameter.getName() + "=" + logObject(objects[i]));
            }
        }
        return StringUtils.joinWith(", ", logs);
    }

    public String logObject(Object object) throws IllegalAccessException {
        if (object == null) {
            return "null";
        }
        Class<?> aClass = object.getClass();
        if (object instanceof Collection<?>) {
            return logObject((Collection<?>) object);
        }
        if (object instanceof Map<?, ?>) {
            return logObject((Map<?, ?>) object);
        }
        if (aClass.isArray()) {
            return logArray(object);
        }
        String packageName = aClass.getPackageName();
        if ("java.lang".equals(packageName) || "java.time".equals(packageName)) {
            return object.toString();
        }
        Field[] declaredFields = aClass.getDeclaredFields();
        List<String> logs = new ArrayList<>(declaredFields.length);
        for (Field declaredField : declaredFields) {
            var annotation = declaredField.getAnnotation(ConfidentialField.class);
            if (annotation != null) {
                logs.add(declaredField.getName() + "=" + annotation.value());
            } else {
                declaredField.setAccessible(true);
                logs.add(declaredField.getName() + "=" + logObject(declaredField.get(object)));
            }
        }
        return StringUtils.joinWith(", ", logs);
    }

    public String logArray(Object object) throws IllegalAccessException {
        int length = Array.getLength(object);
        List<String> logs = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            logs.add(logObject(Array.get(object, i)));
        }
        return StringUtils.joinWith(", ", logs);
    }

    public String logObject(Collection<?> objects) throws IllegalAccessException {
        List<String> logs = new ArrayList<>(objects.size());
        for (Object object : objects) {
            logs.add(logObject(object));
        }
        return StringUtils.joinWith(", ", logs);
    }

    public String logObject(Map<?, ?> map) throws IllegalAccessException {
        List<String> logs = new ArrayList<>(map.size());
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            logs.add(logObject(logObject(entry.getKey()) + ":" + logObject(entry.getValue())));
        }
        return StringUtils.joinWith(", ", logs);
    }

}
