package cs3500.easyanimator.model.shapes;


/**
 * Represents an instance of a triangle.
 */
public class Triangle extends AShape implements IShape {

  /**
   * To create an Triangle given a {@link Posn}, {@link Dimension}, and {@link Color}. Posn assumes
   * center of the shape. Dimension assumes width and height. Color represents color.
   *
   * @param p the position of the center of the circle the board
   * @param d the dimension of the triangle
   * @param c the color of the triangle
   */
  public Triangle(Posn p, Dimension d, Color c) {
    super(p, d, c, true);
  }

  @Override
  public String officialShapeName() {
    return "triangle";
  }
}
