package cs3500.easyanimator.controller;

import cs3500.easyanimator.model.IAnimationModel;

import cs3500.easyanimator.view.IView;

/**
 * This represents an instance of our controller. It handles both the view and model to delegate to
 * their respective operations. Constructor takes in both a model of type {@link IAnimationModel}
 * and view of type {@link IView}.
 */
public class ControllerImpl implements IControllerFeatures {

  private final IAnimationModel model;
  private final IView view;

  /**
   * A constructor for our controller. Takes in an animation model of {@link IAnimationModel} and a
   * view of type {@link IView}.
   *
   * @param m The model that will be handled.
   * @param v The view type to output.
   */
  public ControllerImpl(IAnimationModel m, IView v) {
    this.model = m;
    this.view = v;
    //provide view with all the callbacks
    v.addControllerFeatures(this);
  }

  @Override
  public void exitProgram() {
    System.exit(0);
  }


  @Override
  public void runAnimator() {
    view.showView();
  }
}
