import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import cs3500.easyanimator.controller.ControllerImpl;
import cs3500.easyanimator.controller.IControllerFeatures;
import cs3500.easyanimator.model.AnimationModelImpl;
import cs3500.easyanimator.model.ReadOnlyModelImpl;
import cs3500.easyanimator.model.actions.ChangeColor;
import cs3500.easyanimator.model.actions.ChangeDimension;
import cs3500.easyanimator.model.actions.ChangePosition;
import cs3500.easyanimator.model.actions.IActionCommand;
import cs3500.easyanimator.model.actions.ISynchronisedActionSet;
import cs3500.easyanimator.model.actions.SynchronizedActionSetImpl;
import cs3500.easyanimator.model.shapes.Color;
import cs3500.easyanimator.model.shapes.Dimension;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.model.shapes.Oval;
import cs3500.easyanimator.model.shapes.Posn;
import cs3500.easyanimator.model.shapes.Rectangle;
import cs3500.easyanimator.model.shapes.Triangle;
import cs3500.easyanimator.view.ViewImplTextual;
import java.util.ArrayList;
import org.junit.Test;

/**
 * Represents tests for the AnimationModel and all associated classes.
 */

public class TestAnimationModelImpl {

  //Colors
  Color blue = new Color(1, 2, 3);
  Color red = new Color(20, 0, 0);
  Color yellow = new Color(2, 2, 2);

  //Dimensions
  Dimension big = new Dimension(7, 7);
  Dimension small = new Dimension(1, 1);
  Dimension medium = new Dimension(4, 4);

  //Posn
  Posn left = new Posn(2, 2);
  Posn right = new Posn(10, 10);
  Posn low = new Posn(10, 0);
  Posn high = new Posn(0, 10);

  //Shapes
  Oval oval = new Oval(left, big, blue);
  Rectangle rect = new Rectangle(right, small, red);
  Triangle tri = new Triangle(low, medium, yellow);

  ArrayList<IActionCommand> listOfAnimationCommand = new ArrayList<>();
  IActionCommand move = new ChangePosition(1, 10, 5);
  IActionCommand size = new ChangeDimension(10, 1, 5);
  IActionCommand color = new ChangeColor(3, 3, 3, 5);

  AnimationModelImpl model = new AnimationModelImpl();

  @Test(expected = IllegalArgumentException.class)
  public void testAddBad() {
    model.createShape("oval", oval);
    model.createShape("rect", rect);
    model.createShape("tri", tri);

    model.add("rect", -1, 5, new ChangePosition(10, 10, 5));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddBad2() {
    model.createShape("oval", oval);
    model.createShape("rect", rect);
    model.createShape("tri", tri);

    model.add("rect", 0, 5, new ChangePosition(10, 10, 5));
    model.add("rect", 0, 5, new ChangePosition(10, 10, 5));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddBad3() {

    model.add("rect", 0, 5, new ChangePosition(10, 10, 5));
    model.add("rect", 0, 5, new ChangePosition(10, 10, 5));
  }


  @Test
  public void clear() {
    model.createShape("oval", oval);
    model.createShape("rect", rect);
    model.createShape("tri", tri);

    ArrayList<String> keys = new ArrayList<>();
    keys.add("oval");
    keys.add("rect");
    keys.add("tri");
    assertEquals(keys, model.getShapeKeys());

    model.clear();

    assertEquals(new ArrayList<String>(), model.getShapeKeys());
  }

  @Test
  public void getStartTick() {
    listOfAnimationCommand.add(color);
    listOfAnimationCommand.add(size);
    listOfAnimationCommand.add(move);
    ISynchronisedActionSet ai = new SynchronizedActionSetImpl(0, 5, listOfAnimationCommand);
    assertEquals(0, ai.getStartTick());
  }

  @Test
  public void getStartTick2() {
    listOfAnimationCommand.add(color);
    listOfAnimationCommand.add(size);
    listOfAnimationCommand.add(move);
    ISynchronisedActionSet ai = new SynchronizedActionSetImpl(100, 102, listOfAnimationCommand);
    assertEquals(100, ai.getStartTick());
  }

  @Test
  public void getEndTick() {
    listOfAnimationCommand.add(color);
    listOfAnimationCommand.add(size);
    listOfAnimationCommand.add(move);
    ISynchronisedActionSet ai = new SynchronizedActionSetImpl(0, 5000, listOfAnimationCommand);
    assertEquals(5000, ai.getEndTick());
  }

  @Test
  public void applyAnimation() {
    listOfAnimationCommand.add(color);
    listOfAnimationCommand.add(size);
    listOfAnimationCommand.add(move);
    ISynchronisedActionSet ai = new SynchronizedActionSetImpl(0, 5000, listOfAnimationCommand);
    assertEquals("10 10 1 1 20 0 0", rect.toString());
    ai.applyAnimation(rect);
    assertEquals("8 10 2 1 17 0 0", rect.toString());
  }

  @Test
  public void getCommandList() {
    listOfAnimationCommand.add(color);
    listOfAnimationCommand.add(size);
    listOfAnimationCommand.add(move);
    ISynchronisedActionSet ai = new SynchronizedActionSetImpl(0, 5000, listOfAnimationCommand);
    assertEquals(listOfAnimationCommand, ai.getCommandList());
  }

  @Test
  public void getCommandList2() {
    listOfAnimationCommand.add(color);
    ISynchronisedActionSet ai = new SynchronizedActionSetImpl(0, 5000, listOfAnimationCommand);
    assertEquals(listOfAnimationCommand, ai.getCommandList());
  }

  @Test
  public void getCommandList3() {
    ISynchronisedActionSet ai = new SynchronizedActionSetImpl(0, 5000, listOfAnimationCommand);
    assertEquals(listOfAnimationCommand, ai.getCommandList());
  }

  @Test
  public void testRemoveCommand() {
    listOfAnimationCommand.add(color);
    ISynchronisedActionSet ai = new SynchronizedActionSetImpl(0, 5000, listOfAnimationCommand);
    ai.removeCommand(0);
    assertEquals(new ArrayList<>(), ai.getCommandList());
  }

  @Test
  public void containsInstanceOf() {
    listOfAnimationCommand.add(color);
    listOfAnimationCommand.add(size);
    listOfAnimationCommand.add(move);
    ISynchronisedActionSet ai = new SynchronizedActionSetImpl(0, 5000, listOfAnimationCommand);
    assertTrue(ai.containsInstanceOf(move));
    assertTrue(ai.containsInstanceOf(color));
    assertTrue(ai.containsInstanceOf(size));
  }

  @Test
  public void containsInstanceOfFalse() {
    listOfAnimationCommand.add(color);
    listOfAnimationCommand.add(size);
    ISynchronisedActionSet ai = new SynchronizedActionSetImpl(0, 5000, listOfAnimationCommand);
    assertFalse(ai.containsInstanceOf(move));
  }

  @Test
  public void containsInstanceOfEmpty() {
    ISynchronisedActionSet ai = new SynchronizedActionSetImpl(0, 5000, listOfAnimationCommand);
    assertFalse(ai.containsInstanceOf(move));
  }

  @Test
  public void containsInstanceOfNull() {
    ISynchronisedActionSet ai = new SynchronizedActionSetImpl(0, 5000, listOfAnimationCommand);
    assertFalse(ai.containsInstanceOf(null));
  }

  @Test(expected = IllegalArgumentException.class)
  public void animationConstructorBad1() {
    ISynchronisedActionSet ani = new SynchronizedActionSetImpl(-1, -1, listOfAnimationCommand);
  }

  @Test(expected = IllegalArgumentException.class)
  public void animationConstructorBad() {
    ISynchronisedActionSet ani = new SynchronizedActionSetImpl(1, 0, listOfAnimationCommand);
  }

  // In SynchronizedActionSetImpl
  @Test
  public void compareTo() {
    ISynchronisedActionSet ani1 = new SynchronizedActionSetImpl(1, 6, listOfAnimationCommand);
    ISynchronisedActionSet ani2 = new SynchronizedActionSetImpl(4, 6, listOfAnimationCommand);
    assertEquals(-3, ani1.compareTo(ani2));
  }

  @Test
  public void compareTo2() {
    ISynchronisedActionSet ani1 = new SynchronizedActionSetImpl(1, 6, listOfAnimationCommand);
    ISynchronisedActionSet ani2 = new SynchronizedActionSetImpl(5, 8, listOfAnimationCommand);
    assertEquals(-4, ani1.compareTo(ani2));
  }

  @Test
  public void compareTo3() {
    ISynchronisedActionSet ani1 = new SynchronizedActionSetImpl(9, 10, listOfAnimationCommand);
    ISynchronisedActionSet ani2 = new SynchronizedActionSetImpl(5, 8, listOfAnimationCommand);
    assertEquals(4, ani1.compareTo(ani2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void ishapeConstructor4() {
    IShape shape = new Triangle(new Posn(3, 2),
        new Dimension(-1, 2),
        new Color(3, 2, 3));
  }

  @Test(expected = IllegalArgumentException.class)
  public void ishapeConstructor5() {
    IShape shape = new Triangle(new Posn(3, 2),
        new Dimension(1, 2),
        new Color(3, -3, 3));
  }

  @Test
  public void toStringTest() {
    IShape shape = new Oval(new Posn(3, 2),
        new Dimension(1, 2),
        new Color(3, 2, 3));
    assertEquals("3 2 1 2 3 2 3", shape.toString());
  }

  @Test
  public void toStringTest2() {
    IShape shape = new Rectangle(new Posn(3, 2),
        new Dimension(1, 2),
        new Color(3, 2, 3));
    assertEquals("3 2 1 2 3 2 3", shape.toString());
  }

  @Test
  public void toStringTest3() {
    IShape shape = new Triangle(new Posn(3, 2),
        new Dimension(1, 2),
        new Color(3, 2, 1));
    assertEquals("3 2 1 2 3 2 1", shape.toString());
  }

  @Test
  public void getDimension() {
    IShape shape = new Triangle(new Posn(3, 2),
        new Dimension(1, 2),
        new Color(3, 2, 1));
    assertEquals(new Dimension(1, 2), shape.getDimension());
  }

  @Test
  public void getColor() {
    IShape shape = new Triangle(new Posn(3, 2),
        new Dimension(1, 2),
        new Color(3, 2, 1));
    assertEquals(new Color(3, 2, 1), shape.getColor());
  }

  @Test
  public void getPosn() {
    IShape shape = new Triangle(new Posn(3, 2),
        new Dimension(1, 2),
        new Color(3, 2, 1));
    assertEquals(new Posn(3, 2), shape.getPosn());
  }

  @Test
  public void getVisibility() {
    IShape shape = new Triangle(new Posn(3, 2),
        new Dimension(1, 2),
        new Color(3, 2, 1));
    assertTrue(shape.getVisibility());
  }

  @Test
  public void mutateloc2() {
    model.createShape("rect", rect);
    assertEquals("10 10 1 1 20 0 0", rect.toString());
    color.mutate(rect);
    assertEquals("10 10 1 1 17 0 0", rect.toString());
  }

  @Test
  public void mutateloc3() {
    model.createShape("rect", rect);
    assertEquals("10 10 1 1 20 0 0", rect.toString());
    size.mutate(rect);
    assertEquals("10 10 2 1 20 0 0", rect.toString());
  }

  @Test
  public void mutatesize() {
    model.createShape("oval", oval);
    assertEquals("2 2 7 7 1 2 3", oval.toString());
    size.mutate(oval);
    assertEquals("2 2 7 6 1 2 3", oval.toString());
  }

  @Test
  public void mutatesize2() {
    model.createShape("oval", tri);
    assertEquals("10 0 4 4 2 2 2", tri.toString());
    size.mutate(tri);
    size.mutate(tri);
    color.mutate(tri);
    move.mutate(tri);
    assertEquals("8 2 6 4 2 2 2", tri.toString());
  }

  @Test
  public void testRemove() {
    model.createShape("rect", rect);
    model.createShape("tri", tri);
    StringBuilder sb = new StringBuilder();
    ViewImplTextual v = new ViewImplTextual(sb, new ReadOnlyModelImpl(model));
    IControllerFeatures controller = new ControllerImpl(model, v);

    //changing the color of rectangle
    model.add("rect", 0, 5, new ChangeColor(1, 3, 3, 5));
    controller.runAnimator();
    assertEquals("Columns:\n"
        + "t x y w h r g b      t x y w h r g b\n"
        + "shape rect rectangle\n"
        + "0 10 10 1 1 20 0 0      5 10 10 1 1 1 3 3\n"
        + "shape tri triangle", sb.toString());

    sb = new StringBuilder();
    //changing the color of rectangle again
    model.add("rect", 5, 10, new ChangeColor(7, 8, 15, 5));
    controller.runAnimator();
    assertEquals("", sb.toString());

    sb = new StringBuilder();
    controller.runAnimator();

    assertEquals("", sb.toString());

    assertEquals(2, model.getAnimationList().get("rect").size());

    model.remove("rect", 5, 10);

    assertEquals(1, model.getAnimationList().get("rect").size());
  }

  @Test
  public void testRemove1() {
    model.createShape("rect", rect);
    model.createShape("tri", tri);

    //changing the color of rectangle
    model.add("rect", 0, 5, new ChangeColor(1, 3, 3, 5));

    assertEquals(5, model.getAnimationList().get("rect").get(0).getEndTick());

    //changing the color of rectangle again
    model.add("rect", 5, 10, new ChangeColor(7, 8, 15, 5));

    assertEquals(2, model.getAnimationList().get("rect").size());

    model.remove("rect", 5, 10);

    assertEquals(1, model.getAnimationList().get("rect").size());
  }

}
