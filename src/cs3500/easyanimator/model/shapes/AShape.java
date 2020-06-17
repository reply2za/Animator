package cs3500.easyanimator.model.shapes;


/**
 * Represents the abstracted shape. This allows us to add any shape with the same qualities as well
 * as have methods to assign to it. It is assumed that all shapes have these qualities, and since
 * each field is a class, their definitions can be extended as well to represents more complex
 * shapes in the future.
 */
abstract class AShape implements IShape {

  protected Posn p;
  protected Dimension d;
  protected Color c;
  protected boolean visibleStatus;


  /**
   * A standard {@link AShape} constructor. Assigns the three three customizable fields to the
   * shape. That is a {@link Posn} representing Position, {@link Dimension} representing dimension,
   * and {@link Color} representing color.
   *
   */
  AShape(Posn p, Dimension d, Color c, boolean v) {

    this.p = p;
    this.d = d;
    this.c = c;
    visibleStatus = v;
  }

  @Override
  public String toString() {
    return this.p.toString() + " " + this.d.toString() + " " + this.c.toString();
  }

  @Override
  public void changePositionByTick(int x, int y, int ticks) {
    this.p = new Posn(this.p.getX() + (x - this.p.getX()) / ticks,
        this.p.getY() + (y - this.p.getY()) / ticks);
  }

  @Override
  public void changeDimensionByTick(int w, int h, int ticks) {
    this.d = new Dimension(d.getW() + (w - d.getW()) / ticks,
        d.getH() + (h - d.getH()) / ticks);
  }

  @Override
  public void changeColorByTick(int r, int g, int b, int ticks) {
    this.c = new Color(c.getR() + (r - c.getR()) / ticks, c.getG() + (g - c.getG()) / ticks,
        c.getB() + (b - c.getB()) / ticks);
  }

  @Override
  public void changeVisibility(boolean b) {
    visibleStatus = b;
  }

  @Override
  public Dimension getDimension() {
    return d;
  }

  @Override
  public Color getColor() {
    return c;
  }

  @Override
  public Posn getPosn() {
    return p;
  }

  @Override
  public boolean getVisibility() {
    return visibleStatus;
  }


}