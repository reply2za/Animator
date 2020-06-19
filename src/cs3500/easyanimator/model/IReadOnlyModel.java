package cs3500.easyanimator.model;

import cs3500.easyanimator.model.actions.IActionCommand;
import cs3500.easyanimator.model.shapes.IShape;
import java.util.Map;

/**
 * A read-only version of the model. Should not support any methods that manipulate data. Throws an
 * UnsupportedOperationException for unsupported method calls that may cause manipulation of data.
 */
public interface IReadOnlyModel extends IAnimationModel {

  /**
   * Unsupported in this read-only implementation. Will throw an {@link
   * UnsupportedOperationException} if called.
   *
   * @param key   the key to access the shape and list of the shapes commands in a {@link Map}
   * @param start the start time of the given command to be performed.
   * @param end   the end time of the given command to be performed.
   * @param ac    the {@link IActionCommand} that will be performed on the shape.
   * @throws IllegalArgumentException      if the given {@link IActionCommand} is null
   * @throws IllegalArgumentException      if the shape to add the command to doesn't exist
   * @throws IllegalArgumentException      if there is another command of the same type at the same
   *                                       time
   * @throws UnsupportedOperationException if called
   */
  @Override
  void add(String key, int start, int end, IActionCommand ac) throws UnsupportedOperationException;

  /**
   * Unsupported in this read-only implementation. Will throw an {@link
   * UnsupportedOperationException} if called.
   *
   * @throws UnsupportedOperationException if called
   */
  @Override
  void clear() throws UnsupportedOperationException;

  /**
   * Unsupported in this read-only implementation. Will throw an {@link
   * UnsupportedOperationException} if called.
   *
   * @param key       the key that is used to erase an {@link IActionCommand} from a shapes
   *                  commands.
   * @param startTick the start time of the {@link IActionCommand} being removed.
   * @param endTick   the end time of the {@link IActionCommand} being removed.
   * @param ac        the {@link IActionCommand} that will be removed from the list of operations.
   * @throws UnsupportedOperationException if called
   */

  void remove(String key, int startTick, int endTick, IActionCommand ac)
      throws UnsupportedOperationException;

  /**
   * Unsupported in this read-only implementation. Will throw an {@link
   * UnsupportedOperationException} if called.
   *
   * @param name  the name of the new shape.
   * @param shape the new shape and it's properties.
   * @throws IllegalArgumentException      if there already exists a shape with that name.
   * @throws UnsupportedOperationException if called
   */
  @Override
  void createShape(String name, IShape shape) throws UnsupportedOperationException;

  /**
   * Unsupported in this read-only implementation. Will throw an {@link
   * UnsupportedOperationException} if called.
   *
   * @param name the unique name of the shape
   * @param type the type of shape based on specific keywords: 'triangle' - creates a new 'ellipse'
   *             - creates a new 'rectangle' - creates a new rectangle
   * @throws UnsupportedOperationException if called
   */
  @Override
  void createShapeWithoutInstance(String name, String type) throws UnsupportedOperationException;

  /**
   * Unsupported in this read-only implementation. Will throw an {@link
   * UnsupportedOperationException} if called.
   *
   * @param key the shapes key in the Map so that it can be accessed and removed.
   * @throws IllegalArgumentException if the shape does not exist
   */
  @Override
  void removeShape(String key) throws UnsupportedOperationException;

  /**
   * Unsupported in this read-only implementation. Will throw an {@link
   * UnsupportedOperationException} if called.
   *
   * @param x      the leftmost x value
   * @param y      the topmost y value
   * @param width  the width of the canvas
   * @param height the height of the canvas
   * @throws UnsupportedOperationException if called
   */
  @Override
  void changeCanvas(int x, int y, int width, int height) throws UnsupportedOperationException;

  /**
   * Unsupported in this read-only implementation. Will throw an {@link
   * UnsupportedOperationException} if called.
   *
   * @param name The name of the shape
   * @param t1   The start time of this transformation
   * @param x1   The initial x-position of the shape
   * @param y1   The initial y-position of the shape
   * @param w1   The initial width of the shape
   * @param h1   The initial height of the shape
   * @param r1   The initial red color-value of the shape
   * @param g1   The initial green color-value of the shape
   * @param b1   The initial blue color-value of the shape
   * @param t2   The end time of this transformation
   * @param x2   The final x-position of the shape
   * @param y2   The final y-position of the shape
   * @param w2   The final width of the shape
   * @param h2   The final height of the shape
   * @param r2   The final red color-value of the shape
   * @param g2   The final green color-value of the shape
   * @param b2   The final blue color-value of the shape
   */
  @Override
  void addMotions(String name, int t1, int x1, int y1, int w1, int h1, int r1, int g1, int b1,
      int t2, int x2, int y2, int w2, int h2, int r2, int g2, int b2)
      throws UnsupportedOperationException;
}
