package ru.rsreu.tulin.lab5;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 26.05.2017.
 */
public class ThreadedIntegral {

  private final int threadCount;

  public ThreadedIntegral(int threadCount) {
    this.threadCount = threadCount;
  }

  public ThreadedIntegral() {
    this(Runtime.getRuntime().availableProcessors());
  }

  public double getIntegral(int n) throws InterruptedException {
    List<Integral> list = new ArrayList<Integral>();

    final double delta = 0.25;
    for (int index = 0; index < threadCount; index++) {
      Integral integral = new Integral(n / threadCount);
      double from = delta * (double) index;
      double to = (double) from + delta;
      integral.setFrom(from);
      integral.setTo(to);

      Thread task = new Thread(integral);
      task.setName(Integer.toString(index));
      task.start();
      list.add(integral);
    }

    double result = 0;
    for (Integral integral : list) {
      result += integral.getResult();
    }
    return result;
  }

}
