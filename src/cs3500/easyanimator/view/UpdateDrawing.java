package cs3500.easyanimator.view;

import cs3500.easyanimator.model.actions.ISynchronisedActionSet;
import cs3500.easyanimator.model.shapes.Color;
import cs3500.easyanimator.model.shapes.Dimension;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.model.shapes.Posn;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
public class UpdateDrawing extends JPanel implements ActionListener {

  LinkedHashMap<String, IShape> shapeIdentifier;
  LinkedHashMap<String, ArrayList<ISynchronisedActionSet>> animationList;
  int secondsPerTick;
  int ticks;
  //java.awt.Dimension d;

  /**
   * This is the constructor for the class that handles updating the view as the time passes.
   *
   * @param animationList   The list of animations handled.
   * @param shapeIdentifier The shapes to be drawn.
   * @param secondsPerTick  The speed in which the animation runs.
   * @throws IllegalArgumentException if the given speed (secondsPerTick) is non-positive.
   */
  UpdateDrawing(LinkedHashMap<String, ArrayList<ISynchronisedActionSet>> animationList,
      LinkedHashMap<String, IShape> shapeIdentifier, int secondsPerTick) {
    if (secondsPerTick <= 0) {
      throw new IllegalArgumentException("Speed must be a positive integer.");
    }
    this.secondsPerTick = secondsPerTick;
    this.shapeIdentifier = shapeIdentifier;
    this.animationList = animationList;
    this.ticks = 0;
  }

  /**
   * An overridden paint method so that we are able to draw all shapes that need to be drawn.
   *
   * @param g The graphics that we are using to draw the shapes on.
   */
  @Override
  public void paint(Graphics g) {
    super.paintComponents(g);
    //this.setPreferredSize(d);
    Graphics2D g2d = (Graphics2D) g;
    // paint all shapes
    for (String key : shapeIdentifier.keySet()) {
      IShape currentShape = shapeIdentifier.get(key);
      // Get all of the fields to easily call later
      Posn currentPosn = currentShape.getPosn();
      Dimension currentDimension = currentShape.getDimension();
      Color currentColor = currentShape.getColor();
      java.awt.Color awtColor = new java.awt.Color(currentColor.getR(), currentColor.getG(),
          currentColor.getB());

      switch (currentShape.officialShapeName()) {
        case ("rectangle"):
          g2d.fillRect(currentPosn.getX(), currentPosn.getY(),
              currentDimension.getW(), currentDimension.getH());
          g2d.setColor(awtColor);
          break;
        case ("oval"):
          g2d.fillOval(currentPosn.getX(), currentPosn.getY(),
              currentDimension.getW(), currentDimension.getH());
          g2d.setColor(awtColor);
          break;
        default:
          throw new IllegalArgumentException("Not a valid shape.");
      }
    }
    Timer timer = new Timer(1000 / secondsPerTick, this);
    ticks++;
    timer.start();
  }

  /**
   * The action listener for the class. This is called every time the timer moves one tick
   * duration.
   *
   * @param e The action event listener for the timer.
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    for (String name : animationList.keySet()) {
      ArrayList<ISynchronisedActionSet> listOfActions = animationList.get(name);
      IShape currentShape = shapeIdentifier.get(name);
      for (ISynchronisedActionSet ai : listOfActions) {
        if (ticks <= ai.getEndTick() && ticks >= ai.getStartTick()) {
          ai.applyAnimation(currentShape);
          // attempts to change size of the panel if shape goes off screen
 /*         if (currentShape.getPosn().getX() > d.width) {
            this.d.setSize(currentShape.getPosn().getX()
                    + currentShape.getDimension().getW() + 40,
                d.height);
          }
          if (currentShape.getPosn().getY() > d.height) {
            this.d.setSize(d.width,
                currentShape.getPosn().getY() + currentShape.getDimension().getH() + 40);
          }*/
          break;
        }
      }
      // Animations seem to run fine without these two method calls
      //animationList.put(name, listOfActions);
      //shapeIdentifier.put(name, currentShape);
    }
    repaint();
  }

  public void setTicks(int ticks) {
    System.out.println("Current ticks are " + this.ticks + ". Setting ticks to: " + ticks);
    this.ticks = ticks;
    System.out.println("Ticks have been set: " + this.ticks);
  }

  public void setAnimationList(LinkedHashMap<String, ArrayList<ISynchronisedActionSet>> aniList) {
    System.out.println("before clear: " + aniList.size()); // some number
    this.animationList.clear();
    System.out.println("after clear: " + aniList.size()); // 0
    System.out.println("size of ani: " + animationList.size());

    this.animationList.putAll(aniList);
    System.out.println("size of final ani: " + animationList.size());
    repaint();
  }

  public void setShapeIdentifier(LinkedHashMap<String, IShape> shapes) {
    this.shapeIdentifier.clear();
    //System.out.println("size of size: " + shapes.size());
    this.shapeIdentifier.putAll(shapes);
    repaint();
  }
}
