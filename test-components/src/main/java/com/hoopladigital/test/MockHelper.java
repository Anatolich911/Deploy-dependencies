package com.hoopladigital.test;

import org.mockito.Mock;
import org.mockito.Spy;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MockHelper {

	public static Object[] allDeclaredMocks(final Object test, final Object... ignore) {

		final Field[] declaredFields = test.getClass().getDeclaredFields();

		final ArrayList<Object> mockList = new ArrayList<>();

		for (final Field field : declaredFields) {
			field.setAccessible(true);
			if (!ignored(ignore, getValue(test, field))) {
				checkAnnotation(test, mockList, field, Mock.class);
				checkAnnotation(test, mockList, field, Spy.class);
			}
		}

		return mockList.toArray();

	}

	private static Object getValue(final Object test, final Field field) {
		try {
			return field.get(test);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	private static boolean ignored(final Object[] ignore, final Object field) {
		for (final Object o : ignore) {
			if (field.equals(o)) {
				return true;
			}
		}
		return false;
	}

	private static void checkAnnotation(
			final Object test,
			final ArrayList<Object> mockList,
			final Field field,
			final Class annotationClass
	) {
		if (null != field.getAnnotation(annotationClass)) {
			mockList.add(getValue(test, field));
		}
	}

}
