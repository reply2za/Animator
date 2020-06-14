package cs3500.easyanimator.model.shapes;

/**
 * To represent an oval. Implements {@link IShape}. Extends {@link AShape}.
 */
public class Oval extends AShape implements IShape {

  /**
   * To create an Oval given a {@link Posn}, {@link Dimension}, and {@link Color}. Posn assumes
   * center of the shape. Dimension assumes width and height. Color represents color.
   *
   * @param p the position of the center of the circle the board
   * @param d the dimension of the oval
   * @param c the color of the oval
   */
  public Oval(Posn p, Dimension d, Color c) {
    super(p, d, c, true);
  }

  @Override
  public String officialShapeName() {
    return "oval";
  }
}
