package cs3500.easyanimator.view;

import cs3500.easyanimator.controller.IControllerFeatures;
import cs3500.easyanimator.model.IReadOnlyModel;
import cs3500.easyanimator.model.actions.ISynchronisedActionSet;
import cs3500.easyanimator.model.shapes.IShape;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;

/**
 * A editable view for the animation. Has custom stop, start, restart buttons and gives user the
 * ability to edit keyframes and shapes.
 */
public class ViewImplVisualControllable2 extends JFrame implements IView2 {

  private IReadOnlyModel readOnlyModel;
  private int ticksPerSecond;
  private JButton restartButton;
  private JButton pauseButton;
  private JButton playButton;
  private JButton loopingButton;
  private JButton exitButton;
  private JLabel addShapeLabel;
  private JTextField shapeNameTextField;
  private JTextField colorTextField;
  private JTextField positionTextField;
  private JTextField dimensionTextField;
  private JTextField ticksTextField;
  private JLabel shapeNameTextLabel;
  private JLabel colorTextLabel;
  private JLabel positionTextLabel;
  private JLabel dimensionTextLabel;
  private JLabel ticksTextLabel;
  private JLabel infoTextLabel;
  private JLabel requiredFieldsTextLabel;
  private JComboBox<String> shapeComboBox;
  private JButton commitButton;
  private UpdateDrawingEditByFrame graphics;
  private String shapeText;
  private String ticksText;
  private String colorText;
  private String dimensionText;
  private String positionText;
  private final JLabel speedLabel;
  private final JTextField speedTextField;
  private JSlider animationSlider;
  private JPanel buttonPane;
  private int currentTick;
  private int timeDelay;
  private int initialDelay;
  private int secondsPerTick;
  private int biggestTick;
  private boolean isPlaying;

  /**
   * Singular constructor that takes in the speed of the animation to be played.
   *
   * @param ticksPerSecond the speed of the animation.
   * @param readOnlyModel  the given read-only model
   */
  public ViewImplVisualControllable2(int ticksPerSecond, IReadOnlyModel readOnlyModel) {

    super("Easy Animator");
    this.readOnlyModel = readOnlyModel;
    this.ticksPerSecond = ticksPerSecond;
    this.shapeText = "";
    this.ticksText = "";
    this.colorText = "";
    this.dimensionText = "";
    this.positionText = "";
    this.speedLabel = new JLabel("Speed (1-5): 1");
    this.speedTextField = new JTextField();

    this.isPlaying = true;
    secondsPerTick = 1;
    timeDelay = 1000 / secondsPerTick;
    initialDelay = 10 / secondsPerTick;
    biggestTick = 0;
    initializeFrame();
  }

  /**
   * Initializes the pane that all of the information will be on. Contains all necessary info for
   * the editable view. Should be the only main-window initialize method to be called in the
   * constructor. Other initialize methods regarding parts of the main frame should be placed here.
   */
  private void initializeFrame() {
    setLocation(readOnlyModel.getCanvasX(), readOnlyModel.getCanvasY());
    setSize(readOnlyModel.getCanvasWidth() + 210,
        readOnlyModel.getCanvasHeight() + 75);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    this.graphics = new UpdateDrawingEditByFrame(readOnlyModel.getAnimationList(),
        readOnlyModel.getShapeIdentifier(), ticksPerSecond);

    graphics.setView(this);

    initializeButtonPane();

    // EDIT PANE: To manipulate the keyframes -----

    JPanel editPane = new JPanel();
    editPane.setLayout(new BoxLayout(editPane, BoxLayout.Y_AXIS));
    editPane.setMaximumSize(editPane.getPreferredSize());
    addShapeLabel = new JLabel("<HTML><h3>Your action:</h3></HTML>");
    infoTextLabel = new JLabel("Selected: None");
    //infoTextLabel.setMaximumSize(editPane.getPreferredSize());
    String[] shapeStrings = {"None", "Add rectangle", "Add ellipse", "Add keyframe",
        "Edit keyframe",
        "Remove keyframe", "Remove shape",
    };
    shapeComboBox = new JComboBox<>(shapeStrings);

    // add text fields
    this.colorTextField = new JTextField();
    this.dimensionTextField = new JTextField();
    this.positionTextField = new JTextField();
    this.shapeNameTextField = new JTextField();
    this.ticksTextField = new JTextField();
    colorTextField.setMaximumSize(new Dimension(100, colorTextField.getPreferredSize().height));
    dimensionTextField
        .setMaximumSize(new Dimension(100, dimensionTextField.getPreferredSize().height));
    positionTextField
        .setMaximumSize(new Dimension(100, positionTextField.getPreferredSize().height));
    shapeNameTextField
        .setMaximumSize(new Dimension(100, shapeNameTextField.getPreferredSize().height));
    ticksTextField
        .setMaximumSize(new Dimension(100, shapeNameTextField.getPreferredSize().height));
    this.colorTextLabel = new JLabel("Enter a color (r,g,b)");
    this.dimensionTextLabel = new JLabel("Enter a dimension (w,h)");
    this.positionTextLabel = new JLabel("Enter a position (x,y):");
    this.shapeNameTextLabel = new JLabel("Enter a shape name:");
    this.ticksTextLabel = new JLabel("Enter the ticks (start,end)");
    this.requiredFieldsTextLabel = new JLabel("<HTML><b>Required fields:</b><br>"
        + "<i>values with multiple numbers<br> should be separated by a space</i><br>"
        + "__________________________<br> <br></HTML>");

    //commit button
    commitButton = new JButton("Commit");
    commitButton.setActionCommand("Commit Button");

    // ADD TO EDIT PANE ---------
    JLabel editPaneTitle = new JLabel("<HTML><h2>EDIT ANIMATION</h2></HTML>");

    editPane.setBackground(Color.LIGHT_GRAY);
    editPane.add(editPaneTitle);
    editPane.add(new JLabel(" "));
    editPane.add(new JLabel(" "));
    editPane.add(new JLabel(" "));
    editPane.add(new JLabel(" "));
    editPane.add(addShapeLabel);
    editPane.add(infoTextLabel);
    editPane.add(shapeComboBox);
    editPane.add(requiredFieldsTextLabel);
    editPane.add(shapeNameTextLabel);
    editPane.add(shapeNameTextField);
    editPane.add(positionTextLabel);
    editPane.add(positionTextField);
    editPane.add(dimensionTextLabel);
    editPane.add(dimensionTextField);
    editPane.add(colorTextLabel);
    editPane.add(colorTextField);
    editPane.add(ticksTextLabel);
    editPane.add(ticksTextField);
    editPane.add(commitButton);

    shapeNameTextField.setVisible(false);
    positionTextField.setVisible(false);
    dimensionTextField.setVisible(false);
    colorTextField.setVisible(false);
    ticksTextField.setVisible(false);
    shapeNameTextLabel.setVisible(false);
    positionTextLabel.setVisible(false);
    dimensionTextLabel.setVisible(false);
    colorTextLabel.setVisible(false);
    ticksTextLabel.setVisible(false);
    requiredFieldsTextLabel.setVisible((true));



/*
    Timer timer = new Timer(timeDelay, this);
    timer.setInitialDelay(initialDelay);
    timer.start();
*/



    this.add(buttonPane, BorderLayout.PAGE_END);
    this.add(graphics, BorderLayout.CENTER);
    this.add(editPane, BorderLayout.EAST);

  }

  private void initializeButtonPane() {
    // BUTTON PANE: To add the buttons -----
    buttonPane = new JPanel();
    buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
    // delete later if not needed - no noticeable effects
    // buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
    //buttonPane.add(Box.createHorizontalGlue()); // right aligns the button
    exitButton = new JButton("Exit");
    exitButton.setActionCommand("Exit Button");
    buttonPane.add(exitButton);

    loopingButton = new JButton("Looping: off");
    loopingButton.setActionCommand("Looping Button");
    buttonPane.add(loopingButton);

    restartButton = new JButton("Restart");
    restartButton.setActionCommand("Restart Button");
    buttonPane.add(restartButton);


    pauseButton = new JButton("Pause");
    pauseButton.setActionCommand("Pause Button");
    buttonPane.add(pauseButton);
    playButton = new JButton("Play");
    playButton.setActionCommand("Resume Button");
    buttonPane.add(playButton);
    buttonPane.setBackground(Color.LIGHT_GRAY);

    buttonPane.add(speedLabel);
    buttonPane.add(speedTextField);
    speedTextField.setMaximumSize(new Dimension(5, speedTextField.getPreferredSize().height));
    speedTextField.setColumns(2);

    animationSlider = new JSlider(0, getBiggestTick(), ticksPerSecond);
    buttonPane.add(animationSlider);
    //  animationSlider.setMajorTickSpacing(10);
    //  animationSlider.setMinorTickSpacing(1);
    //  animationSlider.setPaintTicks(true);
    //  animationSlider.setPaintLabels(true);
  }

  private int getBiggestTick() {
    biggestTick = 0;
    for (String key : readOnlyModel.getAnimationList().keySet()) {
      ArrayList<ISynchronisedActionSet> tempList = readOnlyModel.getAnimationList().get(key);
      if (tempList.size() > 0) {
        int nextTick = tempList.get(tempList.size() - 1).getEndTick();
        if (nextTick > biggestTick) {
          biggestTick = nextTick;
        }
      }
    }
    return biggestTick;
  }


  /**
   * Passes the features of the controller of type {@link IControllerFeatures} to the view. Allows
   * the view to retrieve these features from the controller to execute the proper reaction.
   *
   * @param features the possible actions that the controller supports, of type {@link
   *                 IControllerFeatures}
   */
  @Override
  public void addFeatureListeners(IControllerFeatures features) {
    exitButton.addActionListener(evt -> features.exitProgram());
    restartButton.addActionListener(evt -> this.restartButton(features));
    playButton.addActionListener(evt -> this.playButton());
    pauseButton.addActionListener(evt -> this.pauseButton());
    loopingButton.addActionListener(evt -> this.loopingButton());
    animationSlider.addChangeListener(this::animationSliderAction);
    commitButton.addActionListener(evt -> this.commitButton(features));
    shapeComboBox.addActionListener(evt -> this.comboBoxAction(shapeComboBox.getSelectedIndex()));
    speedTextField.addActionListener(evt -> {
      if (!speedTextField.getText().isBlank()) {
        setSpeedAndErase();
      }
    });
    speedTextField.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        // intentionally left blank
      }

      @Override
      public void focusLost(FocusEvent e) {
        setSpeedAndErase();
      }
    });
  }

  /**
   * Sets the speed and erases the text field responsible for setting the speed. Will set the speed
   * to 1 if given a negative number. Max speed is 99.
   */
  private void setSpeedAndErase() {
    if (!speedTextField.getText().isBlank()) {
      int s;
      try {
        s = Integer.parseInt(speedTextField.getText());
        if (s < 1) {
          s = 1;
        } else if (s > 5) {
          s = 5;
        }
        graphics.setTickSpeed(s);
      } catch (NumberFormatException nfe) {
        return;
      }
      speedTextField.setText("");
      speedLabel.setText("Speed (1-5): " + s);
    }
  }

  /**
   * Sets the inputted position to a string field in the class.
   *
   * @param text the inputted string
   */
  private void setPosition(String text) {
    this.positionText = text;
  }

  /**
   * Sets the inputted color to a string field in the class.
   *
   * @param text the inputted string
   */
  private void setDimension(String text) {
    this.dimensionText = text;
  }

  /**
   * Sets the inputted color to a string field in the class.
   *
   * @param text the inputted string
   */
  private void setColor(String text) {
    this.colorText = text;
  }

  /**
   * Sets the name to a string field in the class.
   *
   * @param text the inputted string
   */
  private void setShapeName(String text) {
    this.shapeText = text;
  }

  /**
   * Sets the ticks amount to a string value-holder class field.
   *
   * @param text the inputted string
   */
  private void setTicksText(String text) {
    this.ticksText = text;
  }

  /**
   * Gives the options for the user to choose from.
   *
   * @param selectedIndex the index of the box that the user selected
   */
  private void comboBoxAction(int selectedIndex) {
    StringBuilder sb = new StringBuilder();
    this.shapeNameTextField.setText("");
    this.dimensionTextField.setText("");
    this.positionTextField.setText("");
    this.colorTextField.setText("");
    this.ticksTextField.setText("");
    colorTextField.setVisible(false);
    positionTextField.setVisible(false);
    dimensionTextField.setVisible(false);
    ticksTextField.setVisible(false);
    shapeNameTextLabel.setVisible(false);
    colorTextLabel.setVisible(false);
    positionTextLabel.setVisible(false);
    dimensionTextLabel.setVisible(false);
    ticksTextLabel.setVisible(false);
    shapeNameTextLabel.setVisible(false);
    shapeNameTextField.setVisible(false);
    switch (selectedIndex) {
      case (1):
        sb.append("Selected: Add rectangle");
        shapeNameTextLabel.setVisible(true);
        shapeNameTextField.setVisible(true);
        break;
      case (2):
        sb.append("Selected: Add ellipse");
        shapeNameTextLabel.setVisible(true);
        shapeNameTextField.setVisible(true);

        break;
      case (3):
        sb.append("Selected: Add keyframe");
        shapeNameTextLabel.setVisible(true);
        shapeNameTextField.setVisible(true);
        colorTextLabel.setVisible(true);
        colorTextField.setVisible(true);
        positionTextLabel.setVisible(true);
        positionTextField.setVisible(true);
        dimensionTextLabel.setVisible(true);
        dimensionTextField.setVisible(true);
        ticksTextLabel.setVisible(true);
        ticksTextField.setVisible(true);
        break;
      case (4):
        shapeNameTextLabel.setVisible(true);
        shapeNameTextField.setVisible(true);
        sb.append("Selected: Edit keyframe");
        break;
      case (5):
        sb.append("Selected: Remove keyframe");
        shapeNameTextLabel.setVisible(true);
        shapeNameTextField.setVisible(true);
        ticksTextLabel.setVisible(true);
        ticksTextField.setVisible(true);
        break;
      case (6):
        sb.append("Selected: Remove shape");
        shapeNameTextLabel.setVisible(true);
        shapeNameTextField.setVisible(true);
        break;
      default:
        sb.append("Selected: None");
    }
    this.infoTextLabel.setText(sb.toString());
  }

  /**
   * Changes the speed of the animation.
   *
   * @param e a given ChangeEvent.
   */
  private void animationSliderAction(ChangeEvent e) {
    graphics.play(false);
    JSlider source = (JSlider) e.getSource();
    graphics.setCurrentTick(source.getValue());
    playButton.setForeground(Color.blue);
    pauseButton.setForeground(Color.black);
      if (!source.getValueIsAdjusting()) {
            graphics.play(true);
          }

  }

  /**
   * Commits the changes that are made in the editable view.
   *
   * @param features the features of the controller that are call-able
   */
  private void commitButton(IControllerFeatures features) {
    this.setTicksText(ticksTextField.getText());
    this.setShapeName(shapeNameTextField.getText());
    this.setPosition(positionTextField.getText());
    this.setDimension(dimensionTextField.getText());
    this.setColor(colorTextField.getText());
    cs3500.easyanimator.model.shapes.Color tempC;
    cs3500.easyanimator.model.shapes.Dimension tempD;
    cs3500.easyanimator.model.shapes.Posn tempP;
    int startTick;
    int endTick;
    String[] c = colorText.split(" ");
    String[] p = positionText.split(" ");
    String[] d = dimensionText.split(" ");
    String[] t = ticksText.split(" ");
    try {
      if (c.length >= 5) {
        tempC = new cs3500.easyanimator.model.shapes.Color(Integer.parseInt(c[0]),
            Integer.parseInt(c[1]), Integer.parseInt(c[2]));
      }
      if (p.length >= 3) {
        tempP = new cs3500.easyanimator.model.shapes.Posn(Integer.parseInt(p[0]),
            Integer.parseInt(p[1]));
      }
      if (d.length >= 3) {
        tempD = new cs3500.easyanimator.model.shapes.Dimension(Integer.parseInt(d[0]),
            Integer.parseInt(d[1]));
      }
    } catch (NumberFormatException nfe) {
      error("Could not parse numbers!");
      return;
    }
    if (shapeText.isEmpty()) {
      error("Shape name is empty!");
      return;
    }
    switch (this.shapeComboBox.getSelectedIndex()) {
      case (1):
        try {
          features.createShapeWithoutInstance(shapeText, "rectangle");
        } catch (IllegalArgumentException ia) {
          error("Shape name is taken!");
          return;
        }
        break;
      case (2):
        try {
          features.createShapeWithoutInstance(shapeText, "ellipse");
        } catch (IllegalArgumentException ia) {
          error("Shape name is taken!");
          return;
        }
        break;
      case (3):
        if (dimensionText.isEmpty() || positionText.isEmpty() || colorText.isEmpty()) {
          error("Cannot have empty field.");
        }
        try {
          startTick = Integer.parseInt(t[0]);
          endTick = Integer.parseInt(t[0]);
        } catch (NumberFormatException nfe) {
          error("Cannot read ticks");
          return;
        }
        break;
      case (4): // edit keyframe
        error("depreciated action");
        return;
      case (5):
        if (t.length < 3) {
          error("Need ticks!");
          return;
        }
        try {
          startTick = Integer.parseInt(t[0]);
          endTick = Integer.parseInt(t[0]);
        } catch (NumberFormatException nfe) {
          error("Cannot read ticks");
          return;
        }
        features.remove(shapeText, startTick, endTick);
        try {
          startTick = Integer.parseInt(t[0]);
          endTick = Integer.parseInt(t[0]);
        } catch (NumberFormatException nfe) {
          error("Cannot read ticks");
          return;
        }

        break;
      case (6):
        features.removeShape(shapeText);
        break;
      default:
        break;
    }
    this.infoTextLabel.setText("Committed!");
    this.readOnlyModel = features.updateReadOnly();
    this.infoTextLabel.setForeground(Color.green);
    graphics.updateReadOnly(features.updateReadOnly());
  }

  /**
   * Changes a JLabel to inform the user that one of their fields is empty.
   *
   * @param message the message of the error you would like to convey
   */
  private void error(String message) {
    this.infoTextLabel.setForeground(Color.red);
    this.infoTextLabel.setText(message);
  }

  /**
   * Loops the animation indefinitely.
   */
  private void loopingButton() {
    if (graphics.isLooping()) {
      loopingButton.setText("looping: off");
      graphics.setLooping(false);
      loopingButton.setForeground(Color.black);
    } else {
      loopingButton.setText("looping: on");
      graphics.setLooping(true);
      loopingButton.setForeground(Color.blue);

    }
  }

  /**
   * Resets the fields to repaint.
   */
  private void restartButton(IControllerFeatures f) {
    this.playButton.setForeground(Color.blue);
    this.pauseButton.setForeground(Color.black);
    this.isPlaying = false;
    graphics.play(false);
    f.updateReadOnly();
    graphics.resetFields();
  }

  /**
   * Resumes the animation if it was paused. If it was not paused then does not change anything.
   */
  private void playButton() {
    this.playButton.setForeground(Color.blue);
    this.pauseButton.setForeground(Color.black);
    this.isPlaying = true;
    graphics.play(true);

  }

  /**
   * Pauses the animation.
   */
  private void pauseButton() {
    this.playButton.setForeground(Color.black);
    this.pauseButton.setForeground(Color.blue);
    this.isPlaying = false;
    graphics.play(false);
  }

  /**
   * Creates the view's visuals that is intended to be made as per the implementation and type of
   * view.
   */
  @Override
  public void showView() {
    setVisible(true);
  }

  @Override
  public void updateTicks(int ticks) {
    this.currentTick = ticks;
    animationSlider.setValue(ticks);
  }

  @Override
  public void triggerPlayButtonBlue() {
    this.playButton.setForeground(Color.blue);
    this.pauseButton.setForeground(Color.black);
  }

  @Override
  public void triggerPauseButtonBlue() {
    this.playButton.setForeground(Color.black);
    this.pauseButton.setForeground(Color.blue);
  }

}