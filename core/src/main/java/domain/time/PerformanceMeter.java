package domain.time;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 * The PerformanceMeter class provides utility methods for measuring and reporting execution time of tasks.
 * It offers functionality to measure the execution time of both Runnable and Callable tasks, as well as
 * manual start and end measurement capabilities. The class also allows customization of the output handler
 * for reporting execution times.
 *
 * Key features:
 * - Measure execution time of Runnable and Callable tasks
 * - Manual start and end measurement for more complex scenarios
 * - Customizable output handling
 * - Retrieval of the last measured execution time
 * 
 * The class uses System.nanoTime() for high-precision timing and converts nanoseconds to seconds for output.
 */
public class PerformanceMeter {
    private static final double NANO_TO_SECONDS = 1_000_000_000.0;
    private static Consumer<String> outputHandler = System.out::println;

    public static void measureExecutionTime(Runnable task) {
        long startTime = System.nanoTime();
        task.run();
        long endTime = System.nanoTime();
        printExecutionTime(endTime - startTime);
    }

    public static <T> T measureExecutionTime(Callable<T> task) throws Exception {
        long startTime = System.nanoTime();
        T result = task.call();
        long endTime = System.nanoTime();
        printExecutionTime(endTime - startTime);
        return result;
    }

    private static void printExecutionTime(long durationNanos) {
        double durationSeconds = durationNanos / NANO_TO_SECONDS;
        outputHandler.accept(String.format("\n---------\nExecution time: %.6f seconds\n---------", durationSeconds));
    }

    public static void setOutputHandler(Consumer<String> handler) {
        outputHandler = handler;
    }

    public static double getLastExecutionTimeSeconds() {
        return lastExecutionTime / NANO_TO_SECONDS;
    }

    private static long lastExecutionTime = 0;

    public static void startMeasurement() {
        lastExecutionTime = System.nanoTime();
    }

    public static void endMeasurement() {
        long endTime = System.nanoTime();
        lastExecutionTime = endTime - lastExecutionTime;
        printExecutionTime(lastExecutionTime);
    }
}
