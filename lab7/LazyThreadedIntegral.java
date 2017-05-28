package ru.rsreu.tulin.lab7;


import java.util.concurrent.ExecutionException;


public class LazyThreadedIntegral {

  private ThreadedIntegral instance = null;

  public double getIntegral(int n) throws InterruptedException, ExecutionException {
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
