package sk.fiit.martinfranta.tools;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

import sk.fiit.martinfranta.disambiguation.module.Entity;
import flexjson.JSONSerializer;

public class Serializer {
	
	
	public static void objectSerilize(Object o, String filename) {
		try {
	         FileOutputStream fileOut = new FileOutputStream(filename);
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(o);
	         out.close();
	         fileOut.close();
       }
		catch(IOException i) {
	          i.printStackTrace();
		}
	}
	
	public static Object objectDeserialize(String filename) {
		FileInputStream fileIn;
		try {
			fileIn = new FileInputStream(filename);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			
			Object o = in.readObject();
			in.close();
			fileIn.close();
			
			return o;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static void stdSerialize(List<Entity> entities) {
		stdSerialize(entities, "entities.srlz");
	}
	
	public static void stdSerialize(List<Entity> entities, String filename) {
		try {
	         FileOutputStream fileOut = new FileOutputStream(filename);
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(entities);
	         out.close();
	         fileOut.close();
        }
		catch(IOException i) {
	          i.printStackTrace();
		}
	}
	
	@SuppressWarnings({ "resource", "unchecked" })
	public static List<Entity> deserialize() {
		FileInputStream fileIn;
		try {
			fileIn = new FileInputStream("entities.srlz");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			
			return (LinkedList<Entity>)in.readObject();
		} catch (Exception e) {
			return null;
		}
		
	}
}
