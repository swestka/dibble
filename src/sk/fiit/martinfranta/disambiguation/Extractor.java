package sk.fiit.martinfranta.disambiguation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.florianlaws.uima.StanfordNERAnnotator;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.uima.jcas.tcas.Annotation;

public class Extractor {
	
	public static String LOCATION = "de.florianlaws.uima.types.stanford.Location";
	public static String PERSON = "de.florianlaws.uima.types.stanford.Person";
	public static String ORGANIZATION = "de.florianlaws.uima.types.stanford.Organization";
	
	private FileInputStream inputStream;
	private PDDocument doc;
	private static String text = "";
	
	private List<String> types = new ArrayList<String>();
	
	private static ArrayList<Annotation> mentions = new ArrayList<Annotation>();
	
	
	public static ArrayList<Annotation> getMentions() {
		return mentions;
	}

	public static void setMentions(ArrayList<Annotation> mentions1) {
		mentions = mentions1;
	}

	public Extractor (FileInputStream in) {
		this.inputStream = in;
		try {
			initPDFBox();
		} catch (IOException e) {
			System.err.println("Cannot open file.");
			e.printStackTrace();
		}
	}
	
	public Extractor (String filename) {
		File f = new File(filename);
		try {
			this.inputStream = new FileInputStream(f);
			initPDFBox();
		} catch (FileNotFoundException e) {
			System.err.println("Cannot open file - file not found: "+filename);
			e.printStackTrace();
		} catch (IOException e) {
			try {
				BufferedReader in = new BufferedReader(new FileReader(f));
				String line = "";
				while ((line = in.readLine()) != null) {
					text+=line;
				}
				
				in.close();
			} catch (FileNotFoundException e1) {
				System.err.println("Cannot open file - file not found: "+filename);
				e1.printStackTrace();
			} catch (IOException e1) {
				System.err.println("Cannot read file: "+filename);
				e1.printStackTrace();
			}
		}
	}
	
	public void initPDFBox () throws IOException {
		doc = PDDocument.load(inputStream);
		
		PDFTextStripper stripper = new PDFTextStripper();
		text = stripper.getText(doc);
	}
	
	public ArrayList<Annotation> extract () {
		text = text.replaceAll("-(\\s*)\\n(.+)", "$2");
		return extract(text);
	}
	
	public ArrayList<Annotation> extract(String inputText) {
		stanfordAnnotate(inputText);
		return mentions;
	}
	
	public void stanfordAnnotate(String input) {
		StanfordNERAnnotator sner = new StanfordNERAnnotator();
		
		for (Annotation a : sner.getAnnotations(input)) {
			if (types.contains(a.getType().toString())) {
				mentions.add(a);
			}
		}
	}
	
	public static String getText () {
		return text;
	}

	public void setText (String text1) {
		text = text1;
	}
	
	public Extractor addType(String type) {
		types.add(type);
		return this;
	}
}
