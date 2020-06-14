package cs3500.easyanimator.model.shapes;

import java.util.Objects;

/**
 * To represent a two-dimensional space. Contains fields "w" and "h" representing width and height
 * respectively.
 */
public class Dimension {

  private int w;
  int h;

  /**
   * A Dimension constructor. Requires two positive integers to represent the width and height
   * respectively.
   *
   * @param w the width
   * @param h the height
   * @throws IllegalArgumentException when the width and height are less than 1
   */
  public Dimension(int w, int h) {
    if (w < 1 || h < 1) {
      throw new IllegalArgumentException("Width and height must be positive.");
    }
    this.w = w;
    this.h = h;
  }

  /**
   * Returns the width field of Dimension.
   *
   * @return the width
   */
  public int getW() {
    return this.w;
  }

  /**
   * Returns Dimension height.
   *
   * @return the height
   */
  public int getH() {
    return this.h;
  }

  /**
   * Outputs a custom definition of the string representation of a Dimension.
   *
   * @return the string representation of a Dimension
   */
  @Override
  public String toString() {
    return this.w + " " + this.h;
  }

  /**
   * Outputs whether a Dimension is equal to a given object.
   *
   * @param o a given object to be compared.
   * @return true if the Dimension is equal, false otherwise.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (!(o instanceof Dimension)) {
      return false;
    }

    Dimension that = (Dimension) o;

    return ((Math.abs(this.w - that.w) < 0.01) && (Math.abs(this.h - that.h) < 0.01));
  }

  /**
   * Outputs the hashcode of a Dimension.
   *
   * @return the hashcode of a Dimension.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.w, this.h);
  }

  /**
   * Formatted custom string format for a dimension.
   *
   * @return a string with a formatted dim.
   */
  public String dimToString() {
    return this.getW() + "x" + this.getH();
  }
}
