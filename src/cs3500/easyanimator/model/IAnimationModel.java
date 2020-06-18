package cs3500.easyanimator.model;


import cs3500.easyanimator.model.actions.IActionCommand;
import cs3500.easyanimator.model.actions.ISynchronisedActionSet;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.util.AnimationBuilder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Represents an instance of a single animation that is a collection of actions performed on shapes
 * of type {@link IShape}.
 */
public interface IAnimationModel {

  /**
   * Adds an {@link IActionCommand} to the list of operations performed on a shape for a given
   * amount of time.
   *
   * @param key   the key to access the shape and list of the shapes commands in a {@link
   *              java.util.Map}
   * @param start the start time of the given command to be performed.
   * @param end   the end time of the given command to be performed.
   * @param ac    the {@link IActionCommand} that will be performed on the shape.
   * @throws IllegalArgumentException if the given {@link IActionCommand} is null
   * @throws IllegalArgumentException if the shape to add the command to doesn't exist
   * @throws IllegalArgumentException if there is another command of the same type at the same time
   */
  void add(String key, int start, int end, IActionCommand ac);

  /**
   * Clears all {@link IShape} and {@link IActionCommand} from a {@link IAnimationModel}. This
   * resets the {@link IAnimationModel} to its original state.
   */
  void clear();

  /**
   * Removes a timeframe from a Shapes commands.
   *
   * @param key       the key that is used to erase an {@link IActionCommand} from a shapes
   *                  commands.
   * @param startTick the start time of the {@link IActionCommand} being removed.
   * @param endTick   the end time of the {@link IActionCommand} being removed.
   */
  void remove(String key, int startTick, int endTick);

  /**
   * Removes an {@link IActionCommand} from a Shapes commands.
   *
   * @param key       the key that is used to erase an {@link IActionCommand} from a shapes
   *                  commands.
   * @param startTick the start time of the {@link IActionCommand} being removed.
   * @param endTick   the end time of the {@link IActionCommand} being removed.
   * @param ac        the {@link IActionCommand} that will be removed from the list of operations.
   */
  void removeActionCommand(String key, int startTick,
      int endTick, IActionCommand ac);


  /**
   * Creates a new {@link IShape} in the model to be manipulated.
   *
   * @param name  the name of the new shape.
   * @param shape the new shape and it's properties.
   * @throws IllegalArgumentException if there already exists a shape with that name.
   */
  void createShape(String name, IShape shape);

  /**
   * Creates a new {@link IShape} in the model that can be manipulated via actions. Does not require
   * any instantiation of an {@link IShape} to be passed as an argument.
   *
   * @param name   the unique name of the shape
   * @param type   the type of shape based on specific keywords: 'triangle' - creates a new {@link
   *               cs3500.easyanimator.model.shapes.Triangle} 'ellipse' - creates a new {@link
   *               cs3500.easyanimator.model.shapes.Oval} 'rectangle' - creates a new {@link
   *               cs3500.easyanimator.model.shapes.Rectangle}
   */
  void createShapeWithoutInstance(String name, String type);


  /**
   * Removes the shape from the Map and all of its animations.
   *
   * @param key the shapes key in the Map so that it can be accessed and removed.
   * @throws IllegalArgumentException if the shape does not exist
   */
  void removeShape(String key);

  /**
   * Returns a list of the shapes names.
   *
   * @return A list of the {@link IShape} in the {@link IAnimationModel}.
   */
  ArrayList<String> getShapeKeys();

  /**
   * Returns the shape and all of its features as an {@link IShape}.
   *
   * @param key the unique name of the shape
   * @return the respective object of type IShape
   */
  IShape getShape(String key);

  /**
   * Changes the canvas dimensions to the given arguments.
   *
   * @param x      the leftmost x value
   * @param y      the topmost y value
   * @param width  the width of the canvas
   * @param height the height of the canvas
   */
  void changeCanvas(int x, int y, int width, int height);

  /**
   * Adds a transformation to the model.
   *
   * @param name The name of the shape (added with {@link AnimationBuilder#declareShape})
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
  void addMotions(String name, int t1, int x1, int y1, int w1,
      int h1, int r1, int g1, int b1, int t2, int x2, int y2, int w2, int h2, int r2, int g2,
      int b2);

  /**
   * Returns a list of ticks from a storage type that stores all of the actions of a shape as a list
   * of {@link ISynchronisedActionSet}.
   *
   * @param name the unique name of the shape
   * @return a list of integers of all of the ticks where the shape has a defined instance/frame
   */
  List<Integer> getTicks(String name);

  /**
   * Returns a hashmap of the animations that are in the model for all shapes.
   *
   * @return hashmap of the animations that are in the model for all shapes.
   */
  LinkedHashMap<String, ArrayList<ISynchronisedActionSet>> getAnimationList();

  /**
   * Returns a hashmap of all shapes an a given animation.
   *
   * @return hashmap of all of the shapes and their names.
   */
  LinkedHashMap<String, IShape> getShapeIdentifier();

  /**
   * Returns the canvas width for this model.
   *
   * @return returns the canvas width as an int.
   */
  int getCanvasWidth();

  /**
   * Returns the canvas height for this model.
   *
   * @return the canvas height as an int.
   */
  int getCanvasHeight();

  /**
   * Returns the canvas leftmost x value for this model.
   *
   * @return returns the leftmost x value of the canvas as an int.
   */
  int getCanvasX();

  /**
   * Returns the canvas topmost y value for this model.
   *
   * @return the topmost y value canvas height as an int.
   */
  int getCanvasY();
}
