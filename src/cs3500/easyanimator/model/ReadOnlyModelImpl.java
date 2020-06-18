package cs3500.easyanimator.model;

import cs3500.easyanimator.model.actions.IActionCommand;
import cs3500.easyanimator.model.actions.ISynchronisedActionSet;
import cs3500.easyanimator.model.shapes.IShape;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * An instance of a 'read-only' model. Implements {@link IReadOnlyModel}. Given a model into this
 * class's constructor as its only argument, packages the model as read-only and prevents
 * manipulations.
 */
public class ReadOnlyModelImpl implements IReadOnlyModel {

  IAnimationModel m;


  public ReadOnlyModelImpl(IAnimationModel m) {
    if (m instanceof ReadOnlyModelImpl) {
      throw new IllegalArgumentException("Cannot pass a read-only implementation to this class.");
    }
    this.m = m;
  }

  @Override
  public void add(String key, int start, int end, IActionCommand ac)
      throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation not supported in this readonly.");
  }

  @Override
  public void clear() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation not supported in this readonly.");
  }


  @Override
  public void remove(String key, int startTick, int endTick) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation not supported in this readonly.");
  }


  @Override
  public void removeActionCommand(String key, int startTick, int endTick, IActionCommand ac)
      throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation not supported in this readonly.");
  }

  @Override
  public void remove(String key, int startTick, int endTick, IActionCommand ac)
      throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation not supported in this readonly.");
  }

  @Override
  public void createShape(String name, IShape shape) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation not supported in this readonly.");
  }

  @Override
  public void createShapeWithoutInstance(String name, String type)
      throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation not supported in this readonly.");
  }

  @Override
  public void removeShape(String key) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation not supported in this readonly.");
  }

  @Override
  public ArrayList<String> getShapeKeys() {
    return m.getShapeKeys();
  }

  @Override
  public IShape getShape(String key) {
    return m.getShape(key);
  }

  @Override
  public void changeCanvas(int x, int y, int width, int height)
      throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation not supported in this readonly.");
  }


  @Override
  public void addMotions(String name, int t1, int x1, int y1, int w1, int h1, int r1, int g1,
      int b1, int t2, int x2, int y2, int w2, int h2, int r2, int g2, int b2)
      throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation not supported in this readonly.");
  }


  @Override
  public List<Integer> getTicks(String name) {
    return m.getTicks(name);
  }

  @Override
  public LinkedHashMap<String, ArrayList<ISynchronisedActionSet>> getAnimationList() {
    return m.getAnimationList();
  }

  @Override
  public LinkedHashMap<String, IShape> getShapeIdentifier() {
    return m.getShapeIdentifier();
  }

  @Override
  public int getCanvasWidth() {
    return m.getCanvasWidth();
  }

  @Override
  public int getCanvasHeight() {
    return m.getCanvasHeight();
  }

  /**
   * Returns the canvas leftmost x value for this model.
   *
   * @return returns the leftmost x value of the canvas as an int.
   */
  @Override
  public int getCanvasX() {
    return m.getCanvasX();
  }

  /**
   * Returns the canvas topmost y value for this model.
   *
   * @return the topmost y value canvas height as an int.
   */
  @Override
  public int getCanvasY() {
    return m.getCanvasY();
  }
}
