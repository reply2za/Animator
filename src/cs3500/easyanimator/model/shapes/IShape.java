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
   * Given x and y ints representing a position. Assigns the position to the shape and then returns
   * the altered shape per tick.
   *
   * @param x     the shape's new x position away from initial for every tick
   * @param y     the shape's new y position away from initial for every tick
   * @param ticks the total amount of ticks to change to the new x and y position.
   */
  void changePositionByTick(int x, int y, int ticks);

  /**
   * Given width and height ints representing a dimension. Assigns the given dimension to the shape
   * and then returns the altered shape.
   *
   * @param w     the shape's new width away from initial for every tick
   * @param h     the shape's new height away from initial for every tick
   * @param ticks the total amount of ticks to change to the new width and height.
   */
  void changeDimensionByTick(int w, int h, int ticks);

  /**
   * Given red, green, and blue ints representing a color. Assigns the given color to the shape and
   * then returns the altered shape.
   *
   * @param r     the shape's new red value away from initial for every tick
   * @param g     the shape's new green value away from initial for every tick
   * @param b     the shape's new blue value away from initial for every tick
   * @param ticks the total amount of ticks to change to the new width and height.
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