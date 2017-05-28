package ru.rsreu.tulin.lab8;


public class CustomLock {

  CustomLock() {
    lockHoldCount = 0;
  }

  public void lock() {
    synchronized (monitor) {
      if (lockHoldCount == 0) {
        lockHoldCount++;
        lockedBy = Thread.currentThread();
      } else if (lockHoldCount > 0 && lockedBy.equals(Thread.currentThread())) {
        lockHoldCount++;
      } else {
        try {
          while (lockHoldCount != 0) {
            monitor.wait();
          }
          lockHoldCount++;
          lockedBy = Thread.currentThread();
        } catch (InterruptedException e) {
          System.out.println("Lock was interrupted in thread " + Thread.currentThread().getName());
        }
      }
    }
  }

  public void unlock() {
    synchronized (monitor) {
      if (lockHoldCount == 0) {
        throw new IllegalMonitorStateException();
      }

      lockHoldCount--;
      if (lockHoldCount == 0) {
        monitor.notify();
      }
    }
  }

  private volatile int lockHoldCount;
  private volatile Thread lockedBy;
  private Object monitor = new Object();
}
