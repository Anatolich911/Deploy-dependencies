package com.hoopladigital.test;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.text.MessageFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class BeanTestHelper {

	private static final Logger log = LoggerFactory.getLogger(BeanTestHelper.class);

	public <T, S extends T> void assertPropertiesAreEqual(
			final T expected,
			final S actual,
			final String... excludes
	) throws Exception {

		final PropertyDescriptor[] pd = PropertyUtils.getPropertyDescriptors(expected);

		for (final PropertyDescriptor p : pd) {

			final String name = p.getName();

			boolean excluded = false;
			if (name.equals("class")) {
				excluded = true;
			} else {
				for (final String s : excludes) {
					if (s.equalsIgnoreCase(name)) {
						excluded = true;
						break;
					}
				}
			}

			if (!excluded) {

				final Object expectedValue = PropertyUtils.getProperty(expected, name);
				final Object actualValue = PropertyUtils.getProperty(actual, name);

				if ((expectedValue == null) && (actualValue == null)) {
					log.trace("Both beans have 'null' values for property '{}'", name);
					// We will also consider two nulls as equal, even though technically they aren't.
					// In practice, though, this method is called to test "sameness", not equality.
				} else {
					assertEquals(
							expectedValue, actualValue,
							() -> MessageFormat.format("Assert failed for property ''{0}''", name)
					);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public <TYPE> TYPE getPrivateField(
			final Object object,
			final String fieldName
	) throws Exception {
		final Field field = findDeclaredField(fieldName, object.getClass());
		field.setAccessible(true);
		return (TYPE) field.get(object);
	}

	public void setPrivateField(
			final Object object,
			final String fieldName,
			final Object newValue
	) throws Exception {
		final Field field = findDeclaredField(fieldName, object.getClass());
		field.setAccessible(true);
		field.set(object, newValue);
	}

	private Field findDeclaredField(
			final String fieldName,
			final Class<?> objectClass
	) {

		try {
			return objectClass.getDeclaredField(fieldName);
		} catch (final NoSuchFieldException e) {
			// no field? try the parent class
			if (Object.class != objectClass) {
				return findDeclaredField(fieldName, objectClass.getSuperclass());
			}
		}

		// punt. :(
		throw new RuntimeException("Cannot find field " + fieldName);

	}

	@SuppressWarnings("StatementWithEmptyBody")
	public <T, S extends T> void diffBeans(final T expected, final S actual, final String... excludes) throws Exception {
		final StringBuilder stringBuilder = new StringBuilder();
		final PropertyDescriptor[] pd = PropertyUtils.getPropertyDescriptors(expected);
		for (final PropertyDescriptor p : pd) {
			boolean excluded = false;
			for (final String s : excludes) {
				if (s.equalsIgnoreCase(p.getName())) {
					excluded = true;
				}
			}
			if (!excluded) {
				final Object expectedValue = PropertyUtils.getProperty(expected, p.getName());
				final Object actualValue = PropertyUtils.getProperty(actual, p.getName());

				if ((expectedValue == null) && (actualValue == null)) {
					// We will also consider two nulls as equal, even though technically they aren't.
					// In practice, though, this method is called to test "sameness", not equality.
				} else {
					try {
						assertEquals(
								expectedValue, actualValue,
								() -> MessageFormat.format("Assert failed for property ''{0}''", p.getName())
						);
					} catch (final AssertionError e) {
						stringBuilder.append("\n- ").append(p.getName()).append(" - expected: '").append(expectedValue).append("', actual: '").append(actualValue).append("'");
					}
				}
			}
		}
		if (stringBuilder.length() != 0) {
			fail("Beans differ: " + stringBuilder.toString());
		}
	}

}
