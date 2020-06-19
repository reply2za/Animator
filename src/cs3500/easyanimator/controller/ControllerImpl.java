package cs3500.easyanimator.controller;

import cs3500.easyanimator.model.IAnimationModel;
import cs3500.easyanimator.model.IReadOnlyModel;
import cs3500.easyanimator.model.ReadOnlyModelImpl;
import cs3500.easyanimator.model.actions.IActionCommand;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.view.IView;
import java.util.Map;

/**
 * This represents an instance of our controller. It handles both the view and model to delegate to
 * their respective operations. Constructor takes in both a model of type {@link IAnimationModel}
 * and view of type {@link IView}.
 */
public class ControllerImpl implements IControllerFeatures {

  private final IAnimationModel model;
  private final IView view;

  /**
   * A constructor for our controller. Takes in an animation model of {@link IAnimationModel} and a
   * view of type {@link IView}.
   *
   * @param m The model that will be handled.
   * @param v The view type to output.
   */
  public ControllerImpl(IAnimationModel m, IView v) {
    this.model = m;
    this.view = v;
    //provide view with all the callbacks
    v.addControllerFeatures(this);
  }

  @Override
  public void exitProgram() {
    System.exit(0);
  }


  @Override
  public void runAnimator() {
    view.showView();
  }

  /**
   * Creates a new {@link IShape} in the model to be manipulated.
   *
   * @param name  the name of the new shape.
   * @param shape the new shape and it's properties.
   * @throws IllegalArgumentException if there already exists a shape with that name.
   */
  @Override
  public void createShape(String name, IShape shape) {
    model.createShape(name, shape);
  }

  /**
   * Creates a new {@link IShape} in the model that can be manipulated via actions. Does not require
   * any instantiation of an {@link IShape} to be passed as an argument.
   *
   * @param name the unique name of the shape
   * @param type the type of shape based on specific keywords: 'triangle' - creates a new 'ellipse'
   *             - creates a new 'rectangle' - creates a new rectangle
   */
  @Override
  public void createShapeWithoutInstance(String name, String type) {
    model.createShapeWithoutInstance(name, type);
  }

  /**
   * Adds an {@link IActionCommand} to the list of operations performed on a shape for a given
   * amount of time.
   *
   * @param key   the key to access the shape and list of the shapes commands in a {@link Map}
   * @param start the start time of the given command to be performed.
   * @param end   the end time of the given command to be performed.
   * @param ac    the {@link IActionCommand} that will be performed on the shape.
   * @throws IllegalArgumentException if the given {@link IActionCommand} is null
   * @throws IllegalArgumentException if the shape to add the command to doesn't exist
   * @throws IllegalArgumentException if there is another command of the same type at the same time
   */
  @Override
  public void add(String key, int start, int end, IActionCommand ac) {
    model.add(key, start, end, ac);
  }

  /**
   * Clears all {@link IShape} and {@link IActionCommand} from a {@link IAnimationModel}. This
   * resets the {@link IAnimationModel} to its original state.
   */
  @Override
  public void clear() {
    model.clear();
  }

  /**
   * Removes a timeframe from a Shapes commands.
   *
   * @param key       the key that is used to erase an {@link IActionCommand} from a shapes
   *                  commands.
   * @param startTick the start time of the {@link IActionCommand} being removed.
   * @param endTick   the end time of the {@link IActionCommand} being removed.
   */
  @Override
  public void remove(String key, int startTick, int endTick) {
    model.remove(key, startTick, endTick);
  }

  /**
   * Returns the most up-to-date model of the controller.
   */
  @Override
  public IReadOnlyModel updateReadOnly() {
    return new ReadOnlyModelImpl(model);
  }

  @Override
  public void removeShape(String key) {
    model.removeShape(key);
  }


}
