package cs3500.easyanimator.controller;

import cs3500.easyanimator.model.IAnimationModel;
import cs3500.easyanimator.view.IView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControllerImpl implements IControllerFeatures {

    private IAnimationModel model;
    private IView view;


  public ControllerImpl(IAnimationModel m, IView v) {
      model = m;
      view = v;
    //provide view with all the callbacks
      view.addFeatures(this);
    }

  @Override
  public void createAnimation() {

  }
}
