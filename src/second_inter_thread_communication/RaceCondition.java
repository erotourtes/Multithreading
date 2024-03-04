package second_inter_thread_communication;

import main.ExampleI;

import java.util.LinkedList;
import java.util.List;

public class RaceCondition implements ExampleI {
    private int counter = 0;
    private final List<Thread> threads = new LinkedList<>();

    private synchronized void incSyncKeyword() { // Synchronized keyword is allowing only one thread to access this method at a time
        counter++;
    }

    public void start() {
        var t = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                incSyncKeyword();
            }
        });

        threads.add(t);
        t.start();
    }

    public void show() {
        for (var t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(counter);
    }

    @Override
    public void example() {
        var rc = new RaceCondition();
        rc.start();
        rc.start();
        rc.show();
    }
}
