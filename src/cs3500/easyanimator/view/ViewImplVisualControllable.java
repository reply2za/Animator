package cs3500.easyanimator.view;

import cs3500.easyanimator.controller.IControllerFeatures;
import cs3500.easyanimator.model.IReadOnlyModel;
import java.awt.BorderLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;

public class ViewImplVisualControllable extends JFrame implements IView {

  IReadOnlyModel readOnlyModel;
  int ticksPerSecond;
  JButton restartButton;
  JButton pauseButton;
  JButton resumeButton;
  JButton loopingButton;
  JButton exitButton;
  JPanel buttonPane;
  UpdateDrawingEdit graphics;

  /**
   * Singular constructor that takes in the speed of the animation to be played.
   *
   * @param ticksPerSecond the speed of the animation.
   * @param readOnlyModel  the given read-only model
   */
  public ViewImplVisualControllable(int ticksPerSecond, IReadOnlyModel readOnlyModel) {

    super("Easy Animator");
    this.readOnlyModel = readOnlyModel;
    this.ticksPerSecond = ticksPerSecond;

    initializePane();
  }

  private void initializePane() {
    setLocation(readOnlyModel.getCanvasX(), readOnlyModel.getCanvasY());
    setSize(readOnlyModel.getCanvasWidth(), readOnlyModel.getCanvasHeight());
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    this.graphics = new UpdateDrawingEdit(readOnlyModel.getAnimationList(),
        readOnlyModel.getShapeIdentifier(), ticksPerSecond);

    // to add the buttons
    this.buttonPane = new JPanel();
    buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
    // delete later if not needed - no noticeable effects
    // buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
    buttonPane.add(Box.createHorizontalGlue()); // right aligns the button
    exitButton = new JButton("Exit");
    exitButton.setActionCommand("Exit Button");
    buttonPane.add(exitButton);
    restartButton = new JButton("Restart");
    restartButton.setActionCommand("Restart Button");
    buttonPane.add(restartButton);

    pauseButton = new JButton("Pause");
    pauseButton.setActionCommand("Pause Button");
    buttonPane.add(pauseButton);
    resumeButton = new JButton("Play");
    resumeButton.setActionCommand("Resume Button");
    buttonPane.add(resumeButton);

    loopingButton = new JButton("Looping: off");
    loopingButton.setActionCommand("Looping Button");
    buttonPane.add(loopingButton);

    //the slider
    ChangeListener sliderChange = e -> {
      JSlider source = (JSlider) e.getSource();
      if (!source.getValueIsAdjusting()) {
        graphics.secondsPerTick = source.getValue();
      }
    };

    JSlider speed = new JSlider(1, 101, ticksPerSecond);
    speed.addChangeListener(sliderChange);
    buttonPane.add(speed);
    speed.setMajorTickSpacing(10);
    speed.setMinorTickSpacing(1);
    speed.setPaintTicks(true);
    speed.setPaintLabels(true);

    this.add(buttonPane, BorderLayout.PAGE_END);
    this.add(graphics, BorderLayout.CENTER);
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
    restartButton.addActionListener(evt -> this.restartButton());
    resumeButton.addActionListener(evt -> this.resumeButton());
    pauseButton.addActionListener(evt -> this.pauseButton());
    loopingButton.addActionListener(evt -> this.loopingButton());

  }

  private void loopingButton() {
    if(graphics.looping) {
      loopingButton.setText("looping: off");
      graphics.setLooping(false);
    } else {
      loopingButton.setText("looping: on");
      graphics.setLooping(true);
    }
  }

  private void restartButton() {
    graphics.resetFields();
  }

  private void resumeButton() {
    graphics.startPlaying();
  }

  private void pauseButton() {
    graphics.pause();
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