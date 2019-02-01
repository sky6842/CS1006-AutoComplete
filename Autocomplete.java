import java.util.Arrays;

public class Autocomplete {
    Term[] terms;

    // Initializes the data structure from the given array of terms.
    public Autocomplete(Term[] terms) {
        //METHOD COUNTING
        int counter = 0;

        for (int i = 0; i < terms.length; i++) {
            if (terms[i] == null) {
                throw new NullPointerException();
            }
            counter++;
        }
        this.terms = terms;
        System.out.println("Autocomplete constructor count: " + counter);
    }

    // Returns all terms that start with the given
    // prefix, in descending order of weight.
    public Term[] allMatches (String prefix) {
        if (prefix == null) {
            throw new NullPointerException();
        }
        Term prefixTerm = new Term(prefix, 0);
        int firstIndex = BinarySearchDeluxe.firstIndexOf(terms, prefixTerm, Term.byPrefixOrder(prefix.length()));
        int lastIndex = BinarySearchDeluxe.lastIndexOf(terms, prefixTerm, Term.byPrefixOrder(prefix.length()));

        if(firstIndex==-1 && lastIndex==-1){
            Term[] empty = new Term[0];
            return empty;
        }
        Term[] matches = Arrays.copyOfRange(terms, firstIndex, lastIndex + 1);
        Arrays.sort(matches, Term.byReverseWeightOrder());
        return matches;
    }

    // Returns the number of terms that start with the given prefix.
    public int numberOfMatches(String prefix) {
        if (prefix == null) {
            throw new NullPointerException();
        }
        Term prefixTerm = new Term(prefix, 0);
        int firstIndex = BinarySearchDeluxe.firstIndexOf(terms, prefixTerm, Term.byPrefixOrder(prefix.length()));
        int lastIndex = BinarySearchDeluxe.lastIndexOf(terms, prefixTerm, Term.byPrefixOrder(prefix.length()));

        if (firstIndex == -1 && lastIndex == -1) {
            return 0;
        } else {
            return lastIndex - firstIndex + 1;
        }
    }
}