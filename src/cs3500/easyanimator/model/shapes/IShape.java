package cs3500.easyanimator.model.shapes;


/**
 * To represent a two-dimensional shape. All IShapes are expected to have a {@link Color}, {@link
 * Dimension}, and {@link Posn}.
 */
public interface IShape {

  /**
   * Returns a textual log of the shape's properties. Properties are returned as integers, separated
   * by a space, in the following order: x-position, y-position, width, height, red, green, blue.
   *
   * @return the string log of the shape's properties
   */
  String toString();

  /**
   * The official name of the shape that the class represents.
   *
   * @return the name of the shape
   */
  String officialShapeName();


  /**
   * Given the desired final x and y position of the shape and the number of ticks in which that
   * final position is to be achieved. Utilizes rate of change to alter the shape's position by the
   * amount it should over a single tick. This method should be called by the given number of ticks
   * to achieve the desired final position.
   *
   * @param x     the shape's new x position
   * @param y     the shape's new y position
   * @param ticks the total amount of ticks the action should take
   */
  void changePositionByTick(int x, int y, int ticks);

  /**
   * Given the desired final w and h dimensions of the shape and the number of ticks in which that
   * final dimension is to be achieved. Utilizes rate of change to alter the shape's dimension by
   * the amount it should over a single tick. This method should be called by the given number of
   * ticks to achieve the desired final dimension.
   *
   * @param w     the shape's new width
   * @param h     the shape's new height
   * @param ticks the total amount of ticks the action should take
   */
  void changeDimensionByTick(int w, int h, int ticks);

  /**
   * Given the desired final r, g, and b color values of the shape and the number of ticks in which
   * that final color is to be achieved. Utilizes rate of change to alter the shape's color by the
   * amount it should over a single tick. This method should be called by the given number of ticks
   * to achieve the desired final color.
   *
   * @param r     the shape's new red value
   * @param g     the shape's new green value
   * @param b     the shape's new blue value
   * @param ticks the total amount of ticks the action should take
   */
  void changeColorByTick(int r, int g, int b, int ticks);

  /**
   * Changes the visibility of the shape. True represents visible and false represents invisible.
   * Leaving open for implementation until the view is connected.
   *
   * @param b the new visibility of the shape
   */
  void changeVisibility(boolean b);


  /**
   * Gets the dimension of the shape as a {@link Dimension}.
   *
   * @return the shape's dimension
   */
  Dimension getDimension();

  /**
   * Gets the shape's color as a {@link Color}.
   *
   * @return the shape's color
   */
  Color getColor();

  /**
   * Gets the shape's position as a {@link Posn}.
   *
   * @return the shape's position
   */
  Posn getPosn();

  /**
   * Return's the shape's visibility. True means that the shape is visible and false means that the
   * shape is not visible.
   *
   * @return the visibility of the shape
   */
  boolean getVisibility();


}