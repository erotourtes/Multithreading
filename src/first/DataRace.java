package first;

import main.ExampleI;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class DataRace implements ExampleI {

    private int x = 0;
    private int y = 0;

//    private volatile int x = 0;
//    private volatile int y = 0;

    public void increment() {
        x++; // order might be changed (if x is not volatile)
        y++;
    }

    public void incSafe() {
        synchronized (this) {
            x++;
            y++;
        }
    }

    public int check() {
        return y - x;
    }

    @Override
    public void example() {
        var t1 = new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                increment();
//                incSafe(); doesn't solve the data race, because check() is not synchronized
            }
        });

        var t2 = new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                var result = check();
                if (result > 0)
                    System.out.println("y > x: " + result);
                else if (result < 0)
                    System.out.println("x > y: " + result);
            }
        });

        t1.start();
        t2.start();

        var a = new ReentrantReadWriteLock();
        Lock b = a.readLock();
        b.lock();

        try {
            t1.join();
            t2.join();
            check();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}