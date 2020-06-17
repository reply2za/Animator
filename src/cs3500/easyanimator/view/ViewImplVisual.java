package cs3500.easyanimator.view;

import cs3500.easyanimator.controller.IControllerFeatures;
import cs3500.easyanimator.model.IReadOnlyModel;
import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

/**
 * Represents a visual view for the animation model. Shows the animation on a client using java
 * swing library.
 */
public class ViewImplVisual extends JFrame implements IView {

  protected JButton exitButton;
  protected IReadOnlyModel readOnlyModel;

  /**
   * Singular constructor that takes in the speed of the animation to be played.
   *
   * @param ticksPerSecond the speed of the animation.
   */
  public ViewImplVisual(int ticksPerSecond, IReadOnlyModel readOnlyModel) {
    super("Easy Animator");

    this.readOnlyModel = readOnlyModel;

    setLocation(readOnlyModel.getCanvasX(), readOnlyModel.getCanvasY());
    setSize(readOnlyModel.getCanvasWidth(), readOnlyModel.getCanvasHeight());
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JScrollPane listScroller = new JScrollPane();
    listScroller.setAlignmentX(LEFT_ALIGNMENT);
    listScroller.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);

    // delete later if not needed - no noticeable effects
  //    JFrame jFrame = new JFrame();
  //    jFrame.add(new UpdateDrawing(readOnlyModel.getAnimationList(),
  //        readOnlyModel.getShapeIdentifier(), ticksPerSecond));

    // to add the buttons
    JPanel buttonPane = new JPanel();
    buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
    // delete later if not needed - no noticeable effects
  //    buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
  //    buttonPane.add(Box.createHorizontalGlue()); // right aligns the button
    exitButton = new JButton("Exit");
    exitButton.setActionCommand("Exit Button");
    buttonPane.add(exitButton);

    Container contentPane = getContentPane();
    contentPane.add(buttonPane, BorderLayout.PAGE_END);
    contentPane.add(new UpdateDrawing(readOnlyModel.getAnimationList(),
        readOnlyModel.getShapeIdentifier(), ticksPerSecond));
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
    exitButton.addActionListener(evt -> features.exitProgram());
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
