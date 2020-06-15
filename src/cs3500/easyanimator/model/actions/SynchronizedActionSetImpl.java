package cs3500.easyanimator.model.actions;

import java.util.List;

/**
 * To represent multiple actions occurring at the same time. An action is an {@link
 * IActionCommand}.
 */
public class SynchronizedActionSetImpl extends ASynchronisedActionSet implements
    ISynchronisedActionSet {

  /**
   * Accepts the super class implementation of an ISynchronisedActionSet.
   *
   * @param start       start time of the animation
   * @param end         end time of the animation
   * @param commandList the list of commands to be applied to a shape.
   */
  public SynchronizedActionSetImpl(int start, int end, List<IActionCommand> commandList) {
    super(start, end, commandList);
  }

  @Override
  public int compareTo(ISynchronisedActionSet o) {
    SynchronizedActionSetImpl other = (SynchronizedActionSetImpl) o;
    return this.start - other.start;
  }
}
