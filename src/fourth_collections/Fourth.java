package fourth_collections;

import main.ExampleI;

import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;

public class Fourth implements ExampleI {
    public void syncCollections() {
//        var list = new LinkedList<Integer>();
        var list = Collections.synchronizedList(new LinkedList<Integer>());

        var t1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                list.add(i);
            }
        });
        var t2 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                list.add(i);
            }
        });

        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("should be 20, got: " + list.size());
    }

    public void latch() {
        var latch = new CountDownLatch(5);
        var service = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            service.execute(() -> {
                System.out.println("Running ...");
                latch.countDown();
            });
        }
        try {
            System.out.println("Waiting ...");
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("All done");
        service.shutdown();
    }

    public void cyclicBarrier() {
        var service = Executors.newCachedThreadPool();
        var cb = new CyclicBarrier(5, () -> {
            System.out.println("Shutdown");
            service.shutdown();
        });

        for (int i = 0; i < 5; i++) {
            service.execute(() -> {
                System.out.println("Running " + Thread.currentThread().getName() + " ...");
                try {
                    Thread.sleep(1000);
                    cb.await();
                    System.out.println("After barrier await " + Thread.currentThread().getName());
                } catch (InterruptedException | BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @Override
    public void example() {
//        syncCollections();
//        latch();
        cyclicBarrier();
    }
}
