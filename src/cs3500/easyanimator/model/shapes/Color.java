package cs3500.easyanimator.model.shapes;

import java.util.Objects;

/**
 * To represent color. Color contains the fields "r", "g", and "b", representing "red", "blue", and
 * "green" respectively. The Color fields are represented as an integer between 0 and 255.
 */
public class Color {

  private final int r; // red amount
  private final int g; // green amount
  private final int b; // blue amount

  /**
   * A {@link Color} constructor. Requires three integers between 0 and 255.
   *
   * @param r red
   * @param g green
   * @param b blue
   */
  public Color(int r, int g, int b) {
    if (r < 0 || g < 0 || b < 0 || r > 255 || g > 255 || b > 255) {
      throw new IllegalArgumentException("Color fields must be between 0 and 255.");
    }
    this.r = r;
    this.g = g;
    this.b = b;
  }

  /**
   * Returns the red field for Color as an integer.
   *
   * @return red
   */
   int getR() {
    return this.r;
  }

  /**
   * Returns the green field for Color as an integer.
   *
   * @return green
   */
  int getG() {
    return this.g;
  }

  /**
   * Returns the blue field for Color as an integer.
   *
   * @return blue
   */
  int getB() {
    return this.b;
  }

  /**
   * Returns the string representation of a color.
   *
   * @return the string representation of a color.
   */
  @Override
  public String toString() {
    return this.r + " " + this.g + " " + this.b;
  }

  /**
   * Returns whether 2 Colors are equilvalent.
   *
   * @param o another object to be compared.
   * @return true if the Color is equal to Object o, false otherwise.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (!(o instanceof Color)) {
      return false;
    } else {
      Color that = (Color) o;
      return (Math.abs(this.r - that.r) < 0.01) && (Math.abs(this.g - that.g) < 0.01) && (
          Math.abs(this.b - that.b) < 0.01);
    }
  }

  /**
   * Outputs the hashcode of a Color.
   *
   * @return the hashcode of a Color.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.r, this.g, this.b);
  }

  /**
   * Returns a custom formatted color in string form. Based on the values of the color, prints out
   * the most accurate color description.
   *
   * @return a formatted color in string format.
   */
  public String colorToString() {
    if (b == 255 && g == 255 && r == 255) {
      return "white";
    } else if (b == 0 && g == 0 && r == 0) {
      return "black";
    } else if (b > g && b > r) {
      return "blue";
    } else if (g > b && g > r) {
      return "green";
    } else if (r > b && r > g) {
      return "red";
    } else if (r == b && g < r) {
      return "purple-ish";
    } else if (g == b && r < g) {
      return "green-ish blue-ish";
    } else if (g == b) {
      return "brown";
    } else {
      return "a neutral color";
    }
  }
}
