package cs3500.easyanimator.controller;

import cs3500.easyanimator.model.IAnimationModel;
import cs3500.easyanimator.model.IReadOnlyModel;
import cs3500.easyanimator.model.actions.IActionCommand;
import cs3500.easyanimator.model.shapes.IShape;

/**
 * Represents all of the different actions and controls that a controller supports. As such, the
 * view can call on these specific 'features' of the controller if given an instance of this. Is the
 * controller of the program and delegates between the model and the view.
 */
public interface IControllerFeatures {

  /**
   * Ends the program.
   */
  void exitProgram();

  /**
   * Runs the animator and the overall program. Creates the views according to the type of view
   * attached to it.
   */
  void runAnimator();

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
   * @param name the unique name of the shape
   * @param type the type of shape based on specific keywords: 'triangle' - creates a new {@link
   *             cs3500.easyanimator.model.shapes.Triangle} 'ellipse' - creates a new {@link
   *             cs3500.easyanimator.model.shapes.Oval} 'rectangle' - creates a new {@link
   *             cs3500.easyanimator.model.shapes.Rectangle}
   */
  void createShapeWithoutInstance(String name, String type);

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
   * Returns the most up-to-date model of the controller.
   */
  IReadOnlyModel updateReadOnly();

  /**
   * Removes the shape from the Map and all of its animations.
   *
   * @param key the shapes key in the Map so that it can be accessed and removed.
   * @throws IllegalArgumentException if the shape does not exist
   */
  void removeShape(String key);
}



