package ru.rsreu.tulin.lab6;

import java.util.concurrent.Callable;

public class Integral implements Callable<Double> {

  private double from = 0;
  private double to;
  private final int iterationCount;
  private double result = 0;
  private Boolean isResultReady = false;

  public Integral(int iterationCount) {
    this.iterationCount = iterationCount;
    this.to = (double) iterationCount;
  }

  public double getIntegral(int n) throws InterruptedException {
    final double a = 0;
    final double b = 1;
    double result;
    double h;

    final double commonIterationInfo = (b - a) / 100;
    double currentIterationInfo = commonIterationInfo;
    result = 0;
    h = (b - a) / n; //Шаг сетки

    for (double current = from; current < to; current += h) {
      {
        if (Thread.currentThread().isInterrupted()) {
          throw new InterruptedException();
        }

        if (current >= currentIterationInfo) {
          System.out.println(Thread.currentThread().getName() + " Текущий результат : \t" + result);
          currentIterationInfo += commonIterationInfo;
        }
        result += inFunction(current); //Вычисляем в средней точке и добавляем в сумму
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
    return getIntegral(iterationCount);
  }

  private double inFunction(double x) {
    return Math.sin(x) * x; //Например, sin(x)
  }
}
