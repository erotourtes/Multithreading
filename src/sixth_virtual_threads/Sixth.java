package sixth_virtual_threads;

import main.ExampleI;

import java.util.concurrent.Executors;

public class Sixth implements ExampleI {
    void creation() {
        Runnable r = () -> System.out.println("Cur: " + Thread.currentThread());
        Thread t = Thread.ofPlatform().unstarted(r);
        t.start();

        // Virtual thread
        // or Executors.newVirtualThreadPerTaskExecutor().execute(r);
        Thread vt = Thread.ofVirtual().unstarted(r);
        vt.start();

        try {
            t.join();
            vt.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void example() {
        creation();
    }
}
