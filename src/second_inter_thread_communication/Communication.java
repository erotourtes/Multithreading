package second_inter_thread_communication;

import main.ExampleI;

public class Communication implements ExampleI {
    public void consume() {
        // wait and notify works only because the intrinsic lock is the same (this)
        synchronized (this) {
            System.out.println("Consume");
            try {
                wait(); // release the intrinsic lock and wait for notify (it blocks the thread)
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Consume done");
        }
    }

    public void produce() {
        synchronized (this) {
            System.out.println("Produce");
            notify();
            // The intrinsic lock is not released until the end of the synchronized block
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Produce done");
        }
    }

    @Override
    public void example() {
        var c = new Communication();
        var t1 = new Thread(() -> {
            c.consume();
            System.out.println("Outside consume");
        });
        var t2 = new Thread(c::produce);
        t1.start();
        t2.start();
    }
}
