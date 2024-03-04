package third_concepts;

import main.ExampleI;

import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class MySemaphore implements ExampleI {
    private final Semaphore semaphore = new Semaphore(3, true);

    void download() {
        try {
            semaphore.acquire();
            download(700);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            semaphore.release();
        }
    }

    private void download(int time) {
        System.out.println("Downloading on thread: " + Thread.currentThread().getName() + " ...");
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void example() {
        var service = Executors.newCachedThreadPool();
        for (int i = 0; i < 12; i++) {
            service.execute(this::download);
        }
        service.shutdown();
    }
}
