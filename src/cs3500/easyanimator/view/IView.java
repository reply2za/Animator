package cs3500.easyanimator.view;

import cs3500.easyanimator.controller.IControllerFeatures;

/**
 * The interface for our view class. Creates the visual representation of the animator to the
 * client. Can support 'read only' models for better data retrieval.
 */
public interface IView {

  /**
   * Passes the features of the controller of type {@link IControllerFeatures} to the view. Allows
   * the view to retrieve these features from the controller to execute the proper reaction.
   *
   * @param features the possible actions that the controller supports, of type {@link
   *                 IControllerFeatures}
   */
  void addControllerFeatures(IControllerFeatures features);

  /**
   * Creates the view's visuals that is intended to be made as per the implementation and type of
   * view.
   */
  void showView();
}

