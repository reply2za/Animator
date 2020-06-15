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

  /**
   * This is the constuctor for the class that handles updating the view as the time passes.
   *
   * @param animationList   The list of animations handled.
   * @param shapeIdentifier The shapes to be drawn.
   * @param secondsPerTick  The speed in which the animation runs.
   */
  UpdateDrawing(LinkedHashMap<String, ArrayList<ISynchronisedActionSet>> animationList,
      LinkedHashMap<String, IShape> shapeIdentifier, int secondsPerTick) {
    this.secondsPerTick = secondsPerTick;
    this.shapeIdentifier = shapeIdentifier;
    this.animationList = animationList;
    this.ticks = 0;
  }

  /**
   * An overriden paint method so that we are able to draw all shapes that need to be drawn.
   *
   * @param g The graphics that we are using to draw the shapes on.
   */
  @Override
  public void paint(Graphics g) {
    super.paintComponents(g);
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
      ArrayList<ISynchronisedActionSet> currentAni = animationList.get(name);
      IShape currentShape = shapeIdentifier.get(name);
      for (ISynchronisedActionSet ai : currentAni) {
        if (ai.getStartTick() <= ticks && ticks <= ai.getEndTick()) {
          ai.applyAnimation(currentShape);
          break;
        }
      }
      animationList.put(name, currentAni);
      shapeIdentifier.put(name, currentShape);

      repaint();
    }
  }
}
