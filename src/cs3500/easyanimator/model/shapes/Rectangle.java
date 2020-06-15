package cs3500.easyanimator.model.shapes;

/**
 * Represents a rectangle shape.
 */
public class Rectangle extends AShape implements IShape {

  /**
   * To create an Rectangle given a {@link Posn}, {@link Dimension}, and {@link Color}. Posn assumes
   * center of the shape. Dimension assumes width and height. Color represents color.
   *
   * @param p the position of the center of the circle the board
   * @param d the dimension of the rectangle
   * @param c the color of the rectangle
   */
  public Rectangle(Posn p, Dimension d, Color c) {
    super(p, d, c, true);
  }

  @Override
  public String officialShapeName() {
    return "rectangle";
  }

}
