import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;



/**
 * @author Mathilde
 *
 */
public class T1 {

    Automaton automaton;
	
	
	public ArrayList<String> lireDicoTSV(String filePath) {
		
        ArrayList<String> arrayLignes = new ArrayList<>();
		
		try{
		// Cr�ation du flux buff�ris� sur un FileReader
		BufferedReader buff = new BufferedReader(new FileReader(filePath));
		 
		try {
			String line;
			// Lecture du fichier ligne par ligne
			while ((line = buff.readLine()) != null) {
				
				String[] tmp = line.split("\t");
                if (tmp.length != 3) {
                    System.out.println("Erreur de construction du dictionnaire"+line.toString() );
                    System.exit(1);
                }
                
				arrayLignes.add(line);
			}
			
			} finally {
				// dans tous les cas, on ferme nos flux
				buff.close();
			}
		} catch (FileNotFoundException ex) {
            System.out.println("Erreur d'ouverture : " + ex.getMessage());
        } catch (IOException ioe) {
			// erreur de fermeture des flux
			System.out.println("Erreur :" + ioe.toString());
		}
		return arrayLignes;
		
	}
	
	
	
    public ArrayList<String[]> splitLignes(ArrayList<String> lignes) {
        ArrayList<String[]> lignesAnalyser = new ArrayList<>();
        for (String ligne : lignes) {
        	lignesAnalyser.add(ligne.split("\t"));
        }
        return lignesAnalyser;
    }
	
    
    
    public CharSequence[] encodageMorphologique(ArrayList<String[]> lignesAnalyser) {
        CharSequence[] charEncod = new CharSequence[lignesAnalyser.size()];
        // Pour chaque entrée
        int i = 0;
        for (String[] ligne : lignesAnalyser) {
            String forme = ligne[0];
            String lemme = ligne[1];
            String traits = ligne[2];

            // On compare chaque caractère 
            int nbCarIdentique = 0; // nb de caractère identique
            while ((forme.length() >= nbCarIdentique) && 
            		(lemme.length() >= nbCarIdentique) && 
            		forme.substring(0, nbCarIdentique).matches(lemme.substring(0, nbCarIdentique))) {
            	nbCarIdentique++;
            }
            
            // Le nombre de caractère a enlever 
            int nbCaractereAEnlever = forme.length() - (nbCarIdentique - 1);
            // La terminaison du mot 
            String terminaison = lemme.substring(nbCarIdentique - 1);
            // Encodage dans la charSequance
            charEncod[i] = (CharSequence) (forme + (char) 0 + (char) nbCaractereAEnlever + terminaison + (char) 0 + traits);
            i++;
        }
        return charEncod;
    }

    public void save(String fichier) {
        File file;
        FileOutputStream fop;
        try {
            file = new File(fichier);
            fop = new FileOutputStream(file);

            if (!file.exists()) {
                file.createNewFile();
            }
            automaton.store(fop);
            fop.flush();
            fop.close();
        } catch (IOException ex) {
            System.out.println("Erreur d'ecriture: " + ex.getMessage());
        }
    }

    public Automaton load(String fichier) {
        File file = new File(fichier);
        FileInputStream fis;
        Automaton automaton = null;

        try {
            fis = new FileInputStream(file);
            automaton = Automaton.load(fis);
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Erreur de lecture : " + ex.getMessage());
            System.exit(1);
        }
        return automaton;
    }
    
    public void testmot(String mot) {
        System.out.print("> ");
        State e = automaton.getInitialState();
        int i = 0;
        while ((e != null) && (i < mot.length())) {
            e = transiter(e, mot.charAt(i));
            System.out.print(mot.charAt(i));
            i++;

        }
        if (e != null) {  // Fin du mot
            e = transiter(e, (char) 0);
            if (e != null) { // C'est le mot en entier
                System.out.println(" est dans le dico");
            }
        } else {
            System.out.println(" n'est pas dans le dico");
        }
    }

    public State transiter(State e, char c) {
        Set<Transition> transitions = e.getTransitions();
        for (Transition transition : transitions) {
            if (c >= transition.getMin() && c <= transition.getMax()) {
                return transition.getDest();
            }
        }
        return null; 
    }
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		String[] args = new String[3];
////		args[0] = "dico.tsv";
////		args[1] = "dico.aef";
//		
//		args[0] = "-test";
//		args[1] = "dico.aef";
//		args[2] = "lire";
//		
			
		T1 t1 = new T1();
		
        if( args.length == 3){
        	// "Ouvrir le fichier.aef
            System.out.println("Ouvrir le fichier.aef");
            t1.automaton = t1.load(args[1]);

            // Test du mot
            System.out.println("Recherche du mot \"" + args[2] + "\" ");
            t1.testmot(args[2]);

        } else if(args.length == 2) {
        	

    		// Extraction du dictionnaire
            System.out.println("Extraction du dictionnaire");
    		ArrayList<String> lignes = t1.lireDicoTSV(args[0]);
    		
    		// Recuperation des mots et de leurs analyses morphologiques (lemmes et traits)
            System.out.println("Recuperation des mots et de leurs analyses morphologiques (lemmes et traits)");
            ArrayList<String[]> lignesAnalyser = t1.splitLignes(lignes);
    		    
    		// Encodage d'une analyse morphologique
            System.out.println("Encodage de l'analyse morphologique");
            CharSequence[] encodChar =  t1.encodageMorphologique(lignesAnalyser);
            
            // Génération de l'AEF
            System.out.println("Génération de l'AEF");
            t1.automaton = Automaton.makeStringUnion(encodChar);
            
            // Enregistrement de l'AEF 
            System.out.println("Enregistrement de l'AEF");
            t1.save(args[1]);
        } else {
        	 System.out.println("TP1 :\n"
        	 		+ "Compilation du dictionnaire : java -jar T1.jar dico.tsv dico.aef\n"
        	 		+ "Test de l'AEF : java -jar T1.jar -test dico.aef <mot>");
        }
        
	}
}