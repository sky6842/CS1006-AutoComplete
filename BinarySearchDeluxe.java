import java.util.Comparator;

// Search API
public class BinarySearchDeluxe {

    // Returns the index of the first key in a[] that
    // equals the search key, or -1 if no such key.
    public static <Key> int firstIndexOf(Key[] a, Key key, Comparator<Key> comparator) throws NullPointerException {
        //METHOD COUNTING
        int counter = 0;

        if (a == null || key == null || comparator == null) {
            throw new NullPointerException();
        }
        int high = a.length - 1;
        int low = 0;
        int firstIndex = -1;

        while (high >= low) {
            int mid = (low + high)/2;

	    //looking for first index, so we ignore everything higher
            if (comparator.compare(key, a[mid]) == 0) {
                firstIndex = mid;
                high = mid - 1;
                counter++;
            }
            else if (comparator.compare(key, a[mid]) > 0) {
                low = mid + 1;
                counter++;
            }
            else {
                high = mid - 1;
                counter++;
            }
        }
        System.out.println("firstIndexOf count: " + counter);
        return firstIndex;
    }

    // Returns the index of the last key in a[] that
    // equals the search key, or -1 if no such key.
    public static <Key> int lastIndexOf(Key[] a, Key key, Comparator<Key> comparator) {
        //METHOD COUNTING
        int counter = 0;

        if (a == null || key == null || comparator == null) {
            throw new NullPointerException();
        }
        int high = a.length - 1;
        int low = 0;
        int lastIndex = -1;

        while (high >= low) {
            int mid = (low + high)/2;

            //looking for last index, so we ignore everything lower
            if (comparator.compare(key, a[mid]) == 0) {
                lastIndex = mid;
                low = mid + 1;
                counter++;
            }
            else if (comparator.compare(key, a[mid]) > 0) {
                low = mid + 1;
                counter++;
            }
            else {
                high = mid - 1;
                counter++;
            }
        }
        System.out.println("lastIndexOf count: " + counter);
        return lastIndex;
    }
}
