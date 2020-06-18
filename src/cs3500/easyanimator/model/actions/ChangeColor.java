package cs3500.easyanimator.model.actions;


import cs3500.easyanimator.model.shapes.IShape;

/**
 * Represents a change of color for any IShape.
 */
public class ChangeColor implements IActionCommand {

  private int ticks;
  private final int red;
  private final int blue;
  private final int green;

  /**
   * A ChangeSize constructor. Given a positive width and height, creates a dimension object and
   * assigns it to the class field.
   *
   * @param red   the desired final red color value
   * @param green the desired final green color value
   * @param blue  the desired final blue color value
   * @param ticks the number of ticks to perform the given change
   * @throws IllegalArgumentException if ticks are less than or equal to 0
   */
  public ChangeColor(int red, int green, int blue, int ticks) {
    if (ticks <= 0) {
      throw new IllegalArgumentException("Ticks cannot be less than of equal to 0.");
    }
    this.red = red;
    this.blue = blue;
    this.green = green;
    this.ticks = ticks;
  }

  @Override
  public void mutate(IShape s) {
    if (this.ticks > 0) {
      s.changeColorByTick(red, green, blue, ticks);
    }
    ticks = this.ticks - 1;
  }

  @Override
  public String officialName() {
    return "color";
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
    int[] fields = new int[3];
    fields[0] = this.red;
    fields[1] = this.green;
    fields[2] = this.blue;
    return fields;
  }
}
