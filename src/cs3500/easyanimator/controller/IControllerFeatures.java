package cs3500.easyanimator.controller;

import cs3500.easyanimator.model.actions.ISynchronisedActionSet;
import cs3500.easyanimator.model.shapes.IShape;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Represents all of the different actions and controls that a controller supports. As such, the
 * view can call on these specific 'features' of the controller if given an instance of this. Is the
 * controller of the program and delegates between the model and the view.
 */
public interface IControllerFeatures {

  /**
   * Ends the program.
   */
  void exitProgram();

  /**
   * Runs the animator and the overall program. Creates the views according to the type of view
   * attached to it.
   */
  void runAnimator();
}



