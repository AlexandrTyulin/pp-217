package ru.rsreu.tulin.lab8;

public class CustomLatch {

  public CustomLatch(int count) {
    this.count = count;
  }

  public void await() throws InterruptedException {
    synchronized (monitor) {
      if (count > 0) {
        monitor.wait();
      }
    }
  }

  public void countDown() {
    synchronized (monitor) {
      count--;

      if (count == 0) {
        monitor.notifyAll();
      }
    }
  }

  private volatile int count;
  private Object monitor = new Object();
}
