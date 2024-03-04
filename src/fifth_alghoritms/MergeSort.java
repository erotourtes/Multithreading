package fifth_alghoritms;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MergeSort {
    public static void sort(int[] array) {
        if (array.length < 2) return;

        int middle = array.length / 2;
        int[] left = Arrays.copyOfRange(array, 0, middle);
        int[] right = Arrays.copyOfRange(array, middle, array.length);
        sort(left);
        sort(right);

        merge(array, left, right);
    }

    public static final int THREADS = 4;
    public static ExecutorService service = Executors.newFixedThreadPool(THREADS);

    public static void sortParallel(int[] array, int threads) {
        if (array.length < 2) return;

        int middle = array.length / 2;
        int[] left = Arrays.copyOfRange(array, 0, middle);
        int[] right = Arrays.copyOfRange(array, middle, array.length);

        if (threads > 1) {
            var f1 = service.submit(() -> sortParallel(left, threads / 2));
            var f2 = service.submit(() -> sortParallel(right, threads / 2));
            try {
                f1.get();
                f2.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            sort(left);
            sort(right);
        }
        merge(array, left, right);
    }

    private static void merge(int[] array, int[] left, int[] right) {
        var li = 0;
        var ri = 0;
        while (li < left.length && ri < right.length) {
            if (left[li] < right[ri]) array[li + ri] = left[li++];
            else array[li + ri] = right[ri++];
        }

        while (li < left.length) array[li + ri] = left[li++];
        while (ri < right.length) array[li + ri] = right[ri++];
    }
}
