package ru.rsreu.tulin.lab7;

import java.util.concurrent.ExecutionException;

public class Runner {

  public static void main(String[] args) {
    LazyThreadedIntegral integral = new LazyThreadedIntegral();
    double result = 0;
    try {
      result = integral.getIntegral(10000);
      System.out.println("Result=" + result);
    } catch (InterruptedException e) {
      System.err.println("Interrupted.");
    } catch (ExecutionException e) {
      System.err.println("Something went wrong:" + e.getCause().getMessage());
    }
  }
}
