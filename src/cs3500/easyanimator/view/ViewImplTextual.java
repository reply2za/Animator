package cs3500.easyanimator.view;

import cs3500.easyanimator.controller.IControllerFeatures;
import cs3500.easyanimator.model.IAnimationModel;
import cs3500.easyanimator.model.IReadOnlyModel;
import cs3500.easyanimator.model.actions.ISynchronisedActionSet;
import cs3500.easyanimator.model.shapes.IShape;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * A textual implementation of a animation models view. Allows for output to a file or console.
 */
public class ViewImplTextual implements IView {

  Appendable output;
  // stores the animations with the name
  private final Map<String, ArrayList<ISynchronisedActionSet>> animationList;
  private final Map<String, IShape> shapeIdentifier; // name of a shape and its reference

  /**
   * Takes in an output type for the view to output to.
   *
   * @param output the output type for the view
   * @param m      the read-only model of type {@link IReadOnlyModel}
   */
  public ViewImplTextual(Appendable output, IReadOnlyModel m) {
    this.output = output;
    animationList = m.getAnimationList();
    shapeIdentifier = m.getShapeIdentifier();
  }

  /**
   * Method to return the appendable so that it can be outputted to the proper channels.
   *
   * @throws IOException If the appendable cannot be written to.
   */
  private void generateView() throws IOException {
    output.append(getAnimationLog(animationList, shapeIdentifier));
  }

  /**
   * Given a shape of type {@link IShape} and an animation of type {@link ISynchronisedActionSet}
   * and a String representing the shape's unique name. Prints out the state of the shape before and
   * after the animation is applied. This is returned as a string in a log formation in regards to
   * the shape. Assuming that it is a part of the 'shapeIdentifier' HashMap, this method will also
   * put the mutated shape in the map.
   *
   * @return A string that prints out the full animation state for the total time.
   */
  private String getAnimationLog(Map<String, ArrayList<ISynchronisedActionSet>> animationList,
      Map<String, IShape> shapeIdentifier) {
    StringBuilder log = new StringBuilder();
    int logSpaceDivider = 6; // the amount of spaces between the start and end log
    IShape shape;
    int startTick;
    int endTick;
    log.append("Columns:\n");
    log.append("t x y w h r g b      t x y w h r g b");
    for (String key : animationList.keySet()) {
      shape = shapeIdentifier.get(key);
      log.append("\n").append("shape ").append(key).append(" ")
          .append(shape.officialShapeName());
      for (ISynchronisedActionSet ia : animationList.get(key)) {
        startTick = ia.getStartTick();
        endTick = ia.getEndTick();
        log.append("\n").append(startTick).append(" ")
            .append(shape.toString());
        log.append(" ".repeat(logSpaceDivider)); // adds the space divider
        // changes the shape in the map to the new shape
        // mutates here - mutateShapeAndReplace
        for (int j = startTick; j < ia.getEndTick(); j++) {
          // mutates the shape to the desired ISynchronisedActionSet
          ia.applyAnimation(shape);
        }
        log.append(endTick).append(" ")
            .append(shape.toString());
      }
    }
    return log.toString();
  }

  /**
   * Prints out a more detailed description of the animations in the {@link IAnimationModel} for the
   * visually impaired.
   *
   * @return A string that prints out the full animation description for the total duration.
   */
  private String getAnimationDescription(
      Map<String, ArrayList<ISynchronisedActionSet>> animationList,
      Map<String, IShape> shapeIdentifier) {
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
        // mutates here - mutateShapeAndReplace
        for (int j = ia.getStartTick(); j < ia.getEndTick(); j++) {
          ia.applyAnimation(shapeIdentifier.get(key));
        } // mutates the shape to the desired ISynchronisedActionSet
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
  public void showView() {
    try {
      generateView();
    } catch (IOException e) {
      e.printStackTrace();
      // intentionally left unedited
    }
  }

  @Override
  public void addFeatureListeners(IControllerFeatures features) {
    // intentionally left blank
  }
}

