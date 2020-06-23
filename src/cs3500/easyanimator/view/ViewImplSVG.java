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
   * A constructor that requires an appendable type to output the textual data, a speed for the
   * tick, and a read-only model.
   *
   * @param output         the output type for the text
   * @param secondsPerTick the tick speed in which the animation plays
   * @param readOnlyModel  the read-only model of type {@link IReadOnlyModel}
   */
  public ViewImplSVG(Appendable output, int secondsPerTick, IReadOnlyModel readOnlyModel) {
    this.output = output;
    this.secondsPerTick = 1000 / secondsPerTick;
    this.readOnlyModel = readOnlyModel;
  }

  /**
   * Method to generate the SCG text and append it to the given appendable.
   *
   * @throws IOException if the appendable cannot be written to
   */
  private void generateView() throws IOException {
    output.append(getSVGText(this.readOnlyModel));
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
    StringBuilder log = new StringBuilder();
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
            log.append("<animate attributeType=\"xml\" begin=\"base.begin+")
                .append(ia.getStartTick() * secondsPerTick).append("ms\" ").append(" dur=\"")
                .append((ia.getEndTick() * secondsPerTick) - (ia.getStartTick() * secondsPerTick))
                .append("ms\" ");
            log.append("attributeName=\"cx\" from=\"").append(beforePosn.getX())
                .append("\" ").append("to=\"").append(afterPosn.getX())
                .append("\" fill=\"freeze\" />\n");
            log.append("<animate attributeType=\"xml\" begin=\"base.begin+")
                .append(ia.getStartTick() * secondsPerTick).append("ms\" ").append(" dur=\"")
                .append((ia.getEndTick() * secondsPerTick) - (ia.getStartTick() * secondsPerTick))
                .append("ms\" ");
            log.append("attributeName=\"cy\" from=\"").append(beforePosn.getY())
                .append("\" ").append("to=\"").append(afterPosn.getY())
                .append("\" fill=\"freeze\" />\n");

            break;
          case ("dimension"):
            log.append("<animate attributeType=\"xml\" begin=\"base.begin+")
                .append(ia.getStartTick() * secondsPerTick).append("ms\" ").append(" dur=\"")
                .append((ia.getEndTick() * secondsPerTick) - (ia.getStartTick() * secondsPerTick))
                .append("ms\" ");
            log.append("attributeName=\"rx\" from=\"").append(beforeDimension.getW())
                .append("\" ").append("to=\"").append(afterDimension.getW())
                .append("\" fill=\"freeze\" />\n");
            log.append("<animate attributeType=\"xml\" begin=\"base.begin+")
                .append(ia.getStartTick() * secondsPerTick).append("ms\"").append(" dur=\"")
                .append((ia.getEndTick() * secondsPerTick) - (ia.getStartTick() * secondsPerTick))
                .append("ms\" ");
            log.append("attributeName=\"ry\" from=\"").append(beforeDimension.getH())
                .append("\" ").append("to=\"").append(afterDimension.getH())
                .append("\" fill=\"freeze\" />\n");

            break;
          case ("color"):
            log.append("<animate attributeType=\"xml\" begin=\"base.begin+")
                .append(ia.getStartTick() * secondsPerTick).append("ms\"").append(" dur=\"")
                .append((ia.getEndTick() * secondsPerTick) - (ia.getStartTick() * secondsPerTick))
                .append("ms\" ");
            log.append("attributeName=\"fill\" " + "to=\"rgb(").append(afterColor.getR())
                .append(",").append(afterColor.getG()).append(",").append(afterColor.getB())
                .append(")\" fill=\"freeze\" />\n");

            break;
          default:
            throw new IllegalArgumentException("Not a valid command.");
        }
      }
    }
    return log.toString();
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
    StringBuilder log = new StringBuilder();
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
            log.append("<animate attributeType=\"xml\" begin=\"base.begin+")
                .append(ia.getStartTick() * secondsPerTick).append("ms\" ").append("dur=\"")
                .append((ia.getEndTick() * secondsPerTick) - (ia.getStartTick() * secondsPerTick))
                .append("ms\" ");
            log.append("attributeName=\"x\" from=\"").append(beforePosn.getX())
                .append("\" ").append("to=\"").append(afterPosn.getX())
                .append("\" fill=\"freeze\" />\n");
            log.append("<animate attributeType=\"xml\" begin=\"base.begin+")
                .append(ia.getStartTick() * secondsPerTick).append("ms\" ").append("dur=\"")
                .append((ia.getEndTick() * secondsPerTick) - (ia.getStartTick() * secondsPerTick))
                .append("ms\" ");
            log.append("attributeName=\"y\" from=\"").append(beforePosn.getY())
                .append("\" ").append("to=\"").append(afterPosn.getY())
                .append("\" fill=\"freeze\" />\n");

            break;
          case ("dimension"):
            log.append("<animate attributeType=\"xml\" begin=\"base.begin+")
                .append(ia.getStartTick() * secondsPerTick).append("ms\" ").append("dur=\"")
                .append((ia.getEndTick() * secondsPerTick) - (ia.getStartTick() * secondsPerTick))
                .append("ms\" ");
            log.append("attributeName=\"width\" from=\"").append(beforeDimension.getW())
                .append("\" ").append("to=\"").append(afterDimension.getW())
                .append("\" fill=\"freeze\" />\n");
            log.append("<animate attributeType=\"xml\" begin=\"base.begin+")
                .append(ia.getStartTick() * secondsPerTick).append("ms\" ").append("dur=\"")
                .append((ia.getEndTick() * secondsPerTick) - (ia.getStartTick() * secondsPerTick))
                .append("ms\" ");
            log.append("attributeName=\"height\" from=\"").append(beforeDimension.getH())
                .append("\" ").append("to=\"").append(afterDimension.getH())
                .append("\" fill=\"freeze\" />\n");

            break;
          case ("color"):
            log.append("<animate attributeType=\"xml\" begin=\"base.begin+")
                .append(ia.getStartTick() * secondsPerTick).append("ms\" ").append("dur=\"")
                .append((ia.getEndTick() * secondsPerTick) - (ia.getStartTick() * secondsPerTick))
                .append("ms\" ");
            log.append("attributeName=\"fill\" " + "to=\"rgb(").append(afterColor.getR())
                .append(",").append(afterColor.getG()).append(",").append(afterColor.getB())
                .append(")\" fill=\"freeze\" />\n");

            break;
          default:
            throw new IllegalArgumentException("Not a valid command.");
        }
      }
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
