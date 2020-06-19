package cs3500.easyanimator.view;

import cs3500.easyanimator.model.IReadOnlyModel;
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
public class UpdateDrawingEdit extends JPanel implements ActionListener, IGraphicsEdit {

  LinkedHashMap<String, IShape> shapeIdentifier;
  LinkedHashMap<String, ArrayList<ISynchronisedActionSet>> animationList;
  LinkedHashMap<String, IShape> copyShapeIdentifier;
  LinkedHashMap<String, ArrayList<ISynchronisedActionSet>> copyAnimationList;
  int secondsPerTick;
  private int ticks;
  private boolean playing = false;
  private boolean looping = false;
  ArrayList<ISynchronisedActionSet> listOfActions;
  private int biggestTick;
  private int timeDelay;
  private int initialDelay;

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
    timeDelay = 1000 / secondsPerTick;
    initialDelay = 10 / secondsPerTick;
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
      if (currentShape.getPosn() != null) {
        Posn currentPosn = currentShape.getPosn();
        Dimension currentDimension = currentShape.getDimension();
        Color currentColor = currentShape.getColor();
        java.awt.Color awtColor = new java.awt.Color(currentColor.getR(), currentColor.getG(),
            currentColor.getB());
        switch (currentShape.officialShapeName()) {
          case ("rectangle"):
            g.setColor(awtColor);
            g.fillRect(currentPosn.getX(), currentPosn.getY(),
                currentDimension.getW(), currentDimension.getH());
            break;
          case ("oval"):
            g.setColor(awtColor);
            g.fillOval(currentPosn.getX(), currentPosn.getY(),
                currentDimension.getW(), currentDimension.getH());
            break;
          default:
            throw new IllegalArgumentException("Not a valid shape.");
        }
      }
    }
    Timer timer = new Timer(timeDelay, this);
    timer.setInitialDelay(initialDelay);
    ticks++;
    timer.start();
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
    if (looping && ticks >= biggestTick) {
      resetFields();
      this.playing = true;
    }
  }

  /**
   * Changes the speed of the animation based on the input from the slider.
   *
   * @param secondsPerTick the speed of the animation based on the milliseconds of the Timer.
   */
  public void changeSpeed(int secondsPerTick) {
    timeDelay = 1000 / secondsPerTick;
    initialDelay = 10 / secondsPerTick;
  }

  /**
   * Starts playing the animation by setting the play boolean to true.
   */
  public void startPlaying() {
    this.playing = true;
  }

  /**
   * Pauses the animation by changing the play boolean to false.
   */
  public void pause() {
    this.playing = false;
  }

  /**
   * Sets the curernt amount of ticks passed in the animation.
   *
   * @param ticks the current amount of ticks we want to set the animation to.
   */
  public void setTicks(int ticks) {
    this.ticks = ticks;
  }

  /**
   * Resets the fields of the animation to its original state for playback. Useful for restarting.
   */
  public void resetFields() {
    this.playing = false;
    this.ticks = 0;
    this.copyAnimationList = copyAnimationList();
    this.copyShapeIdentifier = copyShapeList();
    this.ticks = 0;
  }

  /**
   * Getter method that rerturns the amount of ticks passed in the animation.
   *
   * @return the amount of ticks passed in the animation.
   */
  public int getTicks() {
    return this.ticks;
  }

  /**
   * Getter method to extract the current loop state of an animation.
   *
   * @return the boolean of whether or not it is looping.
   */
  public boolean isLooping() {
    return this.looping;
  }

  /**
   * Set the looping state of an animation to whatever boolean is given.
   *
   * @param l the looping state to be set to.
   */
  public void setLooping(boolean l) {
    this.looping = l;
  }

  /**
   * Updates the read only copy of the animation and assigns the copy's to be displayed.
   *
   * @param m the read only model that is read by the view.
   */
  public void updateReadOnly(IReadOnlyModel m) {
    this.playing = false;
    this.shapeIdentifier = m.getShapeIdentifier();
    this.animationList = m.getAnimationList();
    this.copyShapeIdentifier = copyShapeList();
    this.copyAnimationList = copyAnimationList();
    resetFields();
  }

  // copys the animation list from the read copy to be displayed
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
            default:
              throw new IllegalArgumentException("Cannot determine command.");
          }
        }
        isalNew.add(new SynchronizedActionSetImpl(isa.getStartTick(), isa.getEndTick(),
            newCommandList));

      }
      newList.put(key, isalNew);
    }
    return newList;
  }

  // copys the list of shapes from the read copy to be displayed by the view
  private LinkedHashMap<String, IShape> copyShapeList() {
    LinkedHashMap<String, IShape> newList = new LinkedHashMap<>();
    LinkedHashMap<String, IShape> oldList = shapeIdentifier;
    for (String key : oldList.keySet()) {
      IShape oldShape = oldList.get(key);
      Color newColor;
      Posn newPosn;
      Dimension newDimension;
      if (oldShape.getColor() == null) {
        newColor = null;
      } else {
        newColor = new Color(oldShape.getColor().getR(), oldShape.getColor().getG(),
            oldShape.getColor().getB());
      }
      if (oldShape.getPosn() == null) {
        newPosn = null;
      } else {
        newPosn = new Posn(oldShape.getPosn().getX(), oldShape.getPosn().getY());
      }
      if (oldShape.getDimension() == null) {
        newDimension = null;
      } else {
        newDimension = new Dimension(oldShape.getDimension().getW(),
            oldShape.getDimension().getH());
      }
      if (oldList.get(key).officialShapeName().equalsIgnoreCase("oval")) {
        newList.put(key, new Oval(newPosn, newDimension, newColor));
      } else if (oldList.get(key).officialShapeName().equalsIgnoreCase("rectangle")) {
        newList.put(key, new Rectangle(newPosn, newDimension, newColor));
      }
    }
    return newList;
  }
}