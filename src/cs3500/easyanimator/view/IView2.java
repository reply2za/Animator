package cs3500.easyanimator.view;

public interface IView2 extends IView {

  /**
   * Updates the current ticks of the animation for the view. To be used for GUI updates.
   * @param ticks the current tick of the animation
   */
  void updateTicks(int ticks);

  /**
   * Makes the play button blue and the pause button black.
   */
  void triggerPlayButtonBlue();

  /**
   * Makes the pause button blue and the play button black;
   */
  void triggerPauseButtonBlue();

}
