package sk.fiit.martinfranta.rdftools;

import org.openrdf.OpenRDFException;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.http.HTTPRepository;
import org.openrdf.rio.RDFFormat;
import java.io.File;
import java.io.IOException;
import java.net.URL;


public class Loader {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File file = new File("/ntfs/prog/files/dblp.nt");
		String baseURI = "http://lsdis.cs.uga.edu/projects/semdis/opus";
		
		String sesameServer = "http://localhost:8081/openrdf-sesame";
		String repositoryID = "owdblp";
		try {
			Repository myRepository = new HTTPRepository(sesameServer, repositoryID);
			myRepository.initialize();
			
			RepositoryConnection con = myRepository.getConnection();
		
		
			try {
				con.add(file, baseURI, RDFFormat.NTRIPLES);
//
//				URL url = new URL(sesameServer);
//				con.add(url, url.toString(), RDFFormat.RDFXML);
			}
			finally {
				con.close();
			}
		}
		catch (OpenRDFException e) {
		   e.printStackTrace();
		}
		catch (IOException e) {
		   e.printStackTrace();
		}
	}

}
