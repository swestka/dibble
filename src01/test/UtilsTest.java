package test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;

import sk.fiit.martinfranta.disambiguation.module.Context;
import sk.fiit.martinfranta.disambiguation.module.dblp.DblpContext;

public class UtilsTest {

	@Test
	public void testProcessingQuery() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		String test = "abc $1$ def $2$ ghi $1$";
		DblpContext c = new DblpContext(null);
		Method method = Context.class.getDeclaredMethod("processQuery", String[].class);
		method.setAccessible(true);
		Assert.assertEquals("abc ok def fine ghi ok", 
				method.invoke(c, new Object[]{test, "ok", "fine"}));
	}
	@Test
	public void testReplacenement() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		String test = "abc $1$ def $2$ ghi $1$";
		Assert.assertEquals("abc ok def $2$ ghi ok", test.replaceAll("\\$1\\$", "ok"));
		
	}
	
	

}
