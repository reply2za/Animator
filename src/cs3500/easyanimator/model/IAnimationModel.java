package cs3500.easyanimator.model;


import cs3500.easyanimator.model.actions.IActionCommand;
import cs3500.easyanimator.model.actions.ISynchronisedActionSet;
import cs3500.easyanimator.model.shapes.IShape;

/*
TODO: additions to this interface
- Add a 'removeAction' method
- Add a method that manipulates visibility
 */

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
   * Removes an {@link IActionCommand} from a Shapes commands.
   *
   * @param key       the key that is used to erase an {@link IActionCommand} from a shapes
   *                  commands.
   * @param startTick the start time of the {@link IActionCommand} being removed.
   * @param endTick   the end time of the {@link IActionCommand} being removed.
   * @param ac        the {@link IActionCommand} that will be removed from the list of operations.
   */
  void remove(String key, int startTick,
      int endTick, IActionCommand ac);

  /**
   * Given a shape of type {@link IShape} and an animation of type {@link ISynchronisedActionSet}
   * and a String representing the shape's unique name. Prints out the state of the shape before and
   * after the animation is applied. This is returned as a string in a log formation in regards to
   * the shape. Assuming that it is a part of the 'shapeIdentifier' HashMap, this method will also
   * put the mutated shape in the map.
   *
   * @return A string that prints out the full animation state for the total time.
   */
  String getAnimationLog();

  /**
   * Prints out a more detailed description of the animations in the {@link IAnimationModel} for the
   * visually impaired.
   *
   * @return A string that prints out the full animation description for the total duration.
   */
  String getAnimationDescription();

  /**
   * Creates a new {@link IShape} in the model to be manipulated.
   *
   * @param name  the name of the new shape.
   * @param shape the new shape and it's properties.
   * @throws IllegalArgumentException if there already exists a shape with that name.
   */
  void createShape(String name, IShape shape);


  // TODO: Add to README as a change to the model interface : new method
  /**
   * Creates a new {@link IShape} in the model that can be manipulated via actions. Does not require
   * any instantiation of an {@link IShape} to be passed as an argument.
   *
   * @param name   the unique name of the shape
   * @param type   the type of shape based on specific keywords: 'triangle' - creates a new {@link
   *               cs3500.easyanimator.model.shapes.Triangle} 'oval' - creates a new {@link
   *               cs3500.easyanimator.model.shapes.Oval} 'rectangle' - creates a new {@link
   *               cs3500.easyanimator.model.shapes.Rectangle}
   * @param x      the shape's x position
   * @param y      the shape's y position
   * @param width  shape's width
   * @param height shape's height
   * @param red    shape's red value
   * @param green  shape's green value
   * @param blue   shape's blue value
   */
  void createShapeWithoutInstance(String name, String type, int x, int y, int width, int height,
      int red, int green, int blue);


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
   * @return A String list of the {@link IShape} in the {@link IAnimationModel}.
   */
  String getShapeKeys();

  // TODO: Add to README as a change to the model interface : new method
  /**
   * Returns the shape and all of its features as an {@link IShape}.
   *
   * @param key the unique name of the shape
   * @return the respective object of type IShape
   */
  IShape getShape(String key);

}
