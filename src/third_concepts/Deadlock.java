package third_concepts;

import main.ExampleI;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Deadlock implements ExampleI {
    private final Lock lock1 = new ReentrantLock();
    private final Lock lock2 = new ReentrantLock();

    void task1() {
        System.out.println("Task 1: Holding lock 1");
        lock1.lock();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Task 1: Holding lock 2");
        lock2.lock();

        // Deadlock
        lock2.unlock();
        lock1.unlock();
        System.out.println("Is not gonna happen. Task 1: Done");
    }

    void task2() {
        System.out.println("Task 2: Holding lock 2");
        lock2.lock();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Task 2: Holding lock 1");
        lock1.lock();

        // Deadlock
        lock1.unlock();
        lock2.unlock();
        System.out.println("Is not gonna happen. Task 2: Done");
    }

    @Override
    public void example() {
        var t1 = new Thread(this::task1);
        var t2 = new Thread(this::task2);
        t1.start();
        t2.start();
    }
}