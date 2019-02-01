import java.util.Comparator;
import java.lang.IllegalArgumentException;

public final class Term implements Comparable<Term> {

    private String query;
    private long weight;

    // Initializes a term with the given query string and weight.
    public Term(String query, long weight){
        if (query == null) {
            throw new NullPointerException();
        }
        if (weight < 0) {
            throw new IllegalArgumentException();
        }
        this.query = query;
        this.weight = weight;
    }

    // Compares the two terms in descending order by weight
    public static Comparator<Term> byReverseWeightOrder(){
        Comparator<Term> weightComparer = new Comparator<Term>() {
            @Override
            public int compare(Term term, Term t1) {
                int compare = Long.compare(term.weight,t1.weight);
                return (compare)*(-1);

            }
        };
        return weightComparer;
    }

    // Compares the two terms in lexicographic order
    // but using only the first r characters of each query.
    public static Comparator<Term> byPrefixOrder(int r){
        if (r < 0) {
            throw new IllegalArgumentException("Please type a positive integer");
        }
        else {
            Comparator<Term> lexicoComparer = new Comparator<Term>() {
                @Override
                public int compare(Term term, Term t1) {
                    int lengthSmaller = r;
                    if (r > Math.min(term.query.length(), t1.query.length())) {
                        lengthSmaller = Math.min(term.query.length(), t1.query.length());
                    }

                    for (int x = 0; x < lengthSmaller; x++) {
                        int compare = Character.compare(term.query.charAt(x), t1.query.charAt(x));
                        if (compare != 0) {
                            return compare;
                        }
                    }
                    return 0;
                }
            };
            return lexicoComparer;
        }
    }

    // Compares the two terms in lexicographic order by query
    public int compareTo(Term that) {
        int lengthSmaller = Math.min(this.query.length(),that.query.length());
        for(int x=0; x<lengthSmaller; x++) {
            int compare = Character.compare(this.query.charAt(x), that.query.charAt(x));
            if (compare != 0) {
                return compare;
            }
        }
        return Integer.compare(this.query.length(),that.query.length());
    }

    // Returns a string representation of this term in the format:
    // the weight, followed by a tab, followed by the query.
    // Note: we decided only to return query, as it was easier for end user to understand
    public String toString() {
        //return this.weight + "\t" + this.query;
        return this.query;
    }
}
