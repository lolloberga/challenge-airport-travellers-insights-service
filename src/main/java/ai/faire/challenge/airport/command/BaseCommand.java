package ai.faire.challenge.airport.command;

import ai.faire.challenge.airport.exception.CommandException;

public abstract class BaseCommand<T> {

  public BaseCommand() {
  }

  public final T execute() throws CommandException {
    if (!this.canExecute()) {
      throw new CommandException();
    } else {
      return this.doExecute();
    }
  }

  protected boolean canExecute() throws CommandException {
    return true;
  }

  protected abstract T doExecute();

}
