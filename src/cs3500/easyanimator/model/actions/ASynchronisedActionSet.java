package cs3500.easyanimator.model.actions;

import cs3500.easyanimator.model.shapes.IShape;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an abstract implementation of an animation. Implements {@link ISynchronisedActionSet}. The
 * simplest form of an animation is an empty list of commands and a given amount of time.
 */
public abstract class ASynchronisedActionSet implements ISynchronisedActionSet {

  protected int start;
  protected int end;
  protected ArrayList<IActionCommand> command;

  /**
   * Constructor to create an instance of a singular shapes ISynchronisedActionSet. Can be abstracted to be
   * applied to more than one shape in the future.
   *
   * @param start   the start time of the animation
   * @param end     the end time of the shapes animation
   * @param command the command to be applied to this shape for the given amount of time.
   * @throws IllegalArgumentException if the commands are null
   * @throws IllegalArgumentException ticks are less than 0
   * @throws IllegalArgumentException if starting tick is greater han or equal to ending tick
   */
  public ASynchronisedActionSet(int start, int end, List<IActionCommand> command) {
    if (command == null) {
      throw new IllegalArgumentException("Arguments cannot be null.");
    }
    if (start < 0 || end < 0) {
      throw new IllegalArgumentException("Ticks cannot be less than 0.");
    }
    if (start >= end) {
      throw new IllegalArgumentException("Starting tick cannot be greater than "
          + "or equal to the ending tick.");
    }
    this.start = start;
    this.end = end;
    this.command = new ArrayList<IActionCommand>(command);
  }

  @Override
  public int getStartTick() {
    return start;
  }

  @Override
  public int getEndTick() {
    return end;
  }

  @Override
  public void applyAnimation(IShape shape) {
    for (IActionCommand ac : command) {
      ac.mutate(shape);
    }
  }

  @Override
  public ArrayList<IActionCommand> getCommandList() {
    return this.command;
  }

  @Override
  public void removeCommand(int i) {
    this.command.remove(i);
  }

  @Override
  public boolean containsInstanceOf(IActionCommand ac) {
    for (IActionCommand com : command) {
      if (com.getClass().equals(ac.getClass())) {
        return true;
      }
    }
    return false;
  }
}

