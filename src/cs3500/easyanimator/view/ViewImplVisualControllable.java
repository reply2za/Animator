package cs3500.easyanimator.view;

import cs3500.easyanimator.model.IReadOnlyModel;

public class ViewImplVisualControllable extends ViewImplVisual {

  /**
   * Singular constructor that takes in the speed of the animation to be played.
   *
   * @param ticksPerSecond the speed of the animation.
   * @param readOnlyModel  the given read-only model
   */
  public ViewImplVisualControllable(int ticksPerSecond, IReadOnlyModel readOnlyModel) {
    super(ticksPerSecond, readOnlyModel);
  }




}
