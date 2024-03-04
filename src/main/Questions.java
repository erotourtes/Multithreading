package main;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Questions implements ExampleI {

    void questionInterrupt() {
        var t1 = new Thread(() -> {
            var time = System.currentTimeMillis();
            while (true) {
                System.out.println("Working ... " + Thread.currentThread().isInterrupted());

                try {
                    Thread.sleep(500); // Clears the interrupt flag and throws an exception
                    // similar to the following line
                    // if (Thread.interrupted()) throw new InterruptedException();
                } catch (InterruptedException e) {
                    System.out.println("Interrupted " + Thread.currentThread().isInterrupted());
                    Thread.currentThread().interrupt(); // Need to propagate the interrupt
                }

                if (System.currentTimeMillis() - time > 2000) {
                    break;
                }
            }
        });

        t1.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        t1.interrupt();
    }

    void questionWaitForInsideAnotherThread() {
        var latch = new CountDownLatch(1);

        var t1 = new Thread(() -> {
            System.out.println("1 Working ...");
            try {
                Thread.sleep(1000);
                System.out.println("1 Waiting for 2 to finish ...");
                latch.await();
                System.out.println("1 Done");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        var t2 = new Thread(() -> {
            System.out.println("2 Working ...");
            try {
                Thread.sleep(2000);
                latch.countDown();
                System.out.println("2 Done");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        t1.start();
        t2.start();
    }

    public void questionDoubleLock() {
        var lock = new ReentrantLock();
        var t1 = new Thread(() -> {
            System.out.println("1 Starting ...");
            lock.lock();
            System.out.println("1 Working ...");
            sleep(100);
            lock.lock(); // Lock count is incremented
            System.out.println("1 Double lock");
            lock.unlock();
            lock.unlock(); // Without double unlock it will be a deadlock
            System.out.println("1 Done");
        });

        var t2 = new Thread(() -> {
//            sleep(100);
            // causes illegal monitor exception due to unlocking a lock that is locked by another thread
//            lock.unlock();
            System.out.println("2 Starting ...");
            lock.lock();
            System.out.println("2 Working ...");
            sleep(200);
            lock.unlock();
            System.out.println("2 Done");
        });

        t1.start();
        t2.start();
    }

    public void questionLatch() {
        var wg = new WageGroup(0);
        var t1 = new Thread(() -> {
            wg.countUp();
            System.out.println("1 wg: " + wg.getCount());
            sleep(1000);
            System.out.println("1 Done");
            wg.countDown();
        });

        var t2 = new Thread(() -> {
            wg.countUp();
            System.out.println("2 wg: " + wg.getCount());
            sleep(2000);
            System.out.println("2 Done");
            wg.countDown();
        });

        t1.start();
        t2.start();
        System.out.println("Awaiting ...");
        wg.await(); // Is not guaranteed to be executed before the threads start (so it might skip the steps)
        System.out.println("All done");
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private class WageGroup {
        private volatile int count;
        private final Lock lock = new ReentrantLock();
        private final Condition condition = lock.newCondition();

        public WageGroup(int count) {
            this.count = count;
        }

        public void countDown() {
            lock.lock();
            try {
                count--;
                if (count == 0) condition.signalAll();
            } finally {
                lock.unlock();
            }
        }

        public void countUp() {
            lock.lock();
            try {
                count++;
            } finally {
                lock.unlock();
            }
        }

        public void await() {
            lock.lock();
            try {
                // Spurious wake up (it's a good practice to use while)
                while (count > 0) condition.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }

        public int getCount() {
            return count;
        }
    }


    private void questionStackVsHeap() {
        int stackVariable = 10;
        int[] heapVariable = new int[1];

        Runnable function= () -> {
// Why it is accessible even though stackVariable should be in a different stack?
            System.out.println(stackVariable);
//            stackVariable = 9999; // Does not compile, because it's not final
// The guess is that it's pushed to the stack of the thread that runs the function


            System.out.println(heapVariable[0]);
            heapVariable[0] = 9999;
        };

        var t1 = new Thread(function);
        t1.start();
        try {t1.join();} catch (InterruptedException ignored) {}

        System.out.println(stackVariable);
        System.out.println(heapVariable[0]);
    }

    @Override
    public void example() {
//        questionInterrupt();
//        questionWaitForInsideAnotherThread();
//        questionDoubleLock();
//        questionLatch();
        questionStackVsHeap();
    }
}
