package ru.rsreu.tulin.lab4;

public enum TypeCommand {
  START,
  STOP,
  AWAIT,
  EXITS;

  public static TypeCommand fromString(String inString) {
    if (inString.contains("start")) {
      return TypeCommand.START;
    }
    if (inString.contains("exit")) {
      return TypeCommand.EXITS;
    }
    if (inString.contains("stop")) {
      return TypeCommand.STOP;
    }
    if (inString.contains("await")) {
      return TypeCommand.AWAIT;
    }
    return null;
  }

  public boolean needsIntArgument() {
    return (this == STOP) || (this == AWAIT) || (this == START);
  }
}
