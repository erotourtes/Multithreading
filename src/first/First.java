package first;

import main.ExampleI;

class Runnable implements java.lang.Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println("Thread 1: " + i);
        }
    }
}

class MyThread extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println("\t\tThread 3: " + i);
        }
    }
}

public class First implements ExampleI {
    private void creation() {
        Runnable r = new Runnable();
        Thread t = new Thread(r);
        t.start();

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println("\tThread 2: " + i);
            }
        });
        t2.start();

        MyThread t3 = new MyThread();
        t3.start();
    }
    @Override
    public void example() {
//        creation();
        new DataRace().example();
    }
}
