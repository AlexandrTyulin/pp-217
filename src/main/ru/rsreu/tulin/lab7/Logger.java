package ru.rsreu.tulin.lab7;

import java.util.concurrent.locks.ReentrantLock;

public class Logger {

  private static final double PERCENT_DELTA = 0.1;
  private int iterationNumber = 0;
  private float currentPercent = 0.0f;
  private final int maxIterationCount;
  private ReentrantLock lock = new ReentrantLock();

  public Logger(int maxIterationCount) {
    this.maxIterationCount = maxIterationCount;
  }

  public int getIterationNumber() {
    lock.lock();
    int result = iterationNumber;
    lock.unlock();
    return result;
  }

  public boolean isTimeToPrint() {
    lock.lock();
    boolean result = false;
    try {
      result = getCurrentPercents() >= currentPercent;
    } finally {
      lock.unlock();
    }
    return result;
  }

  private float getCurrentPercents() {
    return (float) iterationNumber / (float) maxIterationCount;
  }

  public void increaseIterationNumber(int delta) {
    lock.lock();
    iterationNumber += delta;
    lock.unlock();
  }

  public void increaseIterationNumber() {
    increaseIterationNumber(1);
  }

  public void increasePercent() {
    lock.lock();
    currentPercent += PERCENT_DELTA;
    lock.unlock();
  }

  public void printStatus() {
    lock.lock();
    try {
      int percents = (int) (getCurrentPercents() * 100);
      System.out.println(Thread.currentThread().getName() + " ready on " + percents + "%");
    } finally {
      lock.unlock();
    }
  }
}
