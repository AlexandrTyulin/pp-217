package ru.rsreu.tulin.lab8;


import java.util.concurrent.locks.ReentrantLock;

public class CustomLock {

  CustomLock() {
    lockHoldCount = 0;
  }

  public void lock() throws InterruptedException {
    synchronized (monitor) {
      if (lockHoldCount == 0) {
        lockHoldCount++;
        lockedBy = Thread.currentThread();
      } else if (lockHoldCount > 0 && lockedBy.equals(Thread.currentThread())) {
        lockHoldCount++;
      } else {
          while (lockHoldCount != 0) {
            monitor.wait();
          }
          lockHoldCount++;
          lockedBy = Thread.currentThread();
      }
    }
  }

  public void unlock() {
    synchronized (monitor) {
      if (lockHoldCount == 0) {
        throw new IllegalMonitorStateException();
      }else if(!Thread.currentThread().equals(lockedBy)) {
        throw new IllegalMonitorStateException();
      }

      lockHoldCount--;
      if (lockHoldCount == 0) {
        monitor.notify();
      }
    }
  }

  private volatile int lockHoldCount;
  private Thread lockedBy;
  private Object monitor = new Object();
}
