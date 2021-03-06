package ru.rsreu.tulin.lab5;

public class Integral implements Runnable {

  private double from = 0;
  private double to = 1;
  private final int iterationCount;
  private double result = 0;
  private Boolean isResultReady = false;

  public Integral(int iterationCount) {
    this.iterationCount = iterationCount;
  }

  public double getIntegral(int n) throws InterruptedException {
    final double a = 0;
    final double b = 1;
    double result;
    double h;

    final double commonIterationInfo = (b - a) / 100;
    double currentIterationInfo = commonIterationInfo;
    result = 0;
    h = (b - a) / n;

    for (double current = from; current < to; current += h) {
      {
        if (Thread.currentThread().isInterrupted()) {
          throw new InterruptedException();
        }

        if (current >= currentIterationInfo) {
          System.out.println(Thread.currentThread().getName() + " Текущий результат : \t" + result);
          currentIterationInfo += commonIterationInfo;
        }
        result += inFunction(current);
      }
    }
    result *= h;

    return result;
  }

  public void run() {
    try {
      synchronized (this) {
        isResultReady = false;
        result = getIntegral(iterationCount);
        isResultReady = true;
        this.notify();
      }
    } catch (InterruptedException e) {
      System.err.println(Thread.currentThread().getName() + " was interrupted.");
    } catch (ArithmeticException e) {
      System.err.println("Seems that you entered invalid <n>");
    }
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

  private double inFunction(double x) {
    return Math.sin(x) * x; //Например, sin(x)
  }
}
