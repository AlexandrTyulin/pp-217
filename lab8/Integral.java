package ru.rsreu.tulin.lab8;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;


public class Integral implements Callable<Double> {

  private double from = 0;
  private double to = 1;
  private final int n;
  private double result = 0;
  private Boolean isResultReady = false;
  private Logger logger;
  private CustomSemaphore semaphore = new CustomSemaphore(1);
  private CustomLatch latch = new CustomLatch(1);
  private long endTime = 0;

  public Integral(int n) {
    this.n = n;
    logger = new Logger(n);
  }

  public long getEndTime() {
    return endTime;
  }

  public CustomLatch getLatch() {
    return latch;
  }

  public void setLatch(CustomLatch latch) {
    this.latch = latch;
  }

  public Logger getLogger() {
    return logger;
  }

  public void setLogger(Logger logger) {
    this.logger = logger;
  }

  public CustomSemaphore getSemaphore() {
    return semaphore;
  }

  public void setSemaphore(CustomSemaphore semaphore) {
    this.semaphore = semaphore;
  }

  double InFunction(double x) //Подынтегральная функция
  {
    return Math.sin(x) * x; //Например, sin(x)
  }

  double getIntegral(int n) throws InterruptedException {
    double result, h;

    result = 0;
    h = (to - from) / n; //Шаг сетки

    int iterationNumber = 0;
    for (double current = from; current < to; current += h, iterationNumber++) {
      {
        if (Thread.currentThread().isInterrupted()) {
          throw new InterruptedException();
        }
        result += InFunction(current); //Вычисляем в средней точке и добавляем в сумму

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

  public synchronized double getResult() throws InterruptedException {
    while (!isResultReady) {
      this.wait();
    }

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
      result = getIntegral(n);
      endTime = System.nanoTime();
    } finally {
      semaphore.release();
      latch.countDown();
    }
    return result;
  }
}


