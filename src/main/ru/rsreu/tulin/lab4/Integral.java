package ru.rsreu.tulin.lab4;

public class Integral implements Runnable {

  private final int iterationCount;

  public Integral(int iterationCount) {
    this.iterationCount = iterationCount;
  }

  static double inFunction(double x) {
    return Math.sin(x) * x;
  }

  static double getIntegral(int n) throws InterruptedException {
    byte a = 0;
    byte b = 1;
    int i;
    double result;
    double h;

    int commonIterationInfo = n / 100;
    byte currentIterationInfo = 1;
    result = 0;
    h = (b - a) / n;

    for (i = 0; i < n; i++) {
      if (Thread.currentThread().isInterrupted()) {
        throw new InterruptedException();
      }

      if (i == commonIterationInfo * currentIterationInfo) {
        System.out.println(
            Thread.currentThread().getName() + " Загрузка : " + currentIterationInfo + " %");
        currentIterationInfo++;
      }
      result += inFunction(a + h * (i + 0.5));
    }
    System.out.print(result);
    result *= h;

    return result;
  }

  public void run() {

    try {
      getIntegral(iterationCount);
    } catch (InterruptedException e) {
      System.err.println(Thread.currentThread() + " was interrupted.");
    } catch (ArithmeticException e) {
      System.err.println("Seems that you entered invalid <iterationCount>");
    }
  }
}
