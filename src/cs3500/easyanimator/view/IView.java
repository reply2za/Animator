package cs3500.easyanimator.view;

import cs3500.easyanimator.controller.IControllerFeatures;

/**
 * The interface for our view class
 */
public interface IView {


  /**
   * Passes the features of the controller of type {@link IControllerFeatures} to the view. Allows
   * the view to retrieve these features from the controller to execute the proper reaction.
   *
   * @param features the possible actions that the controller supports, of type {@link
   *                 IControllerFeatures}
   */
  void addFeatures(IControllerFeatures features);
}

