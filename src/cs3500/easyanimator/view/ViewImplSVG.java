package cs3500.easyanimator.view;

import cs3500.easyanimator.controller.IControllerFeatures;
import cs3500.easyanimator.model.IAnimationModel;
import cs3500.easyanimator.model.IReadOnlyModel;
import cs3500.easyanimator.model.actions.IActionCommand;
import cs3500.easyanimator.model.actions.ISynchronisedActionSet;
import cs3500.easyanimator.model.shapes.Color;
import cs3500.easyanimator.model.shapes.Dimension;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.model.shapes.Posn;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * SVG View for the animation model. This outputs a file that can be read by an svg reader to create
 * full animations.
 */
public class ViewImplSVG implements IView {

  Appendable output;
  int secondsPerTick;
  IReadOnlyModel readOnlyModel;

  /**
   * Takes in an appendable type to be outputted.
   *
   * @param output         the output type for the text.
   * @param secondsPerTick The speed in which the animation plays.
   */
  public ViewImplSVG(Appendable output, int secondsPerTick, IReadOnlyModel readOnlyModel) {
    this.output = output;
    this.secondsPerTick = 1000 / secondsPerTick;
    this.readOnlyModel = readOnlyModel;
  }

  /**
   * An alternate constructor with a set speed of 1 tick per second.
   *
   * @param output the appendable type to be outputted.
   */
  public ViewImplSVG(Appendable output, IReadOnlyModel readOnlyModel) {
    this.output = output;
    this.secondsPerTick = 1000;
    this.readOnlyModel = readOnlyModel;
  }

  /**
   * Method to return the appendable so that it can be outputted to the proper channels.
   *
   * @return the appendable that is being outputted by the view.
   * @throws IOException If the appendable cannot be written to.
   */
  Appendable generateView() throws IOException {
    Appendable a;
    try {
      a = output.append(getSVGText(this.readOnlyModel));
    } catch (IOException io) {
      throw new IOException("Could not write to the Appendable.");
    }
    return a;
  }

  /**
   * Creates the view's visuals that is intended to be made as per the implementation and type of
   * view.
   */
  @Override
  public void showView() {
    try {
      generateView();
    } catch (IOException e) {
      e.printStackTrace();
      // intentionally left unedited
    }
  }

  /**
   * Method to get the svg type text. Converts our animation model read-only to animation model.
   *
   * @param model the read-only animation model that is used for outputting text.
   */
  private String getSVGText(IAnimationModel model) {
    LinkedHashMap<String,
        ArrayList<ISynchronisedActionSet>> animationList = model.getAnimationList();
    LinkedHashMap<String, IShape> shapeIdentifier = model.getShapeIdentifier();
    StringBuilder log = new StringBuilder();
    int biggestTick = 0;

    // change width and height
    log.append("<svg width=\"").append(model.getCanvasWidth()).append("\" height=\"")
        .append(model.getCanvasHeight()).append("\" version=\"1.1\"\n")
        .append("     xmlns=\"http://www.w3.org/2000/svg\">\n");

    for (String name : model.getShapeKeys()) {
      for (ISynchronisedActionSet isa : animationList.get(name)) {
        if (isa.getEndTick() > biggestTick) {
          biggestTick = isa.getEndTick();
        }
      }
    }

    //loops animation for given amount of time
    log.append("<rect>\n" + "<animate id=\"base\" begin=\"0;base.end\" dur=\"")
        .append((biggestTick * secondsPerTick) + secondsPerTick)
        .append("ms\" attributeName=\"visibility\" from=\"hide\" to=\"hide\"/>").append("</rect>");

    for (String key : animationList.keySet()) {
      IShape shape = shapeIdentifier.get(key);
      ArrayList<ISynchronisedActionSet> actions = animationList.get(key);
      switch (shapeIdentifier.get(key).officialShapeName()) {
        case ("rectangle"):
          log.append("<rect id=\"").append(key).append("\" x=\"").append(shape.getPosn().getX())
              .append("\" ").append("y=\"").append(shape.getPosn().getY()).append("\" width=\"")
              .append(shape.getDimension().getW()).append("\" height=\"")
              .append(shape.getDimension().getH()).append("\" fill=\"rgb(")
              .append(shape.getColor().getR()).append(",").append(shape.getColor().getG())
              .append(",").append(shape.getColor().getB()).append(")\" visibility=\"visible\" >\n");

          log.append(svgRectangleCommands(shape, actions));

          log.append("</rect>");
          break;
        case ("oval"):
          log.append("<ellipse id=\"").append(key).append("\" cx=\"").append(shape.getPosn().getX())
              .append("\" ").append("cy=\"").append(shape.getPosn().getY()).append("\" rx=\"")
              .append(shape.getDimension().getW()).append("\" ry=\"")
              .append(shape.getDimension().getH()).append("\" fill=\"rgb(")
              .append(shape.getColor().getR()).append(",").append(shape.getColor().getG())
              .append(",").append(shape.getColor().getB()).append(")\" visibility=\"visible\" >\n");

          log.append(svgEllipseCommands(shape, actions));

          log.append("</ellipse>");
          break;
        default:
          throw new IllegalArgumentException("Not a valid shape name");
      }
    }

    log.append("</svg>\n");
    return log.toString();
  }

  /**
   * The svg commands for an ellipse. There is a separate method because there are different
   * attributed for an ellipse than a rectangle.
   *
   * @param shape   the shape to have commands.
   * @param actions the commands that will be implemented on the shape in the textual view.
   * @return a String of action commands for the svg view.
   */
  private String svgEllipseCommands(IShape shape, ArrayList<ISynchronisedActionSet> actions) {
    StringBuilder logEllipse = new StringBuilder();
    for (ISynchronisedActionSet ia : actions) {
      Posn beforePosn = shape.getPosn();
      Dimension beforeDimension = shape.getDimension();
      for (int j = ia.getStartTick(); j < ia.getEndTick(); j++) {
        ia.applyAnimation(shape);
      }
      Posn afterPosn = shape.getPosn();
      Dimension afterDimension = shape.getDimension();
      Color afterColor = shape.getColor();
      for (IActionCommand ac : ia.getCommandList()) {
        switch (ac.officialName()) {
          case ("position"):
            logEllipse.append("<animate attributeType=\"xml\" begin=\"base.begin+")
                .append(ia.getStartTick() * secondsPerTick).append("ms\" ").append(" dur=\"")
                .append((ia.getEndTick() * secondsPerTick) - (ia.getStartTick() * secondsPerTick))
                .append("ms\" ");
            logEllipse.append("attributeName=\"cx\" from=\"").append(beforePosn.getX())
                .append("\" ").append("to=\"").append(afterPosn.getX())
                .append("\" fill=\"freeze\" />\n");
            logEllipse.append("<animate attributeType=\"xml\" begin=\"base.begin+")
                .append(ia.getStartTick() * secondsPerTick).append("ms\" ").append(" dur=\"")
                .append((ia.getEndTick() * secondsPerTick) - (ia.getStartTick() * secondsPerTick))
                .append("ms\" ");
            logEllipse.append("attributeName=\"cy\" from=\"").append(beforePosn.getY())
                .append("\" ").append("to=\"").append(afterPosn.getY())
                .append("\" fill=\"freeze\" />\n");

            break;
          case ("dimension"):
            logEllipse.append("<animate attributeType=\"xml\" begin=\"base.begin+")
                .append(ia.getStartTick() * secondsPerTick).append("ms\" ").append(" dur=\"")
                .append((ia.getEndTick() * secondsPerTick) - (ia.getStartTick() * secondsPerTick))
                .append("ms\" ");
            logEllipse.append("attributeName=\"rx\" from=\"").append(beforeDimension.getW())
                .append("\" ").append("to=\"").append(afterDimension.getW())
                .append("\" fill=\"freeze\" />\n");
            logEllipse.append("<animate attributeType=\"xml\" begin=\"base.begin+")
                .append(ia.getStartTick() * secondsPerTick).append("ms\"").append(" dur=\"")
                .append((ia.getEndTick() * secondsPerTick) - (ia.getStartTick() * secondsPerTick))
                .append("ms\" ");
            logEllipse.append("attributeName=\"ry\" from=\"").append(beforeDimension.getH())
                .append("\" ").append("to=\"").append(afterDimension.getH())
                .append("\" fill=\"freeze\" />\n");

            break;
          case ("color"):
            logEllipse.append("<animate attributeType=\"xml\" begin=\"base.begin+")
                .append(ia.getStartTick() * secondsPerTick).append("ms\"").append(" dur=\"")
                .append((ia.getEndTick() * secondsPerTick) - (ia.getStartTick() * secondsPerTick))
                .append("ms\" ");
            logEllipse.append("attributeName=\"fill\" " + "to=\"rgb(").append(afterColor.getR())
                .append(",").append(afterColor.getG()).append(",").append(afterColor.getB())
                .append(")\" fill=\"freeze\" />\n");

            break;
          default:
            throw new IllegalArgumentException("Not a valid command.");
        }
      }
    }
    return logEllipse.toString();
  }

  /**
   * The svg commands for an rectangle. There is a separate method because there are different
   * attributed for an ellipse than a rectangle.
   *
   * @param shape   the shape to have commands.
   * @param actions the commands that will be implemented on the shape in the textual view.
   * @return a String of animate commands for the svg view.
   */
  private String svgRectangleCommands(IShape shape, ArrayList<ISynchronisedActionSet> actions) {
    StringBuilder logRectangle = new StringBuilder();
    for (ISynchronisedActionSet ia : actions) {
      Posn beforePosn = shape.getPosn();
      Dimension beforeDimension = shape.getDimension();
      for (int j = ia.getStartTick(); j < ia.getEndTick(); j++) {
        ia.applyAnimation(shape);
      }
      Posn afterPosn = shape.getPosn();
      Dimension afterDimension = shape.getDimension();
      Color afterColor = shape.getColor();
      for (IActionCommand ac : ia.getCommandList()) {
        switch (ac.officialName()) {
          case ("position"):
            logRectangle.append("<animate attributeType=\"xml\" begin=\"base.begin+")
                .append(ia.getStartTick() * secondsPerTick).append("ms\" ").append("dur=\"")
                .append((ia.getEndTick() * secondsPerTick) - (ia.getStartTick() * secondsPerTick))
                .append("ms\" ");
            logRectangle.append("attributeName=\"x\" from=\"").append(beforePosn.getX())
                .append("\" ").append("to=\"").append(afterPosn.getX())
                .append("\" fill=\"freeze\" />\n");
            logRectangle.append("<animate attributeType=\"xml\" begin=\"base.begin+")
                .append(ia.getStartTick() * secondsPerTick).append("ms\" ").append("dur=\"")
                .append((ia.getEndTick() * secondsPerTick) - (ia.getStartTick() * secondsPerTick))
                .append("ms\" ");
            logRectangle.append("attributeName=\"y\" from=\"").append(beforePosn.getY())
                .append("\" ").append("to=\"").append(afterPosn.getY())
                .append("\" fill=\"freeze\" />\n");

            break;
          case ("dimension"):
            logRectangle.append("<animate attributeType=\"xml\" begin=\"base.begin+")
                .append(ia.getStartTick() * secondsPerTick).append("ms\" ").append("dur=\"")
                .append((ia.getEndTick() * secondsPerTick) - (ia.getStartTick() * secondsPerTick))
                .append("ms\" ");
            logRectangle.append("attributeName=\"width\" from=\"").append(beforeDimension.getW())
                .append("\" ").append("to=\"").append(afterDimension.getW())
                .append("\" fill=\"freeze\" />\n");
            logRectangle.append("<animate attributeType=\"xml\" begin=\"base.begin+")
                .append(ia.getStartTick() * secondsPerTick).append("ms\" ").append("dur=\"")
                .append((ia.getEndTick() * secondsPerTick) - (ia.getStartTick() * secondsPerTick))
                .append("ms\" ");
            logRectangle.append("attributeName=\"height\" from=\"").append(beforeDimension.getH())
                .append("\" ").append("to=\"").append(afterDimension.getH())
                .append("\" fill=\"freeze\" />\n");

            break;
          case ("color"):
            logRectangle.append("<animate attributeType=\"xml\" begin=\"base.begin+")
                .append(ia.getStartTick() * secondsPerTick).append("ms\" ").append("dur=\"")
                .append((ia.getEndTick() * secondsPerTick) - (ia.getStartTick() * secondsPerTick))
                .append("ms\" ");
            logRectangle.append("attributeName=\"fill\" " + "to=\"rgb(").append(afterColor.getR())
                .append(",").append(afterColor.getG()).append(",").append(afterColor.getB())
                .append(")\" fill=\"freeze\" />\n");

            break;
          default:
            throw new IllegalArgumentException("Not a valid command.");
        }
      }
    }
    return logRectangle.toString();
  }

  /**
   * Passes the features of the controller of type {@link IControllerFeatures} to the view. Allows
   * the view to retrieve these features from the controller to execute the proper reaction.
   *
   * @param features the possible actions that the controller supports, of type {@link
   *                 IControllerFeatures}
   */
  @Override
  public void addControllerFeatures(IControllerFeatures features) {
    // intentionally left blank
  }

  @Override
  public String toString() {
    return output.toString();
  }
}
