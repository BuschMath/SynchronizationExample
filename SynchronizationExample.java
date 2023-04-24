/* In this example, we create two threads that increment a shared count variable using a synchronized block. 
    We also use a ConcurrentHashMap and a ConcurrentLinkedQueue to safely share data between threads without 
    explicit synchronization. Additionally, we use a ReentrantLock object to control access to a shared 
    resource and an atomic variable to increment a counter without the need for explicit synchronization. */

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SynchronizationExample {

    private static int count = 0;
    private static final Object lock = new Object();
    private static final Lock reentrantLock = new ReentrantLock();
    private static final ConcurrentHashMap<Integer, String> concurrentHashMap = new ConcurrentHashMap<>();
    private static final ConcurrentLinkedQueue<String> concurrentQueue = new ConcurrentLinkedQueue<>();

    public static void main(String[] args) throws InterruptedException {

        // Create multiple threads to increment the count
        Thread t1 = new IncrementThread();
        Thread t2 = new IncrementThread();
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Final count: " + count);

        // Add some items to the concurrent collections
        for (int i = 0; i < 10; i++) {
            concurrentHashMap.put(i, "value-" + i);
            concurrentQueue.offer("item-" + i);
        }

        // Iterate over the concurrent collections
        synchronized (lock) {
            for (Integer key : concurrentHashMap.keySet()) {
                System.out.println(key + ": " + concurrentHashMap.get(key));
            }
            for (String item : concurrentQueue) {
                System.out.println(item);
            }
        }

        // Use a lock object to access a shared resource
        reentrantLock.lock();
        try {
            // Do something with the shared resource
            System.out.println("Acquired lock");
        } finally {
            reentrantLock.unlock();
        }

        // Use an atomic variable to increment a counter
        int atomicCount = 0;
        atomicCount++;

        System.out.println("Atomic count: " + atomicCount);
    }

    private static class IncrementThread extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 100000; i++) {
                synchronized (lock) {
                    count++;
                }
            }
        }
    }
}
