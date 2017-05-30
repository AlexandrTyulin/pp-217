package ru.rsreu.tulin.lab8;

public class Logger {

  private static final double PERCENT_DELTA = 0.1;
  private int iterationNumber = 0;
  private float currentPercent = 0.0f;
  private final int maxIterationCount;
  private CustomLock lock = new CustomLock();

  public Logger(int maxIterationCount) {
    this.maxIterationCount = maxIterationCount;
  }

  public int getIterationNumber() throws InterruptedException {
    lock.lock();
    int result = iterationNumber;
    lock.unlock();
    return result;
  }

  public boolean isTimeToPrint() throws InterruptedException {
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

  public void increaseIterationNumber(int delta) throws InterruptedException {
    lock.lock();
    iterationNumber += delta;
    lock.unlock();
  }

  public void increaseIterationNumber() throws InterruptedException {
    increaseIterationNumber(1);
  }

  public void increasePercent() throws InterruptedException {
    lock.lock();
    currentPercent += PERCENT_DELTA;
    lock.unlock();
  }

  public void printStatus() throws InterruptedException {
    lock.lock();
    try {
      int percents = (int) (getCurrentPercents() * 100);
      System.out.println(Thread.currentThread().getName() + " ready on " + percents + "%");
    } finally {
      lock.unlock();
    }
  }
}
