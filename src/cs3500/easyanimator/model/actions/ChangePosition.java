package cs3500.easyanimator.model.actions;


import cs3500.easyanimator.model.shapes.IShape;

/**
 * Represents a new changer of location command to be applied to any IShape.
 */
public class ChangePosition implements IActionCommand {

  private int ticks;
  private final int x;
  private final int y;

  /**
   * Creates a new ChangePosition object to be applied to an {@link IShape}.
   *
   * @param x     final x
   * @param y     final y
   * @param ticks total ticks for the rate
   * @throws IllegalArgumentException if ticks are less than or equal to 0
   */
  public ChangePosition(int x, int y, int ticks) {
    if (ticks <= 0) {
      throw new IllegalArgumentException("Ticks cannot be less than of equal to 0.");
    }
    this.ticks = ticks;
    this.x = x;
    this.y = y;
  }


  @Override
  public void mutate(IShape s) {
    if (this.ticks > 0) {
      s.changePositionByTick(x, y, ticks);
    }
    ticks = this.ticks - 1;
  }

  @Override
  public String officialName() {
    return "position";
  }

  /**
   * Returns the amount of ticks that are left for the action command.
   *
   * @return the number of ticks left
   */
  @Override
  public int getTicksLeft() {
    return this.ticks;
  }

  /**
   * Gets the field values as an array. Each array index for each field. Fields differ depending on
   * the implementation. Order of the fields are placed depending on the implementation of the
   * constructor.
   *
   * @return the values of the fields within a string
   */
  @Override
  public int[] getFieldValues() {
    int[] fields = new int[2];
    fields[0] = this.x;
    fields[1] = this.y;
    return fields;
  }
}
