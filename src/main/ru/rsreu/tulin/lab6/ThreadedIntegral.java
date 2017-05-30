package ru.rsreu.tulin.lab6;

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

    ExecutorService pool = Executors.newFixedThreadPool(threadCount);
    final double delta = 0.25;
    for (int index = 0; index < threadCount; index++) {
      Integral integral = new Integral(n / threadCount);
      double from = delta * (double) index;
      double to = (double) from + delta;
      integral.setFrom(from);
      integral.setTo(to);

      Future<Double> future = pool.submit(integral);
      results.add(future);
    }

    double result = 0;
    for (Future<Double> future : results) {
      double subResult = future.get();
      result += subResult;
    }
    return result;
  }

}
