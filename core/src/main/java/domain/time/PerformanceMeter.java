package domain.time;

import java.util.concurrent.Callable;

public class PerformanceMeter {
    public static void measureExecutionTime(Runnable task) {
        long startTime = System.nanoTime();
        task.run();
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        System.out.println("\n---------\nExe-time: " + duration/1_000_000_000.0 + "s\n---------");
    }

    // Method to measure execution time of a Callable task and return its result
    public static <T> T measureExecutionTime(Callable<T> task) throws Exception {
        long startTime = System.nanoTime();
        T result = task.call();
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        System.out.println("\n---\nExe-time: " + duration/1_000_000_000.0 + "s\n---");
        return result;
    }
}
