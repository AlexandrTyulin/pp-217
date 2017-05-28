package ru.rsreu.tulin.lab8;


public class CustomSemaphore {

  public CustomSemaphore(int permits) {
    this.permits = permits;
    this.acquiredCount = 0;
  }

  public void acquire() throws InterruptedException {
    acquire(1);
  }

  public void acquire(int permits) throws InterruptedException {
    synchronized (lock) {
      ensureValidness(permits);
      if (this.acquiredCount + permits > this.permits) {
        lock.wait();
      }
      this.acquiredCount += permits;
      System.out.println(String.format("[%s] acquired", Thread.currentThread().getName()));
      System.out.flush();
    }
  }

  private void ensureValidness(int permits) throws IllegalArgumentException {
    if (permits < 0) {
      throw new IllegalArgumentException("permits cannot be negative");
    }
  }

  public void release() {
    release(1);
  }

  public void release(int permits) {
    synchronized (lock) {
      ensureValidness(permits);
      this.acquiredCount -= permits;

      System.out.println(String.format("[%s] released", Thread.currentThread().getName()));
      lock.notify();
    }
  }

  private Object lock = new Object();
  private final int permits;
  private volatile int acquiredCount;
}

