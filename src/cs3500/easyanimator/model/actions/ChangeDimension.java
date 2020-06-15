package cs3500.easyanimator.model.actions;


import cs3500.easyanimator.model.shapes.IShape;

/**
 * Represents a change of state (position) for any given object.
 */
public class ChangeDimension implements IActionCommand {

  private final int w;
  private final int h;
  private int ticks;

  /**
   * A ChangeDimension constructor. Given a positive width and height, creates a dimension object
   * and assigns it to the class field.
   *
   * @param w     the new width
   * @param h     the new height
   * @param ticks the number of ticks to perform the given change
   * @throws IllegalArgumentException if ticks are less than or equal to 0
   */
  public ChangeDimension(int w, int h, int ticks) {
    if (ticks <= 0) {
      throw new IllegalArgumentException("Ticks cannot be less than or equal to 0.");
    }
    this.ticks = ticks;
    this.w = w;
    this.h = h;
  }


  @Override
  public void mutate(IShape s) {
    if (this.ticks > 0) {
      s.changeDimensionByTick(w, h, ticks);
    }
    ticks = this.ticks - 1;
  }

  @Override
  public String officialName() {
    return "dimension";
  }
}
