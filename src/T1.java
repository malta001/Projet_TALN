import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;



/**
 * @author Mathilde Maltaire et Thibaud Avenier
 *
 */
public class T1 {


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String filePath = "src/dico.tsv";
		 
		try{
		// Cr�ation du flux buff�ris� sur un FileReader, imm�diatement suivi par un 
		// try/finally, ce qui permet de ne fermer le flux QUE s'il le reader
		// est correctement instanci� (�vite les NullPointerException)
		BufferedReader buff = new BufferedReader(new FileReader(filePath));
		 
		try {
		String ligne;
		// Lecture du fichier ligne par ligne. Cette boucle se termine
		// quand la m�thode retourne la valeur null.
		while ((ligne = buff.readLine()) != null) {
			System.out.println(ligne);
				//faites ici votre traitement
		}
		
		} finally {
			// dans tous les cas, on ferme nos flux
			buff.close();
		}
		} catch (IOException ioe) {
			// erreur de fermeture des flux
			System.out.println("Erreur --" + ioe.toString());
		}

		
	}
}