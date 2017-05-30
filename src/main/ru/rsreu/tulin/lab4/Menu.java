package ru.rsreu.tulin.lab4;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class Menu {

  private PrintStream printStream;
  private Scanner scanner;
  private String inString = "";
  private Map<String, Thread> tasks = new HashMap<String, Thread>();

  public Menu() {

    this.scanner = new Scanner(System.in);
    this.printStream = new PrintStream(System.out);
  }

  public void launch() {
    printStream.println("Input command (start <n> ,stop index,await index,exit)");
    while (scanner.hasNext()) {
      try {
        inString = scanner.next();
        TypeCommand commandType = TypeCommand.fromString(inString);

        if (commandType == null) {
          continue;
        }

        Integer argument = readArgumentIfNeeded(commandType);
        execute(commandType, argument);
      } catch (InputMismatchException e) {
        printStream.println("Incorrect command");
      }
    }
  }

  private Integer readArgumentIfNeeded(TypeCommand commandType) throws InputMismatchException {
    if (commandType.needsIntArgument()) {
      Integer index = scanner.nextInt();
      return index;
    } else {
      return null;
    }
  }

  private void execute(TypeCommand commandType, Integer argument) throws IllegalArgumentException {
    if (commandType.needsIntArgument() && (argument == null)) {
      throw new IllegalArgumentException("Specified command needs integer as argument");
    }

    String message;
    switch (commandType) {
      case START:
        int index = startTask(argument);
        message = String.format("Task number %d started", index);
        printStream.println(message);
        break;
      case AWAIT:
        printStream.println(argument + " process awaiting!");
        join(argument);
        break;
      case STOP:
        if (stop(argument)) {
          printStream.println("Task number " + argument + " stoped");
        }
        break;

      case EXITS:
        printStream.println("Happy End");
        System.exit(0);
        break;
      default:
        printStream.println("Something went wrong with parsing command");
        System.exit(-1);
    }
  }

  private int startTask(int n) {

    Integral integral = new Integral(n);
    Thread threadIntegral = new Thread(integral);

    final int taskNumber = tasks.size();
    String taskName = Integer.toString(taskNumber);
    threadIntegral.setName(taskName);
    tasks.put(taskName, threadIntegral);
    threadIntegral.start();
    return taskNumber;
  }

  private boolean isIndexValid(int index) {
    return (index >= 0) && (index <= tasks.size());
  }

  private boolean stop(int index) throws IllegalArgumentException {
    if (!isIndexValid(index)) {
      throw new IllegalArgumentException("Illegal index argument.");
    }
    String indexAsString = Integer.toString(index);
    Thread neededTask = tasks.get(indexAsString);
    if (neededTask != null) {
      neededTask.interrupt();
    }
    tasks.remove(indexAsString);
    return neededTask != null;
  }

  private void join(int index) {
    Thread task = null;
    final String indexAsString = String.valueOf(index);
    try {
      task = tasks.get(indexAsString);
      if (task != null) {
        task.join();
      }
    } catch (InterruptedException e) {
      System.err.println(indexAsString + " was interrupted");
    } finally {
      tasks.remove(indexAsString);
    }
  }
}
