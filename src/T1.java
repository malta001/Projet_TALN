import java.util.ArrayList;

import dk.brics.automaton.Automaton;



/**
 * @author Mathilde et Thibaud
 *
 */
public class T1 {

	
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
			
		AEF aef = new AEF();
		
        if( args.length == 3){
        	// "Ouvrir le fichier.aef
            System.out.println("Ouvrir le fichier.aef");
            aef.automaton = aef.load(args[1]);

            // Test du mot
            System.out.println("Recherche du mot \"" + args[2] + "\" ");
            AnalyseMorphologique[] analyses = aef.analyser(args[2]);
            
            if(analyses != null) {
                System.out.println("> \""+args[2] + "\" est dans le dico :");
            	System.out.println(" Lemme	|	Traits");
            	System.out.println(" ----------------------");
            	for (AnalyseMorphologique analyse : analyses) {
                    System.out.println(" "+analyse.getLemme() + "	|	" + analyse.getTrait());
                }
            } else {
                System.out.println("> \""+args[2] + "\" n'est pas dans le dico");
            }
            
           
            
        } else if(args.length == 2) {
        	

    		// Extraction du dictionnaire
            System.out.println("Extraction du dictionnaire");
    		ArrayList<String> lignes = aef.lireDicoTSV(args[0]);
    		
    		// Recuperation des mots et de leurs analyses morphologiques (lemmes et traits)
            System.out.println("Recuperation des mots et de leurs analyses morphologiques (lemmes et traits)");
            ArrayList<String[]> lignesAnalyser = aef.splitLignes(lignes);
    		    
    		// Encodage d'une analyse morphologique
            System.out.println("Encodage de l'analyse morphologique");
            CharSequence[] encodChar =  aef.encodageMorphologique(lignesAnalyser);
            
            // Génération de l'AEF
            System.out.println("Génération de l'AEF");
            aef.automaton = Automaton.makeStringUnion(encodChar);
            
            // Enregistrement de l'AEF 
            System.out.println("Enregistrement de l'AEF");
            aef.save(args[1]);
        } else {
        	 System.out.println("TP1 :\n"
        	 		+ "Compilation du dictionnaire : java -jar T1.jar dico.tsv dico.aef\n"
        	 		+ "Test avec un mot : java -jar T1.jar -test dico.aef <mot>");
        }
        
	}
}