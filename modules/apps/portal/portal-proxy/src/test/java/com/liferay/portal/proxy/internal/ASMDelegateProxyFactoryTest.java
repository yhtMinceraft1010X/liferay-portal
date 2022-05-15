/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.proxy.internal;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.SwappableSecurityManager;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;
import com.liferay.portal.kernel.test.rule.NewEnv;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ReflectionUtilTestUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.DelegateProxyFactory;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Shuyang Zhou
 */
public class ASMDelegateProxyFactoryTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			CodeCoverageAssertor.INSTANCE, LiferayUnitTestRule.INSTANCE);

	@Test
	public void testASMWrapper() throws Exception {
		DelegateProxyFactory delegateProxyFactory =
			new ASMDelegateProxyFactory();

		Object asmWrapper = delegateProxyFactory.newDelegateProxyInstance(
			TestInterface.class.getClassLoader(), TestInterface.class,
			new TestDelegate(), new TestDefault());

		Class<?> asmWrapperClass = asmWrapper.getClass();

		Method method = asmWrapperClass.getDeclaredMethod(
			"objectMethod", Object.class);

		Object object = new Object();

		Assert.assertNotSame(object, method.invoke(asmWrapper, object));

		method = asmWrapperClass.getDeclaredMethod("intMethod", Integer.TYPE);

		int randomInt = RandomTestUtil.randomInt();

		Assert.assertEquals(randomInt, method.invoke(asmWrapper, randomInt));
	}

	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testClassInitializationFailure() throws Exception {
		SecurityException securityException = new SecurityException();

		try (SwappableSecurityManager swappableSecurityManager =
				ReflectionUtilTestUtil.throwForSuppressAccessChecks(
					securityException)) {

			DelegateProxyFactory delegateProxyFactory =
				new ASMDelegateProxyFactory();

			delegateProxyFactory.newDelegateProxyInstance(
				TestInterface.class.getClassLoader(), TestInterface.class,
				new TestDelegate(), new TestDefault());

			Assert.fail();
		}
		catch (ExceptionInInitializerError eiie) {
			Assert.assertSame(securityException, eiie.getCause());
		}
	}

	@Test
	public void testCreateASMWrapper() throws Exception {
		DelegateProxyFactory delegateProxyFactory =
			new ASMDelegateProxyFactory();

		Object asmWrapper = delegateProxyFactory.newDelegateProxyInstance(
			TestInterface.class.getClassLoader(), TestInterface.class,
			new TestDelegate(), new TestDefault());

		Class<?> asmWrapperClass = asmWrapper.getClass();

		Assert.assertEquals(Modifier.PUBLIC, asmWrapperClass.getModifiers());

		Package pkg = TestDelegate.class.getPackage();

		Assert.assertEquals(
			StringBundler.concat(
				pkg.getName(), ".", TestInterface.class.getSimpleName(),
				"ASMWrapper"),
			asmWrapperClass.getName());

		Assert.assertSame(Object.class, asmWrapperClass.getSuperclass());

		Method[] expectedMethods = _getDeclaredMethods(TestInterface.class);
		Method[] actualMethods = _getDeclaredMethods(asmWrapperClass);

		// See LPS-71495

		Assert.assertTrue(asmWrapper.equals(null));
		Assert.assertEquals(0, asmWrapper.hashCode());
		Assert.assertEquals("test", asmWrapper.toString());
		Assert.assertEquals(
			StringBundler.concat(
				"Expected: ", Arrays.toString(expectedMethods), ", actual: ",
				Arrays.toString(actualMethods)),
			expectedMethods.length, actualMethods.length);

		for (int i = 0; i < expectedMethods.length; i++) {
			_assertEquals(expectedMethods[i], actualMethods[i]);
		}
	}

	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testErrorCreateASMWrapper() throws Exception {
		Method defineClassMethod = ReflectionTestUtil.getAndSetFieldValue(
			ASMDelegateProxyFactory.class, "_defineClassMethod", null);

		DelegateProxyFactory delegateProxyFactory =
			new ASMDelegateProxyFactory();

		try {
			delegateProxyFactory.newDelegateProxyInstance(
				TestInterface.class.getClassLoader(), TestInterface.class,
				new TestDelegate(), new TestDefault());

			Assert.fail();
		}
		catch (RuntimeException runtimeException) {
			Throwable throwable = runtimeException.getCause();

			Assert.assertSame(NullPointerException.class, throwable.getClass());
		}
		finally {
			ReflectionTestUtil.setFieldValue(
				ASMDelegateProxyFactory.class, "_defineClassMethod",
				defineClassMethod);
		}

		try {
			delegateProxyFactory.newDelegateProxyInstance(
				ClassLoader.getSystemClassLoader(), Object.class, new Object(),
				Object.class);

			Assert.fail();
		}
		catch (IllegalArgumentException illegalArgumentException) {
			Assert.assertEquals(
				Object.class + " is not an interface",
				illegalArgumentException.getMessage());
		}
	}

	public static class TestDefault implements TestInterface {

		@Override
		public boolean booleanMethod(boolean booleanArg) {
			return booleanArg;
		}

		@Override
		public byte byteMethod(byte byteArg) {
			return byteArg;
		}

		@Override
		public char charMethod(char charArg) {
			return charArg;
		}

		@Override
		public double doubleMethod(double doubleArg) {
			return doubleArg;
		}

		@Override
		public float floatMethod(float floatArg) {
			return floatArg;
		}

		@Override
		public int intMethod(int intArg) {
			return intArg;
		}

		@Override
		public long longMethod(long longArg) {
			return longArg;
		}

		@Override
		public Object objectMethod(Object objectArg) {
			return objectArg;
		}

		@Override
		public short shortMethod(short shortArg) {
			return shortArg;
		}

		@Override
		public void voidWithExceptionMethod() throws Exception {
		}

	}

	public static class TestDelegate {

		@Override
		public boolean equals(Object object) {
			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		public Object objectMethod(Object object) {
			return new Object();
		}

		@Override
		public String toString() {
			return "test";
		}

	}

	public interface TestInterface {

		public boolean booleanMethod(boolean booleanArg);

		public byte byteMethod(byte byteArg);

		public char charMethod(char charArg);

		public double doubleMethod(double doubleArg);

		public float floatMethod(float floatArg);

		public int intMethod(int intArg);

		public long longMethod(long longArg);

		public Object objectMethod(Object objectArg);

		public short shortMethod(short shortArg);

		public void voidWithExceptionMethod() throws Exception;

	}

	private void _assertEquals(Method expectedMethod, Method actualMethod) {
		Assert.assertEquals(
			StringBundler.concat(
				"Expected:", expectedMethod, ", actual: ", actualMethod),
			expectedMethod.getModifiers() - Modifier.ABSTRACT,
			actualMethod.getModifiers());
		Assert.assertSame(
			StringBundler.concat(
				"Expected:", expectedMethod, ", actual: ", actualMethod),
			expectedMethod.getReturnType(), actualMethod.getReturnType());
		Assert.assertEquals(
			StringBundler.concat(
				"Expected:", expectedMethod, ", actual: ", actualMethod),
			expectedMethod.getName(), actualMethod.getName());
		Assert.assertArrayEquals(
			StringBundler.concat(
				"Expected:", expectedMethod, ", actual: ", actualMethod),
			expectedMethod.getParameterTypes(),
			actualMethod.getParameterTypes());
		Assert.assertArrayEquals(
			StringBundler.concat(
				"Expected:", expectedMethod, ", actual: ", actualMethod),
			expectedMethod.getExceptionTypes(),
			actualMethod.getExceptionTypes());
	}

	private Method[] _getDeclaredMethods(Class<?> clazz) {
		Method[] methods = clazz.getDeclaredMethods();

		methods = ArrayUtil.<Method>filter(
			methods,
			method -> {
				String name = method.getName();

				if (name.equals("equals") || name.equals("hashCode") ||
					name.equals("toString")) {

					return false;
				}

				return true;
			});

		Arrays.sort(
			methods,
			(method1, method2) -> {
				String name1 = method1.getName();
				String name2 = method2.getName();

				return name1.compareTo(name2);
			});

		return methods;
	}

}