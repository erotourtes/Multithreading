package third_concepts;

import main.ExampleI;

public class Volatile implements ExampleI {
    private boolean terminated = false;
    // TODO: check for volatile
//    private volatile boolean terminated = false;

    @Override
    public void example() {
        var t1 = new Thread(() -> {
            while (!terminated) {
                System.out.println("Working ...");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        var t2 = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            terminated = true;
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
