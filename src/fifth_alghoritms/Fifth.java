package fifth_alghoritms;

import main.ExampleI;

import java.util.Arrays;

public class Fifth implements ExampleI {
    @Override
    public void example() {

        var array = new int[]{3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5};
        MergeSort.sort(array);
        System.out.println(Arrays.toString(array));

        array = new int[]{3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5};
        MergeSort.sortParallel(array, 4);
        System.out.println(Arrays.toString(array));
        MergeSort.service.shutdown();
    }

//    private static double benchmarkTest(int times, Consumer<int[]> consumer) {
//        var numOfThreads = 4;
//        var pool = Executors.newFixedThreadPool(numOfThreads);
//
//        var results = new ArrayList<Future<Integer>>();
//        for (int i = 0; i < numOfThreads; i++) {
//            var res = pool.submit(() -> {
//                int sum = 0;
//                for (int j = 0; j < times / numOfThreads; j++) sum += benchmark(consumer);
//                return sum;
//            });
//            results.add(res);
//        }
//
//        pool.shutdown();
//        return results.stream().mapToInt(f -> {
//            try {
//                return f.get();
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }).sum() / (double) times;
//    }
//
//    private static int benchmark(Consumer<int[]> consumer) {
//        var array = new int[100_000];
//        for (int i = 0; i < array.length; i++) {
//            array[i] = (int) (Math.random() * 100_000);
//        }
//        var start = System.currentTimeMillis();
//        consumer.accept(array);
//        var end = System.currentTimeMillis();
//        return (int) (end - start);
//    }
}
