package cs3500.easyanimator.model.actions;

import cs3500.easyanimator.model.shapes.IShape;
import java.util.ArrayList;

/**
 * To represent an animation for one {@link IShape} with a given time period and at most one of each
 * type of {@link IActionCommand}.
 */
public interface ISynchronisedActionSet extends Comparable<ISynchronisedActionSet> {

  /**
   * Gets the starting time of an {@link ISynchronisedActionSet}.
   *
   * @return the start time of an {@link ISynchronisedActionSet}.
   */
  int getStartTick();

  /**
   * Gets the end time of an {@link ISynchronisedActionSet}.
   *
   * @return the end time of an {@link ISynchronisedActionSet}.
   */
  int getEndTick();

  /**
   * Applies the {@link ISynchronisedActionSet} to the assigned {@link IShape}.
   *
   * @param shape the shape to be mutated
   */
  void applyAnimation(IShape shape);

  /**
   * Outputs an {@link ArrayList} of all of the {@link IActionCommand} associated with this shape
   * for this given time period.
   *
   * @return an {@link ArrayList} of the commands.
   */
  ArrayList<IActionCommand> getCommandList();

  /**
   * Removes a command from an {@link ISynchronisedActionSet}.
   *
   * @param i the index that holds the {@link IActionCommand} to be removed.
   */
  void removeCommand(int i);

  /**
   * Returns a boolean that specifies whether an {@link ISynchronisedActionSet} contains an instance of a
   * particular {@link IActionCommand}. This is used to avoid inconsistent shape states in an
   * {@link ISynchronisedActionSet}.
   *
   * @param ac the {@link IActionCommand} class that is being searched for.
   * @return true if the {@link IActionCommand} class is contained within the searched {@link
   * ISynchronisedActionSet}.
   */
  boolean containsInstanceOf(IActionCommand ac);
}
