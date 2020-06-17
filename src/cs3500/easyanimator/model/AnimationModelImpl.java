package cs3500.easyanimator.model;

import cs3500.easyanimator.model.actions.ChangeColor;
import cs3500.easyanimator.model.actions.ChangeDimension;
import cs3500.easyanimator.model.actions.ChangePosition;
import cs3500.easyanimator.model.actions.IActionCommand;
import cs3500.easyanimator.model.actions.ISynchronisedActionSet;
import cs3500.easyanimator.model.actions.SynchronizedActionSetImpl;
import cs3500.easyanimator.model.shapes.Color;
import cs3500.easyanimator.model.shapes.Dimension;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.model.shapes.Oval;
import cs3500.easyanimator.model.shapes.Posn;
import cs3500.easyanimator.model.shapes.Rectangle;
import cs3500.easyanimator.model.shapes.Triangle;
import cs3500.easyanimator.util.AnimationBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * An instance of an animation. The time is an integer, and the Map is of objects and their
 * respective commands.
 *
 * <p>Class Invariant: 'orderByType' and 'shapeIdentifier' both must have the same keys
 */
public class AnimationModelImpl implements IAnimationModel {

  private final LinkedHashMap<String, ArrayList<ISynchronisedActionSet>> animationList; // name
  // and collective animations
  private final LinkedHashMap<String, IShape> shapeIdentifier; // name of a shape and its reference

  private int canvasWidth = 500;
  private int canvasHeight = 500;
  private int canvasX = 0;
  private int canvasY = 0;

  /**
   * An {@link AnimationModelImpl} constructor. In the case that an animation has begun but the user
   * does not know what actions to apply yet.
   *
   * <p>Class Invariant: 'orderByType' and 'shapeIdentifier'
   * both must have the same keys for a given shape and its operations
   */
  public AnimationModelImpl() {
    shapeIdentifier = new LinkedHashMap<>();
    animationList = new LinkedHashMap<>();
  }

  @Override
  public void add(String key, int start, int end, IActionCommand ac) {
    if (animationList.get(key) == null) {
      throw new IllegalArgumentException("Shape '" + key + "' cannot be found.");
    }
    if (ac == null) {
      throw new IllegalArgumentException(
          "Given Animation Command cannot be null. If this is a shape creation, "
              + "use the 'createShape' method.");
    }
    ArrayList<ISynchronisedActionSet> unsortedlist = animationList.get(key);
    ArrayList<ISynchronisedActionSet> problemlist = new ArrayList<>();
    ArrayList<ISynchronisedActionSet> nonproblemlist = new ArrayList<>();
    for (ISynchronisedActionSet ia : unsortedlist) {
      if (timeConflict(start, end, ia.getStartTick(), ia.getEndTick())) {
        if (ia.containsInstanceOf(ac)) { //containsSameType compares command types
          throw new
              IllegalArgumentException("Cannot have two of the same "
              + "commands overlapping in ticks");
        } else {
          problemlist.add(ia);
        }
      } else {
        nonproblemlist.add(ia);
        // if no time discrepancy then make the animation an
        // ISynchronisedActionSet and add it to the right place in "animationList"
      }
    }
    //means that there are no conflicts
    if (nonproblemlist.size() == unsortedlist.size()) {
      ArrayList<IActionCommand> myAC = new ArrayList<>();
      myAC.add(ac);
      nonproblemlist.add(new SynchronizedActionSetImpl(start, end, myAC));
    }
    // if there were conflicts
    else {
      ArrayList<ISynchronisedActionSet> fixedArray = fixOverlap(problemlist, start, end, ac);
      nonproblemlist.addAll(fixedArray);
    }
    //need to sort the list
    Collections.sort(nonproblemlist);
    //replace the formatted list of animation list
    animationList.put(key, nonproblemlist);
  }

  //Return true if two different AnimationCommands have a time overlap
  private boolean timeConflict(int start1, int end1, int start2, int end2) {
    if (end1 < end2 && end1 > start2) {
      return true;
    } else if (start1 < end2 && start1 > start2) {
      return true;
    } else if (start2 > start1 && start2 < end1) {
      return true;
    } else if (end2 < end1 && end2 > start1) {
      return true;
    } else {
      return (end1 == end2 && start1 == start2);
    }
  }

  @Override
  public void clear() {
    animationList.clear();
    shapeIdentifier.clear();
  }

  @Override
  public void remove(String key, int startTick,
      int endTick, IActionCommand ac) {
    if (animationList.get(key) == null) {
      throw new IllegalArgumentException("Shape '" + key + "' cannot be found.");
    }
    for (ISynchronisedActionSet ia : animationList.get(key)) {
      if (startTick == ia.getStartTick() && endTick == ia.getEndTick()) {
        int index = 0;
        for (IActionCommand aniCommand : ia.getCommandList()) {
          if (ac.equals(aniCommand)) {
            ia.removeCommand(index);
          }
          index++;
        }
      }
    }
  }

  @Override
  public void createShape(String name, IShape shape) {
    if (shapeIdentifier.get(name) != null) {
      throw new IllegalArgumentException(
          "There already exists a shape called " + name + ". Please"
              + "enter a new name that has not been used or delete your other shape first.");
    } else {
      shapeIdentifier.put(name, shape);
      animationList.put(name, new ArrayList<>());
    }
  }

  @Override
  public void createShapeWithoutInstance(String name, String type) {

    IShape shape;
    Posn p = null;
    Dimension d = null;
    Color c = null;

    switch (type) {
      case ("triangle"):
        shape = new Triangle(p, d, c);
        break;
      case ("rectangle"):
        shape = new Rectangle(p, d, c);
        break;
      case ("ellipse"):
        shape = new Oval(p, d, c);
        break;
      default:
        throw new IllegalArgumentException("Not a valid shape type.");
    }
    createShape(name, shape);
  }

  @Override
  public void removeShape(String key) {
    if (animationList.get(key) == null) {
      throw new IllegalArgumentException("Given shape cannot be found.");
    }
    animationList.remove(key); // removed from animation list
    shapeIdentifier.remove(key); // removes the key from the identifier
  }

  @Override
  public ArrayList<String> getShapeKeys() {
    return new ArrayList<>(shapeIdentifier.keySet());
  }

  /**
   * Returns the shape and all of its features as an {@link IShape}.
   *
   * @param key the unique name of the shape
   * @return the respective object of type IShape
   */
  @Override
  public IShape getShape(String key) {
    return shapeIdentifier.get(key);
  }

  // Fixes overlapping IAnimations and reformats/combines them to be more efficient and cover all
  // possible changes for a given amount of time
  private ArrayList<ISynchronisedActionSet> fixOverlap(
      ArrayList<ISynchronisedActionSet> overlappingAnimations,
      int start, int end, IActionCommand ac) {
    ArrayList<ISynchronisedActionSet> sortedAnimations = new ArrayList<>();
    ArrayList<Integer> listOfTicks = new ArrayList<>();
    listOfTicks.add(start);
    listOfTicks.add(end);
    int temp;
    // gets a list of the new start and end times for the new formatted/combines animations
    for (ISynchronisedActionSet ia : overlappingAnimations) {
      temp = ia.getStartTick();
      if (!(listOfTicks.contains(temp))) {
        listOfTicks.add(temp);
      }
      temp = ia.getEndTick();
      if (!(listOfTicks.contains(temp))) {
        listOfTicks.add(temp);
      }
    }
    ArrayList<IActionCommand> tempAC;
    ArrayList<IActionCommand> newAnimationCommandList = new ArrayList<>();
    //sorts them
    Collections.sort(listOfTicks);
    for (int i = 0; i < listOfTicks.size() - 1; i++) {
      //go through all animation and see if it is within these ticks
      //if so, add the AC's to new command list
      // runs for all of the AnimationCommands in the ISynchronisedActionSet
      for (ISynchronisedActionSet currentAnimation : overlappingAnimations) {
        tempAC = currentAnimation.getCommandList(); // creates a new AC
        if (timeConflict(listOfTicks.get(i),
            listOfTicks.get(i + 1),
            currentAnimation.getStartTick(),
            currentAnimation.getEndTick())) {
          newAnimationCommandList.addAll(tempAC); // adds the AC to the command list
        }
      }
      if (timeConflict(start, end, listOfTicks.get(i), listOfTicks.get(i + 1))) {
        newAnimationCommandList.add(ac); // adds the animation command
      }
      ISynchronisedActionSet myNewAnimation = new SynchronizedActionSetImpl(listOfTicks.get(i),
          listOfTicks.get(i + 1),
          newAnimationCommandList);
      sortedAnimations.add(myNewAnimation);
      newAnimationCommandList.clear();
    }
    return sortedAnimations;
  }

  /**
   * Changes the canvas that is being established for the view.
   *
   * @param x      the leftmost x value
   * @param y      the topmost y value
   * @param width  the width of the canvas
   * @param height the height of the canvas
   */
  public void changeCanvas(int x, int y, int width, int height) {
    this.canvasX = x;
    this.canvasY = y;
    this.canvasWidth = width;
    this.canvasHeight = height;
  }

  @Override
  public void addMotions(String name, int t1, int x1, int y1, int w1,
      int h1, int r1, int g1, int b1, int t2, int x2, int y2, int w2, int h2, int r2, int g2,
      int b2) {
    IShape oldShape = shapeIdentifier.get(name);
    IShape newShape;
    if (x1 != x2 || y1 != y2) {
      this.add(name, t1, t2, new ChangePosition(x2, y2, t2 - t1));
    }
    if (w1 != w2 || h1 != h2) {
      this.add(name, t1, t2, new ChangeDimension(x2, y2, t2 - t1));
    }
    if (r1 != r2 || g1 != g2 || b1 != b2) {
      this.add(name, t1, t2, new ChangeColor(r2, g2, b2, t2 - t1));
    }
    // if any of the values are null then overwrite all the values with the given initials
    if (oldShape.getColor() == null
        || oldShape.getPosn() == null
        || oldShape.getDimension() == null) {
      String shapeType = oldShape.officialShapeName();
      if (shapeType.equalsIgnoreCase("Rectangle")) {
        newShape = new Rectangle(new Posn(x1, y1), new Dimension(w1, h1), new Color(r1, g1, b2));
      } else if (shapeType.equalsIgnoreCase("Oval")) {
        newShape = new Oval(new Posn(x1, y1), new Dimension(w1, h1), new Color(r1, g1, b1));
      } else if (shapeType.equalsIgnoreCase("Triangle")) {
        newShape = new Triangle(new Posn(x1, y1), new Dimension(w1, h1), new Color(r1, g1, b1));
      } else {
        throw new IllegalArgumentException("Unexpected shape name given.");
      }
      shapeIdentifier.put(name, newShape);
    }
  }

  /**
   * Returns a list of ticks from a storage type that stores all of the actions of a shape as a list
   * of {@link ISynchronisedActionSet}.
   *
   * @param name the unique name of the shape
   * @return a list of integers of all of the ticks where the shape has a defined instance/frame
   */
  @Override
  public List<Integer> getTicks(String name) {
    ArrayList<ISynchronisedActionSet> isas = animationList.get(name);
    ArrayList<Integer> listOfTicks = new ArrayList<>();

    for (ISynchronisedActionSet actionSet : isas) {
      listOfTicks.add(actionSet.getEndTick());
    }

    return listOfTicks;
  }

  /**
   * Returns a hashmap of the animations that are in the model for all shapes.
   *
   * @return hashmap of the animations that are in the model for all shapes.
   */
  @Override
  public LinkedHashMap<String, ArrayList<ISynchronisedActionSet>> getAnimationList() {
    return animationList;
  }

  /**
   * Returns a hashmap of all shapes an a given animation.
   *
   * @return hashmap of all of the shapes and their names.
   */
  @Override
  public LinkedHashMap<String, IShape> getShapeIdentifier() {
    return shapeIdentifier;
  }

  @Override
  public int getCanvasWidth() {
    return canvasWidth;
  }

  @Override
  public int getCanvasHeight() {
    return canvasHeight;
  }

  /**
   * Returns the canvas leftmost x value for this model.
   *
   * @return returns the leftmost x value of the canvas as an int.
   */
  @Override
  public int getCanvasX() {
    return this.canvasX;
  }

  /**
   * Returns the canvas topmost y value for this model.
   *
   * @return the topmost y value canvas height as an int.
   */
  @Override
  public int getCanvasY() {
    return this.canvasY;
  }

  /**
   * Represents the builder class for an IAnimationModelImpl. Helps to parse through a file and
   * create a model based on the data.
   */
  public static final class Builder implements AnimationBuilder<IAnimationModel> {

    IAnimationModel model;

    /**
     * A constrcutor for a builder that has a singular model to construct.
     */
    public Builder() {
      this.model = new AnimationModelImpl();
    }

    /**
     * Constructs a final document.
     *
     * @return the newly constructed document
     */
    @Override
    public IAnimationModel build() {
      return model;
    }

    /**
     * Specify the bounding box to be used for the animation.
     *
     * @param x      The leftmost x value
     * @param y      The topmost y value
     * @param width  The width of the bounding box
     * @param height The height of the bounding box
     * @return This {@link AnimationBuilder}
     */
    @Override
    public AnimationBuilder<IAnimationModel> setBounds(int x, int y, int width, int height) {
      model.changeCanvas(x, y, width, height);
      return this;
    }

    /**
     * Adds a new shape to the growing document.
     *
     * @param name The unique name of the shape to be added. No shape with this name should already
     *             exist.
     * @param type The type of shape (e.g. "ellipse", "rectangle") to be added. The set of supported
     *             shapes is unspecified, but should include "ellipse" and "rectangle" as a
     *             minimum.
     * @return This {@link AnimationBuilder}
     */
    @Override
    public AnimationBuilder<IAnimationModel> declareShape(String name, String type) {

      switch (type) {
        case ("ellipse"):
          model.createShapeWithoutInstance(name, "ellipse");
          break;
        case ("rectangle"):
          model.createShapeWithoutInstance(name, "rectangle");
          break;
        case ("triangle"):
          model.createShapeWithoutInstance(name, "triangle");
          break;
        default:
          throw new IllegalArgumentException("Not a valid shape type.");
      }
      return this;
    }

    /**
     * Adds a transformation to the growing document.
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
     * @return This {@link AnimationBuilder}
     */
    @Override
    public AnimationBuilder<IAnimationModel> addMotion(String name, int t1, int x1, int y1, int w1,
        int h1, int r1, int g1, int b1, int t2, int x2, int y2, int w2, int h2, int r2, int g2,
        int b2) {

      // tick 0 youre 0 0 0 0 sec 5 youre 5, 5 sec 5 you wanna go down
      // addMotion is called
      // tick 6 youre 1 1 11 1 1 , tick 10 you're 10 10 10
      // 11 -> continues with whats in the model action list
      // if shape's fields are all 0 then we need to change them to match
      model.addMotions(name, t1, x1, y1, w1, h1, r1, g1, b1,
          t2, x2, y2, w2, h2, r2, g2, b2);
      return this;
    }

    /**
     * Adds an individual keyframe to the growing document.
     *
     * @param name The name of the shape (added with {@link AnimationBuilder#declareShape})
     * @param t    The time for this keyframe
     * @param x    The x-position of the shape
     * @param y    The y-position of the shape
     * @param w    The width of the shape
     * @param h    The height of the shape
     * @param r    The red color-value of the shape
     * @param g    The green color-value of the shape
     * @param b    The blue color-value of the shape
     * @return This {@link AnimationBuilder}
     */
    @Override
    public AnimationBuilder<IAnimationModel> addKeyframe(String name, int t, int x, int y, int w,
        int h, int r, int g, int b) {
      List<Integer> listOfTicks = model.getTicks(name);
      if ((listOfTicks.get(listOfTicks.size() - 1) < t)) {
        model.addMotions(name, listOfTicks.get(listOfTicks.size() - 1), x + 1, y + 1, w + 1,
            h + 1, r + 1, g + 1, b + 1, t, x, y, w, h, r, g, b);
      } else {
        for (int i = 0; i < listOfTicks.size() - 1; i++) {
          if (listOfTicks.get(i) == t) {
            throw new IllegalArgumentException("Cant have the state conflict.");
          } else if (listOfTicks.get(i) < t && listOfTicks.get(i + 1) > t) {
            model.addMotions(name, listOfTicks.get(i), x + 1, y + 1, w + 1,
                h + 1, r + 1, g + 1, b + 1, t, x, y, w, h, r, g, b);
          }
        }
      }
      return this;
    }
  }
}
