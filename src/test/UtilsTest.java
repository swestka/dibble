package test;

import java.util.ArrayList;

import junit.framework.Assert;

import org.apache.uima.jcas.tcas.Annotation;
import org.junit.Test;

import sk.fiit.martinfranta.disambiguation.Extractor;
import sk.fiit.martinfranta.tools.Similarity;

public class UtilsTest {

//	@Test
//	public void testProcessingQuery() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
//		String test = "abc $1$ def $2$ ghi $1$";
//		DblpContext c = new DblpContext(null);
//		Method method = Context.class.getDeclaredMethod("processQuery", String[].class);
//		method.setAccessible(true);
//		Assert.assertEquals("abc ok def fine ghi ok", 
//				method.invoke(c, new Object[]{test, "ok", "fine"}));
//	}
//	@Test
//	public void testReplacenement() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
//		String test = "abc $1$ def $2$ ghi $1$";
//		Assert.assertEquals("abc ok def $2$ ghi ok", test.replaceAll("\\$1\\$", "ok"));
//		
//	}
	
//	@Test
//	public void testSerialization() {
//		LinkedList<Entity> entities = new LinkedList<Entity>();
//		entities.add(new AuthorEntity("xyz"));
//		entities.add(new AuthorEntity("bzd"));
//		
//		AuthorEntity e = new AuthorEntity("ghj");
//		AuthorCandidate c = new AuthorCandidate("can");
//		c.setField("resource", "x");
//		c.setContext(new DblpContext(c));
//		e.addCandidate(c);
//		Serializer.stdSerialize(entities, "test.srlz");
//	}
	
	@Test
	public void testSimilarity() {
		System.out.println(Similarity.getMatchLikelyhood("Dan Martin", "David Martin"));
		Assert.assertTrue("Similar", Similarity.getMatchLikelyhood("Dan Martin", "David Martin") > 70);
	}
	
	@Test
	public void testPdfRead() {
		Extractor e = new Extractor("/ntfs/prog/files/SOLAN0000/SOLAN PDF/S2121.pdf");
			
		e.addType(Extractor.PERSON);
		System.out.println(Extractor.getText());
		
		ArrayList<Annotation> entityMentions = e.extract();
		for (Annotation a : entityMentions) {
			System.out.println(a.getCoveredText());
		}

		System.out.println(Extractor.getText());
	}

}
