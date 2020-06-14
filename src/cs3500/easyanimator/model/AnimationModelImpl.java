package cs3500.easyanimator.model;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * An instance of an animation. The time is an integer, and the Map is of objects and their
 * respective commands.
 *
 * <p>Class Invariant: 'orderByType' and 'shapeIdentifier' both must have the same keys
 */
public class AnimationModelImpl implements IAnimationModel {

  private final Map<String, ArrayList<ISynchronisedActionSet>> animationList; // name and collective animations
  private final Map<String, IShape> shapeIdentifier; // name of a shape and its reference

  /**
   * An {@link AnimationModelImpl} constructor. In the case that an animation has begun but the user
   * does not know what actions to apply yet.
   *
   * <p>Class Invariant: 'orderByType' and 'shapeIdentifier'
   * both must have the same keys for a given shape and its operations
   */
  public AnimationModelImpl() {
    shapeIdentifier = new HashMap<String, IShape>();
    animationList = new HashMap<String, ArrayList<ISynchronisedActionSet>>();
  }


  /**
   * An {@link AnimationModelImpl} constructor. In the case that an animation has begun and the user
   * has a list to apply. More for abstraction in the future than for this assignment.
   *
   * @throws IllegalArgumentException if the inputed map is null
   */
  public AnimationModelImpl(
      HashMap<String, IShape> map) {
    if (map == null) {
      throw new IllegalArgumentException("Map cannot be null.");
    }
    shapeIdentifier = map;
    animationList = new HashMap<String, ArrayList<ISynchronisedActionSet>>();

    for (String key : map.keySet()) {
      animationList.put(key, new ArrayList<ISynchronisedActionSet>());

    }
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
        // if no time discrepancy then make the animation an ISynchronisedActionSet and add it to the
        // right place in "animationList"
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
  public String getAnimationLog() {
    int logSpaceDivider = 6; // the amount of spaces between the start and end log
    // make a stringbuilder that has all of the creation keys
    StringBuilder log = new StringBuilder();
    log.append("Columns:\n");
    log.append("t x y w h r g b      t x y w h r g b");
    for (String key : animationList.keySet()) {
      // IShape shapeName = shapeIdentifier.get(key);
      log.append("\n").append("shape ").append(key).append(" ")
          .append(shapeIdentifier.get(key).officialShapeName());

      for (ISynchronisedActionSet ia : animationList.get(key)) {
        log.append("\n").append(ia.getStartTick()).append(" ")
            .append(shapeIdentifier.get(key).toString());
        log.append(" ".repeat(logSpaceDivider)); // adds the space divider
        mutateShapeAndReplace(ia, key); // changes the shape in the map to the new shape
        log.append(ia.getEndTick()).append(" ")
            .append(shapeIdentifier.get(key).toString());
      }
    }
    return log.toString();
  }

  @Override
  public String getAnimationDescription() {
    int i = 0;
    StringBuilder log = new StringBuilder();
    for (String key : shapeIdentifier.keySet()) {
      log.append("Create ").append(shapeIdentifier.get(key).officialShapeName()).append(" named ")
          .append(key).append("\n");
    }
    for (String key : shapeIdentifier.keySet()) {
      if (i > 0) {
        log.append("\n");
      }
      for (ISynchronisedActionSet ia : animationList.get(key)) {
        IShape shape = shapeIdentifier.get(key);
        String beforePosn = shape.getPosn().posnToString();
        String beforeDim = shape.getDimension().dimToString();
        String beforeColor = shape.getColor().colorToString();
        mutateShapeAndReplace(ia, key);
        String afterPosn = shape.getPosn().posnToString();
        String afterDim = shape.getDimension().dimToString();
        String afterColor = shape.getColor().colorToString();
        log.append("\nFrom time ").append(ia.getStartTick()).append(" to time ")
            .append(ia.getEndTick()).append(", ").append(key).append(" ");
        if (beforePosn.equals(afterPosn)) {
          log.append("stays put, ");
        } else {
          log.append("moves from ").append(beforePosn).append(" to ").append(afterPosn)
              .append(", ");
        }
        if (beforeDim.equals(afterDim)) {
          log.append("stays size ").append(beforeDim).append(", ");
        } else {
          log.append("changes from ").append(beforeDim).append(" to ").append(afterDim)
              .append(", ");
        }
        if (beforeColor.equals(afterColor)) {
          log.append("and stays ").append(beforeColor).append(".");
        } else {
          log.append("and turns ").append(afterColor).append(".");
        }
      }
      i++;
    }
    return log.toString();
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


  //TODO: Add tests for this method 'createShapeWithoutInstance'
  @Override
  public void createShapeWithoutInstance(String name, String type, int x, int y, int w, int h,
      int r, int g, int b) {

    IShape shape;
    Posn p = new Posn(x, y);
    Dimension d = new Dimension(w, h);
    Color c = new Color(r, g, b);

    switch (type) {
      case ("triangle"):
        shape = new Triangle(p, d, c);
        break;
      case ("rectangle"):
        shape = new Rectangle(p, d, c);
        break;
      case ("oval"):
        shape = new Oval(p, d, c);
        break;
      default:
        throw new IllegalArgumentException("Not a valid shape type.");
    }

    shapeIdentifier.put(name, shape);
    animationList.put(name, new ArrayList<>());
  }

  @Override
  public void removeShape(String key) {
    if (animationList.get(key) == null) {
      throw new IllegalArgumentException("Given shape cannot be found.");
    }
    animationList.remove(key); // removed from animation list
    shapeIdentifier.remove(key); // removes the key from the identifier
  }

  //mutates the shape so that the final state can be printed for the logs
  private void mutateShapeAndReplace(ISynchronisedActionSet ia, String key) {
    for (int i = ia.getStartTick(); i < ia.getEndTick(); i++) {
      ia.applyAnimation(shapeIdentifier.get(key));
    } // mutates the shape to the desired ISynchronisedActionSet
  }

  @Override
  public String getShapeKeys() {
    StringBuilder sb = new StringBuilder();
    sb.append("name , type\n").append("-".repeat(13)).append("\n");
    for (String s : shapeIdentifier.keySet()) {
      sb.append(s).append(" , ").append(shapeIdentifier.get(s).officialShapeName()).append("\n");
    }
    return sb.toString();
  }

  // TODO: Add tests for this method 'getShape'
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
}
