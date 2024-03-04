package third_concepts;

import main.ExampleI;

public class Third implements ExampleI {
    @Override
    public void example() {
//        new Volatile().example();
//        new Deadlock().example();
        new MySemaphore().example();
    }
}
