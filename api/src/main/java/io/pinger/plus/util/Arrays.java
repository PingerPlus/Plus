package io.pinger.plus.util;

public class Arrays {

    /**
     * This method compares two different int arrays.
     *
     * @param a the first array
     * @param b the second array
     * @return the result of the comparison: more or equal to 1 is first is larger, less
     *      or equal to -1 if the second is larger, and 0 if they are the same
     */
    public static int compare(int[] a, int[] b) {
        if (a == b) {
            return 0;
        }

        // A null array is less than a non-null array
        if (a == null || b == null) {
            return a == null ? -1 : 1;
        }

        final int length = Math.min(a.length, b.length);
        for (int i = 0; i < length; i++) {
            final int oa = a[i];
            final int ob = b[i];
            if (oa != ob) {
                return Integer.compare(oa, ob);
            }
        }

        return a.length - b.length;
    }

}
