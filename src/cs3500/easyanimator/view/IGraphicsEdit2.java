package cs3500.easyanimator.view;

/**
 * An interface to represent the public commands that are involved in re-painting the animations.
 * This interface allows for changes to be made in a backwards-compatible nature. This interface
 * supports standard animation controls to be called on by the view for graphics response.
 */
public interface IGraphicsEdit2 extends IGraphicsEdit {

  /**
   * Changes the speed of the animation based on the input from the user passed to the view.
   *
   * @param secondsPerTick the speed of the animation based on the milliseconds of the Timer.
   */
  public void setTickSpeed(int secondsPerTick) ;

  /**
   * Starts playing the animation by setting the play boolean to true.
   */
  public void play(boolean play) ;


  /**
   * Sets the current amount of ticks passed in the animation.
   *
   * @param currentTick the current amount of ticks we want to set the animation to.
   */
  public void setCurrentTick(int currentTick) ;

  /**
   * Resets the fields of the animation to its original state for playback. Useful for restarting.
   */
  public void resetFields() ;

  /**
   * Getter method that returns the amount of ticks passed in the animation.
   *
   * @return the amount of ticks passed in the animation.
   */
  public int getCurrentTick() ;

  /**
   * Getter method to extract the current loop state of an animation.
   *
   * @return the boolean of whether or not it is looping.
   */
  public boolean isLooping() ;

  /**
   * Set the looping state of an animation to whatever boolean is given.
   *
   * @param l the looping state to be set to.
   */
  public void setLooping(boolean l);

}
