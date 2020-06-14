package cs3500.easyanimator.model.actions;


import cs3500.easyanimator.model.shapes.IShape;

/**
 * To represent a single action (or mutation) that can be performed on a shape of type {@link
 * IShape}.
 */
public interface IActionCommand {

  /**
   * Executes the {@link IActionCommand} on the given shape.
   *
   * @param s a given {@link IShape} to perform an {@link IActionCommand} on.
   */
  void mutate(IShape s);
}