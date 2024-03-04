package second_inter_thread_communication;

import main.ExampleI;

import java.util.concurrent.locks.ReentrantLock;

// TODO: Explain the difference between synchronized keyword and ReentrantLock
public class RLock implements ExampleI {
    private int counter = 0;
    private final Object lock = new Object();

    private void incSyncLock() throws InterruptedException {
        synchronized (lock) {
            System.out.println("Thread: " + Thread.currentThread().getName() + " started");
            Thread.sleep(1000);
            counter++;
            System.out.println("Thread: " + Thread.currentThread().getName() + " done");
        }
    }

    private final ReentrantLock rLock = new ReentrantLock();

    private void incSyncRLock() throws InterruptedException {
        rLock.lock();
        System.out.println("Thread: " + Thread.currentThread().getName() + " started");
        Thread.sleep(2000);
        counter++;
        System.out.println("Thread: " + Thread.currentThread().getName() + " done");
        rLock.unlock();
    }

    public void inc2Times() {
        try {
            var t = new Thread(() -> {
                System.out.println("Regular lock");
                var startTime = System.currentTimeMillis();
                try {
                    incSyncLock();
                    incSyncLock();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Time: " + (System.currentTimeMillis() - startTime) + "ms");
                System.out.println("Reentrant lock (RLock)");
                startTime = System.currentTimeMillis();
                try {
                    incSyncRLock();
                    incSyncRLock();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Time: " + (System.currentTimeMillis() - startTime) + "ms");
            });
            t.start();
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void show() {
        System.out.println(counter);
    }

    @Override
    public void example() {
        var rLock = new RLock();
        rLock.inc2Times();
        rLock.show();
    }
}
