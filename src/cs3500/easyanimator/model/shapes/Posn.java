package cs3500.easyanimator.model.shapes;

import java.util.Objects;

/**
 * To represent a position (x,y) of an object.
 */
public class Posn {

  private final int x;
  private final int y;

  /**
   * Represents a Posn constructor. Given an x and a y to represent a position.
   */
  public Posn(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Gets the x position of the Posn.
   *
   * @return the x position of the Posn
   */
  int getX() {
    return this.x;
  }

  /**
   * Returns the y position of the Posn.
   *
   * @return the y positon of the Posn
   */
  int getY() {
    return this.y;
  }

  /**
   * Outputs the custom string representation of a {@link Posn}.
   *
   * @return the string value of a posn
   */
  @Override
  public String toString() {
    return String.valueOf(x) + " " + String.valueOf(y);
  }

  /**
   * Returns a posn format for the String.
   *
   * @return a posn formatted string.
   */
  public String posnToString() {
    return "(" + this.x + "," + this.y + ")";
  }

  /**
   * Outputs a custom definition of whether or not 2 Posns are equal.
   *
   * @param a a separate object to be compared.
   * @return whether they are equal
   */
  @Override
  public boolean equals(Object a) {
    if (this == a) {
      return true;
    }
    if (!(a instanceof Posn)) {
      return false;
    }

    Posn that = (Posn) a;

    return ((Math.abs(this.x - that.x) < 0.01) && (Math.abs(this.y - that.y) < 0.01));
  }

  /**
   * The hashcode of a Posn object.
   *
   * @return the hashcode of a Posn object.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.x, this.y);
  }


}