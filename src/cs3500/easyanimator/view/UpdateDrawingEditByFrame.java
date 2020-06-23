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
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * This is the class that is in responsible for drawing and updating our visual view. It uses a
 * timer to set the speed and make repetitive changes.
 */
public class UpdateDrawingEditByFrame extends JPanel implements ActionListener, IGraphicsEdit2 {

  private LinkedHashMap<String, IShape> shapeIdentifier;
  private LinkedHashMap<String, ArrayList<ISynchronisedActionSet>> animationList;
  private LinkedHashMap<String, IShape> copyShapeIdentifier;
  private LinkedHashMap<String, ArrayList<ISynchronisedActionSet>> copyAnimationList;
  private int currentTick;
  private boolean isPlaying;
  private boolean looping;
  private int biggestTick;
  private int timeDelay;
  private int initialDelay;
  Timer timer;
  IView2 view;
  HashMap<Integer, Collection<IShape>> mapOfShapes;

  /**
   * This is the constructor for the class that handles updating the view as the time passes.
   *
   * @param animationList   The list of animations handled.
   * @param shapeIdentifier The shapes to be drawn.
   * @param secondsPerTick  The speed in which the animation runs.
   * @throws IllegalArgumentException if the given speed (secondsPerTick) is non-positive.
   */
  UpdateDrawingEditByFrame(LinkedHashMap<String, ArrayList<ISynchronisedActionSet>> animationList,
      LinkedHashMap<String, IShape> shapeIdentifier, int secondsPerTick) {
    if (secondsPerTick <= 0) {
      throw new IllegalArgumentException("Speed must be a positive integer.");
    }
    this.shapeIdentifier = shapeIdentifier;
    this.animationList = animationList;
    this.copyShapeIdentifier = copyShapeList();
    this.copyAnimationList = copyAnimationList();
    this.currentTick = 0;
    mapOfShapes = new HashMap<>();
    this.looping = false;
    this.isPlaying = false;

    this.biggestTick = 0;
    for (String key : copyAnimationList.keySet()) {
      ArrayList<ISynchronisedActionSet> tempList = copyAnimationList.get(key);
      if (tempList.size() > 0) {
        int test = tempList.get(tempList.size() - 1).getEndTick();
        if (test > biggestTick) {
          this.biggestTick = test;
        }
      }
    }
    applyAll();

    timeDelay = 1000 / secondsPerTick;
    initialDelay = 10 / secondsPerTick;

    timer = new Timer(1000, this);
    timer.setDelay(timeDelay);
    timer.setInitialDelay(10);
    timer.restart();
  }


  /**
   * An overridden paint method so that we are able to draw all shapes that need to be drawn.
   *
   * @param g The graphics that we are using to draw the shapes on.
   */
  @Override
  public void paint(Graphics g) {
    // paint all shapes
    super.paintComponents(g);
    if (currentTick > biggestTick) {
      if (looping) {
        currentTick = 0;
        return;
      } else {
        isPlaying = false;
        return;
      }
    }
    for (IShape currentShape : mapOfShapes.get(currentTick)) {
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
    timer.restart();
  }

  /**
   * The action listener for the class. This is called every time the timer moves one tick
   * duration.
   *
   * @param e The action event listener for the timer.
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    //timer.stop();
    if (isPlaying) {
      currentTick++;
      repaint();
      passTicksToView();
    }
    //timer.start();
  }


  @Override
  public void setTickSpeed(int secondsPerTick) {
    if (secondsPerTick != 1) {
      secondsPerTick = 94 + secondsPerTick;
    }
    timeDelay = 1000 - (10 * secondsPerTick);
    initialDelay = 10 / secondsPerTick;
    timer.setDelay(timeDelay);
    timer.setInitialDelay(initialDelay);
    timer.setCoalesce(false);
  }

  @Override
  public void play(boolean play) {
    if (play) {
      isPlaying = true;
      timer.start();
    } else {
      isPlaying = false;
    }
  }


  @Override
  public void setCurrentTick(int currentTick) {
    this.currentTick = currentTick;
    repaint();
  }

  @Override
  public void resetFields() {
    timer.stop();
    isPlaying = false;
    this.copyAnimationList = copyAnimationList();
    this.copyShapeIdentifier = copyShapeList();
    view.updateTicks(0);
    isPlaying = false;
    this.currentTick = 0;
    view.triggerPauseButtonBlue();
  }

  @Override
  public int getCurrentTick() {
    return this.currentTick;
  }

@Override
  public boolean isLooping() {
    return this.looping;
  }

  @Override
  public void setLooping(boolean l) {
    this.looping = l;
    if (looping && currentTick > biggestTick) {
      isPlaying = true;
    }
  }

  @Override
  public void updateReadOnly(IReadOnlyModel m) {
    this.isPlaying = false;
    this.shapeIdentifier = m.getShapeIdentifier();
    this.animationList = m.getAnimationList();
    this.copyShapeIdentifier = this.copyShapeList();
    this.copyAnimationList = this.copyAnimationList();
  }

  /**
   * Gives the graphics implementation a reference to the view. This allows it to call methods to
   * provide information and updates to the view. Should be called by the view.
   *
   * @param v the given view that the graphics will be using of type {@link IView2}
   */
  public void setView(IView2 v) {
    this.view = v;
  }


  // copys the animation list from the read copy to be displayed
  private LinkedHashMap<String, ArrayList<ISynchronisedActionSet>> copyAnimationList() {
    LinkedHashMap<String, ArrayList<ISynchronisedActionSet>> newList = new LinkedHashMap<>();
    LinkedHashMap<String, ArrayList<ISynchronisedActionSet>> oldList = animationList;
    for (String key : oldList.keySet()) {
      ArrayList<ISynchronisedActionSet> oldISAL = oldList.get(key);
      ArrayList<ISynchronisedActionSet> newISAL = new ArrayList<>();
      for (ISynchronisedActionSet isa : oldISAL) {
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
        newISAL.add(new SynchronizedActionSetImpl(isa.getStartTick(), isa.getEndTick(),
            newCommandList));

      }
      newList.put(key, newISAL);
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

  // copys the list of shapes from the read copy to be displayed by the view
  private LinkedHashMap<String, IShape> copyShapeList(LinkedHashMap<String, IShape> orig) {
    LinkedHashMap<String, IShape> newList = new LinkedHashMap<>();
    for (String key : orig.keySet()) {
      IShape oldShape = orig.get(key);
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
      if (orig.get(key).officialShapeName().equalsIgnoreCase("oval")) {
        newList.put(key, new Oval(newPosn, newDimension, newColor));
      } else if (orig.get(key).officialShapeName().equalsIgnoreCase("rectangle")) {
        newList.put(key, new Rectangle(newPosn, newDimension, newColor));
      }
    }
    return newList;
  }

  /**
   * This method
   */
  private void passTicksToView() {
    view.updateTicks(currentTick);
  }

  /**
   * Calculates all of the animations and places it in a HashMap of integers and list of {@link
   * IShape}. The key of integers are the ticks with the state of the shapes being the values.
   */
  private void applyAll() {

    for (int i = 0; i <= biggestTick; i++) {
      for (String name : copyAnimationList.keySet()) {
        ArrayList<ISynchronisedActionSet> listOfActions = copyAnimationList.get(name);
        IShape currentShape = copyShapeIdentifier.get(name);
        for (ISynchronisedActionSet ai : listOfActions) {
          if (i <= ai.getEndTick() && i >= ai.getStartTick()) {
            ai.applyAnimation(currentShape);
            break;
          }
        }
      }
      mapOfShapes.put(i, copyShapeList(copyShapeIdentifier).values());
    }
  }

}