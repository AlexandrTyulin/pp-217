package ru.rsreu.tulin.lab8;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadedIntegral {

  private final int threadCount;

  public ThreadedIntegral(int threadCount) {
    this.threadCount = threadCount;
  }

  public ThreadedIntegral() {
    this(Runtime.getRuntime().availableProcessors());
  }

  public double getIntegral(int n) throws InterruptedException, ExecutionException {
    List<Future<Double>> results = new ArrayList<Future<Double>>();
    List<Integral> integrals = new ArrayList<Integral>(threadCount);

    Logger logger = new Logger(n);
    CustomSemaphore semaphore = new CustomSemaphore(threadCount / 2);
    CustomLatch latch = new CustomLatch(threadCount);
    ExecutorService pool = Executors.newFixedThreadPool(threadCount);
    final double delta = 0.25;
    for (int index = 0; index < threadCount; index++) {
      Integral integral = new Integral(n / threadCount);
      double from = delta * (double) index;
      double to = (double) from + delta;
      integral.setFrom(from);
      integral.setTo(to);
      integral.setLogger(logger);
      integral.setSemaphore(semaphore);
      integral.setLatch(latch);

      integrals.add(integral);
      Future<Double> future = pool.submit(integral);
      results.add(future);
    }

    latch.await();
    long overallEndTime = System.nanoTime();

    double result = 0;
    for (int index = 0; index < results.size(); index++) {
      Future<Double> future = results.get(index);
      double subResult = future.get();
      result += subResult;

      long duration = overallEndTime - integrals.get(index).getEndTime();
      String message =
          String.format("Time of execution for %d task is \t %d \tns", index, duration);
      System.out.println(message);
    }
    return result;
  }
}
