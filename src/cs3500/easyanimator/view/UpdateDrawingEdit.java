package cs3500.easyanimator.view;

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
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * This is the class that is in responsible for drawing and updating our visual view. It uses a
 * timer to set the speed and make repetitive changes.
 */
public class UpdateDrawingEdit extends JPanel implements ActionListener {

  LinkedHashMap<String, IShape> shapeIdentifier;
  LinkedHashMap<String, ArrayList<ISynchronisedActionSet>> animationList;
  LinkedHashMap<String, IShape> copyShapeIdentifier;
  LinkedHashMap<String, ArrayList<ISynchronisedActionSet>> copyAnimationList;
  int secondsPerTick;
  int ticks;
  boolean playing = false;
  boolean looping = false;
  ArrayList<ISynchronisedActionSet> listOfActions;
  int biggestTick;
  Timer timer;

  /**
   * This is the constructor for the class that handles updating the view as the time passes.
   *
   * @param animationList   The list of animations handled.
   * @param shapeIdentifier The shapes to be drawn.
   * @param secondsPerTick  The speed in which the animation runs.
   * @throws IllegalArgumentException if the given speed (secondsPerTick) is non-positive.
   */
  UpdateDrawingEdit(LinkedHashMap<String, ArrayList<ISynchronisedActionSet>> animationList,
      LinkedHashMap<String, IShape> shapeIdentifier, int secondsPerTick) {
    if (secondsPerTick <= 0) {
      throw new IllegalArgumentException("Speed must be a positive integer.");
    }
    this.secondsPerTick = secondsPerTick;
    this.shapeIdentifier = shapeIdentifier;
    this.animationList = animationList;
    this.copyShapeIdentifier = copyShapeList();
    this.copyAnimationList = copyAnimationList();
    this.ticks = 0;

    this.biggestTick = 0;
    for (String key : animationList.keySet()) {
      ArrayList<ISynchronisedActionSet> tempList = animationList.get(key);
      if (tempList.size() > 0) {
        int test = tempList.get(tempList.size() - 1).getEndTick();
        if (test > biggestTick) {
          this.biggestTick = test;
        }
      }
    }
  }


  /**
   * An overridden paint method so that we are able to draw all shapes that need to be drawn.
   *
   * @param g The graphics that we are using to draw the shapes on.
   */
  @Override
  public void paint(Graphics g) {
    super.paintComponents(g);
    // paint all shapes
    for (String key : copyShapeIdentifier.keySet()) {
      IShape currentShape = copyShapeIdentifier.get(key);
      // Get all of the fields to easily call later
      Posn currentPosn = currentShape.getPosn();
      Dimension currentDimension = currentShape.getDimension();
      Color currentColor = currentShape.getColor();
      java.awt.Color awtColor = new java.awt.Color(currentColor.getR(), currentColor.getG(),
          currentColor.getB());
      switch (currentShape.officialShapeName()) {
        case ("rectangle"):
          g.fillRect(currentPosn.getX(), currentPosn.getY(),
              currentDimension.getW(), currentDimension.getH());
          g.setColor(awtColor);
          break;
        case ("oval"):
          g.fillOval(currentPosn.getX(), currentPosn.getY(),
              currentDimension.getW(), currentDimension.getH());
          g.setColor(awtColor);
          break;
        default:
          throw new IllegalArgumentException("Not a valid shape.");
      }
    }
    this.timer = new Timer(1000 / secondsPerTick, this);
    this.timer.setInitialDelay(0);
    ticks++;
    this.timer.start();
  }

  // All animation lists
  // each animation time 1 to time 2 in each animation list

  /**
   * The action listener for the class. This is called every time the timer moves one tick
   * duration.
   *
   * @param e The action event listener for the timer.
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    if (playing) {
      for (String name : copyAnimationList.keySet()) {
        listOfActions = copyAnimationList.get(name);
        IShape currentShape = copyShapeIdentifier.get(name);
        for (ISynchronisedActionSet ai : listOfActions) {
          if (ticks > ai.getEndTick()) {
            listOfActions.remove(0);
            break;
          }
          if (ticks >= ai.getStartTick()) {
            ai.applyAnimation(currentShape);
            break;
          }
        }
      }
      repaint();
    }
    if (biggestTick <= ticks && looping) {
      resetFields();
    }
  }

  public void startPlaying() {
    this.playing = true;
  }

  public void pause() {
    this.playing = false;
  }

  public void setTicks(int ticks) {
    this.ticks = ticks;
  }

  public void resetFields() {
    this.playing = false;
    this.copyAnimationList.clear();
    this.copyShapeIdentifier.clear();
    this.copyAnimationList.putAll(copyAnimationList());
    this.copyShapeIdentifier.putAll(copyShapeList());
    this.ticks = 0;
    this.playing = true;
  }

  public int getTicks() {
    return this.ticks;
  }

  public void setLooping(boolean l) {
    this.looping = l;
  }

  private LinkedHashMap<String, ArrayList<ISynchronisedActionSet>> copyAnimationList() {
    LinkedHashMap<String, ArrayList<ISynchronisedActionSet>> newList = new LinkedHashMap<>();
    LinkedHashMap<String, ArrayList<ISynchronisedActionSet>> oldList = animationList;
    for (String key : oldList.keySet()) {
      ArrayList<ISynchronisedActionSet> isal = oldList.get(key);
      ArrayList<ISynchronisedActionSet> isalNew = new ArrayList<>();
      for (ISynchronisedActionSet isa : isal) {
        ArrayList<IActionCommand> oldCommandList = isa.getCommandList();
        ArrayList<IActionCommand> newCommandList = new ArrayList<>();
        for (IActionCommand ac : oldCommandList) {
          int[] fields = ac.getFieldValues();
          switch (ac.officialName()) {
            case ("color"):
              newCommandList
                  .add(new ChangeColor(fields[0], fields[1], fields[2], ac.getTicksLeft()));
              break;
            case ("position"):
              newCommandList.add(new ChangePosition(fields[0], fields[1], ac.getTicksLeft()));
              break;
            case ("dimension"):
              newCommandList.add(new ChangeDimension(fields[0], fields[1], ac.getTicksLeft()));
              break;
          }
        }
        isalNew.add(new SynchronizedActionSetImpl(isa.getStartTick(), isa.getEndTick(),
            newCommandList));

      }
      newList.put(key, isalNew);
    }
    return newList;
  }

  private LinkedHashMap<String, IShape> copyShapeList() {
    LinkedHashMap<String, IShape> newList = new LinkedHashMap<>();
    LinkedHashMap<String, IShape> oldList = shapeIdentifier;
    for (String key : oldList.keySet()) {
      IShape oldShape = oldList.get(key);
      Color newColor = new Color(oldShape.getColor().getR(), oldShape.getColor().getG(),
          oldShape.getColor().getB());
      Posn newPosn = new Posn(oldShape.getPosn().getX(), oldShape.getPosn().getY());
      Dimension newDimension = new Dimension(oldShape.getDimension().getW(),
          oldShape.getDimension().getH());
      if (oldList.get(key).officialShapeName().equalsIgnoreCase("oval")) {
        newList.put(key, new Oval(newPosn, newDimension, newColor));
      } else if (oldList.get(key).officialShapeName().equalsIgnoreCase("rectangle")) {
        newList.put(key, new Rectangle(newPosn, newDimension, newColor));
      }
    }
    return newList;
  }
}