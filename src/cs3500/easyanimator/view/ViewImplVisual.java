package cs3500.easyanimator.view;

import cs3500.easyanimator.controller.IControllerFeatures;
import cs3500.easyanimator.model.IReadOnlyModel;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Represents a visual view for the animation model. Shows the animation on a client using java
 * swing library.
 */
public class ViewImplVisual extends JFrame implements IView {

  protected IReadOnlyModel readOnlyModel;
  protected Container c;

  /**
   * Singular constructor that takes in the speed of the animation to be played.
   *
   * @param ticksPerSecond the speed of the animation
   * @param readOnlyModel  the given read-only model
   */
  public ViewImplVisual(int ticksPerSecond, IReadOnlyModel readOnlyModel) {
    super("Easy Animator");
    this.readOnlyModel = readOnlyModel;

    //defaults for the window
    setLocation(readOnlyModel.getCanvasX(), readOnlyModel.getCanvasY());
    setSize(readOnlyModel.getCanvasWidth(), readOnlyModel.getCanvasHeight());
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // panel where the animations occur
    JPanel panel = new UpdateDrawing(readOnlyModel.getAnimationList(),
        readOnlyModel.getShapeIdentifier(), ticksPerSecond);
    panel.setPreferredSize(
        new Dimension(readOnlyModel.getCanvasWidth(), readOnlyModel.getCanvasHeight()));

    // JScrollPane which allows for scrollbars
    JScrollPane jScrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    // the window toolkit that holds the pane
    c = getContentPane();
    c.add(panel);
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
    //exitButton.addActionListener(evt -> features.exitProgram());
  }

  /**
   * Creates the view's visuals that is intended to be made as per the implementation and type of
   * view.
   */
  @Override
  public void showView() {
    setVisible(true);
  }

}
