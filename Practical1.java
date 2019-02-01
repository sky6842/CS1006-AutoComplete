import java.io.BufferedReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class Practical1 {

    public static void main(String[] args) {
    	ArrayList<Term> terms = new ArrayList<>();
        Autocomplete autocomplete;
        
    	try (BufferedReader reader = new BufferedReader(new FileReader(args[0]))) {

            reader.readLine();
            String line = "";

            //read terms from file and add terms + weights to an ArrayList
            while ((line = reader.readLine()) != null) {
                String[] details = line.split("\t");
                details[1] = Practical1.replaceCharacters(details[1]);
                details[1] = details[1].toLowerCase();
                Term t = new Term(details[1].trim(), Long.parseLong(details[0].trim()));
                terms.add(t);
            }

            //add sorted terms from ArrayList to Array
            Collections.sort(terms);
            Term[] termArray = new Term[terms.size()];
            terms.toArray(termArray);
            autocomplete = new Autocomplete(termArray);
            
            int k = Integer.parseInt(args[1]);
        	GUI gui = new GUI();
        	if(k < 1 || k >= termArray.length) {
        		System.out.println("Invalid number - the number of terms has been set at the default of 10");
        		k = 10;
        	}
        	gui.maxDisplay = k;
        	gui.setMaximumDisplay(k);
        	gui.terms = termArray;
        	gui.autocomplete = autocomplete;
        }
        catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        catch(ArrayIndexOutOfBoundsException e) {
        	System.out.println("Usage: java Practical1 <input_file> <number_queries>");
        }
        catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    //replaces unicode characters with ascii equivalents
    public static String replaceCharacters(String text) {
        text = text.replaceAll("[àâáāãäå]","a");
        text = text.replaceAll("[èéêëē]","e");
        text = text.replaceAll("[ïîìīí]","i");
        text = text.replaceAll("[òóôõöōøồ]","o");
        text = text.replaceAll("[ûúūùü]","u");
        text = text.replaceAll("ñ","n");
        text = text.replaceAll("[ýÿ]","y");
        text = text.replaceAll("[ÀÁÂÃÄÅ]","A");
        text = text.replaceAll("[ÈÉÊË]","E");
        text = text.replaceAll("[ÍÌÏÎ]","I");
        text = text.replaceAll("[ÒÔÕÖÓØ]","O");
        text = text.replaceAll("[ÛÙÚÜ]","U");
        text = text.replaceAll("Ñ","N");
        text = text.replaceAll("[ÝŸ]","Y");

        return text;
    }
}
