package cs3500.easyanimator.view;

import cs3500.easyanimator.controller.ControllerImpl;

/**
 * A textual implementation of a view.
 */
public class ViewImplTextual implements IView{

  Appendable output;

  ViewImplTextual(Appendable output) {
    this.output = output;
  }



  /**
   * @param controller
   */
  @Override
  public void addFeatures(ControllerImpl controller) {

  }
}
