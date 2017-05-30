package ru.rsreu.tulin.lab5;

public class LazyThreadedIntegral {

  private ThreadedIntegral instance = null;

  public double getIntegral(int n) throws InterruptedException {
    if (instance == null) {
      synchronized (this) {
        if (instance == null) {
          instance = new ThreadedIntegral();
        }
      }
    }

    return instance.getIntegral(n);
  }
}
