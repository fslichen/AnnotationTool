package util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import evolution.dto.AnotherAnnotation;
import evolution.dto.AnyAnnotation;
import evolution.dto.AnyDto;

public class AnyUtil {
	@SuppressWarnings({ "rawtypes" })
	public static List<Object> getInstanceVariablesByAnnotation(Object object, Class annotationClass) {
		return getFieldsByAnnotation(object, annotationClass)
				.parallelStream()
				.map(x -> {
					try {
						x.setAccessible(true);
						return x.get(object);
					} catch (Exception e) {
						return null;
					}
				}).collect(Collectors.toList());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Field> getFieldsByAnnotation(Object object, Class annotationClass) {
		return Arrays.asList(object.getClass().getDeclaredFields())
				.parallelStream()
				.filter(x -> {
					x.setAccessible(true);
					return x.getAnnotation(annotationClass) != null;
				}).collect(Collectors.toList());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Field> getFieldsByAnnotationAndKeyValuePairs(Object object, List<Field> fields, Class annotationClass, Object... keyValuePairs) {
		fields = fields == null ? getFieldsByAnnotation(object, annotationClass) : fields;
		List<Field> desiredFields = new LinkedList<>();
		for (Field field : fields) {
			boolean isDesiredField = true;
			for (int i = 0; i < keyValuePairs.length; i += 2) {
				String key = (String) keyValuePairs[i];
				Object value = keyValuePairs[i + 1];
				try {
					field.setAccessible(true);
					Annotation annotation = field.getAnnotation(annotationClass);
					Method method = annotation.getClass().getDeclaredMethod(key);
					if (!method.invoke(annotation).equals(value)) {
						isDesiredField = false;
						break;
					}
				} catch (Exception e) {
					isDesiredField = false;
					break;
				}
			}
			if (isDesiredField) {
				desiredFields.add(field);
			}
		}
		return desiredFields;
	}

	@SuppressWarnings("rawtypes")
	public static List<Object> getInstanceVariablesByAnnotationAndKeyValuePairs(Object object, List<Field> fields, Class annotationClass, Object... keyValuePairs) {
		return getFieldsByAnnotationAndKeyValuePairs(object, fields, annotationClass, keyValuePairs)
				.parallelStream().map(x -> {
					try {
						x.setAccessible(true);
						return x.get(object);
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}
				}).collect(Collectors.toList());
	}

	public static void setVariable(Object object, String name, Object value) {
		try {
			Field field = object.getClass().getDeclaredField(name);
			field.setAccessible(true);
			field.set(object, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Object getVariable(Object object, String name) {
		try {
			Field field = object.getClass().getDeclaredField(name);// You have to use getDeclaredField to get access to the private field.
			field.setAccessible(true);
			return field.get(object);
		} catch (Exception e) {
			return null;
		}
	}
	
	private static Object getFieldInstance(Object classInstance, String fieldName) {
		try {
			Field field = classInstance.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			Object fieldInstance = field.get(classInstance);
			if (fieldInstance != null) {
				return fieldInstance;
			} else {
				return field.getType().newInstance();
			}
		} catch (Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T runFieldInstance(Object instance, String fieldName, String methodName, Class<T> clazz, Object... parameters) {
		Object fieldInstance = getFieldInstance(instance, fieldName);
		Method[] methods = fieldInstance.getClass().getDeclaredMethods();
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				try {
					return (T) method.invoke(fieldInstance, parameters);
				} catch (Exception e) {}
			}
		}
		return null;
	}
	
	public static <T> Object runFieldInstance(Object instance, String fieldName, String methodName, Object... parameters) {
		return runFieldInstance(instance, fieldName, methodName, Object.class, parameters);
	}
}
