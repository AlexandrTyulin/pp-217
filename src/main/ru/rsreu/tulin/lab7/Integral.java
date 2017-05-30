package ru.rsreu.tulin.lab7;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class Integral implements Callable<Double> {

  private double from = 0;
  private double to = 1;
  private final int iterationCount;
  private Logger logger;
  private Semaphore semaphore = new Semaphore(1);
  private CountDownLatch latch = new CountDownLatch(1);
  private long endTime = 0;

  public Integral(int iterationCount) {
    this.iterationCount = iterationCount;
    logger = new Logger(iterationCount);
  }

  public long getEndTime() {
    return endTime;
  }

  public CountDownLatch getLatch() {
    return latch;
  }

  public void setLatch(CountDownLatch latch) {
    this.latch = latch;
  }

  public Logger getLogger() {
    return logger;
  }

  public void setLogger(Logger logger) {
    this.logger = logger;
  }

  public Semaphore getSemaphore() {
    return semaphore;
  }

  public void setSemaphore(Semaphore semaphore) {
    this.semaphore = semaphore;
  }

  double inFunction(double x) {
    return Math.sin(x) * x;
  }

  double getIntegral(int n) throws InterruptedException {
    double result;
    double h;

    result = 0;
    h = (to - from) / n; //Шаг сетки

    int iterationNumber = 0;
    for (double current = from; current < to; current += h, iterationNumber++) {
      {
        if (Thread.currentThread().isInterrupted()) {
          throw new InterruptedException();
        }
        result += inFunction(current); //Вычисляем в средней точке и добавляем в сумму

        int increaseInterval = n / 10;
        if (iterationNumber % increaseInterval == 0) {
          logger.increaseIterationNumber(increaseInterval);
        }
        if (logger.isTimeToPrint()) {
          logger.printStatus();
          logger.increasePercent();
        }
      }
    }
    result *= h;

    return result;
  }

  public double getFrom() {
    return from;
  }

  public void setFrom(double from) {
    this.from = from;
  }

  public double getTo() {
    return to;
  }

  public void setTo(double to) {
    this.to = to;
  }

  public Double call() throws Exception {
    semaphore.acquire();
    double result;
    try {
      result = getIntegral(iterationCount);
      endTime = System.nanoTime();
    } finally {
      semaphore.release();
      latch.countDown();
    }
    return result;
  }
}
