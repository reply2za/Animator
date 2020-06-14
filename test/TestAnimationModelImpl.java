import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import cs3500.easyanimator.model.actions.IActionCommand;
import cs3500.easyanimator.model.actions.ISynchronisedActionSet;
import cs3500.easyanimator.model.actions.SynchronizedActionSetImpl;
import cs3500.easyanimator.model.AnimationModelImpl;
import cs3500.easyanimator.model.actions.ChangeColor;
import cs3500.easyanimator.model.actions.ChangeDimension;
import cs3500.easyanimator.model.actions.ChangePosition;
import cs3500.easyanimator.model.shapes.Color;
import cs3500.easyanimator.model.shapes.Dimension;
import cs3500.easyanimator.model.IAnimationModel;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.model.shapes.Posn;
import cs3500.easyanimator.model.shapes.Oval;
import cs3500.easyanimator.model.shapes.Rectangle;
import cs3500.easyanimator.model.shapes.Triangle;
import java.util.ArrayList;
import java.util.HashMap;
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

  //if the input is null
  @Test(expected = IllegalArgumentException.class)
  public void badAnimationModelConstructor() {
    IAnimationModel model1 = new AnimationModelImpl(null);
  }

  @Test
  public void testAdd() {
    model.createShape("oval", oval);
    model.createShape("rect", rect);
    model.createShape("tri", tri);

    model.add("rect", 0, 5, new ChangePosition(1, 10, 5));
    model.add("rect", 0, 5, new ChangeDimension(10, 1, 5));
    model.add("rect", 5, 10, new ChangeDimension(5, 2, 5));
    model.add("rect", 3, 8, new ChangeColor(3, 3, 3, 5));

    model.add("tri", 0, 5, new ChangePosition(10, 10, 5));
    model.add("tri", 0, 5, new ChangeDimension(10, 10, 5));
    model.add("tri", 5, 10, new ChangeDimension(1, 1, 5));

    assertEquals("Columns:\n"
        + "t x y w h r g b      t x y w h r g b\n"
        + "shape rect rectangle\n"
        + "0 10 10 1 1 20 0 0      3 5 10 6 1 20 0 0\n"
        + "3 5 10 6 1 20 0 0      5 1 10 10 1 14 0 0\n"
        + "5 1 10 10 1 14 0 0      8 1 10 7 1 3 3 3\n"
        + "8 1 10 7 1 3 3 3      10 1 10 5 2 3 3 3\n"
        + "shape oval oval\n"
        + "shape tri triangle\n"
        + "0 10 0 4 4 2 2 2      5 10 10 10 10 2 2 2\n"
        + "5 10 10 10 10 2 2 2      10 10 10 1 1 2 2 2", model.getAnimationLog());
  }

  @Test
  public void testAddSimpl() {
    model.createShape("oval", oval);
    model.createShape("rect", rect);
    model.createShape("tri", tri);

    model.add("rect", 0, 5, new ChangePosition(10, 10, 5));
    model.add("rect", 0, 5, new ChangeDimension(10, 10, 5));
    model.add("rect", 5, 10, new ChangeDimension(100, 100, 5));

    assertEquals("Columns:\n"
        + "t x y w h r g b      t x y w h r g b\n"
        + "shape rect rectangle\n"
        + "0 10 10 1 1 20 0 0      5 10 10 10 10 20 0 0\n"
        + "5 10 10 10 10 20 0 0      10 10 10 100 100 20 0 0\n"
        + "shape oval oval\n"
        + "shape tri triangle", model.getAnimationLog());
  }

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

    assertEquals("name , type\n"
        + "-------------\n"
        + "rect , rectangle\n"
        + "oval , oval\n"
        + "tri , triangle\n", model.getShapeKeys());

    model.clear();

    assertEquals("name , type\n"
        + "-------------\n", model.getShapeKeys());
  }

  @Test
  public void clear2() {
    model.createShape("rect", rect);
    model.add("rect", 0, 5, new ChangePosition(10, 10, 5));


    assertEquals("Columns:\n"
        + "t x y w h r g b      t x y w h r g b\n"
        + "shape rect rectangle\n"
        + "0 10 10 1 1 20 0 0      5 10 10 1 1 20 0 0", model.getAnimationLog());

    model.clear();

    assertEquals("Columns:\n"
        + "t x y w h r g b      t x y w h r g b", model.getAnimationLog());
  }


  @Test
  public void remove() {
    model.createShape("rect", rect);
    model.add("rect", 0, 5, new ChangePosition(10, 10, 5));

    assertEquals("Columns:\n"
        + "t x y w h r g b      t x y w h r g b\n"
        + "shape rect rectangle\n"
        + "0 10 10 1 1 20 0 0      5 10 10 1 1 20 0 0", model.getAnimationLog());

    model.removeShape("rect");

    assertEquals("Columns:\n"
        + "t x y w h r g b      t x y w h r g b", model.getAnimationLog());
  }

  @Test(expected = IllegalArgumentException.class)
  public void remove2() {
    model.createShape("rect", rect);
    model.add("rect", 0, 5, new ChangePosition(10, 10, 5));

    assertEquals("Columns:\n"
        + "t x y w h r g b      t x y w h r g b\n"
        + "shape rect rectangle\n"
        + "0 10 10 1 1 20 0 0      5 10 10 1 1 20 0 0", model.getAnimationLog());

    model.removeShape("vvf"); //shape key doesnt exist in model
  }

  @Test
  public void getAnimationLog() {
    model.createShape("oval", oval);
    model.createShape("rect", rect);
    model.createShape("tri", tri);

    model.add("rect", 0, 5, new ChangePosition(1, 10, 5));
    model.add("rect", 0, 5, new ChangeDimension(10, 1, 5));
    model.add("rect", 5, 10, new ChangeDimension(5, 2, 5));
    model.add("rect", 3, 8, new ChangeColor(3, 3, 3, 5));

    model.add("tri", 0, 5, new ChangePosition(10, 10, 5));
    model.add("tri", 0, 5, new ChangeDimension(10, 10, 5));
    model.add("tri", 5, 10, new ChangeDimension(1, 1, 5));

    assertEquals("Columns:\n"
        + "t x y w h r g b      t x y w h r g b\n"
        + "shape rect rectangle\n"
        + "0 10 10 1 1 20 0 0      3 5 10 6 1 20 0 0\n"
        + "3 5 10 6 1 20 0 0      5 1 10 10 1 14 0 0\n"
        + "5 1 10 10 1 14 0 0      8 1 10 7 1 3 3 3\n"
        + "8 1 10 7 1 3 3 3      10 1 10 5 2 3 3 3\n"
        + "shape oval oval\n"
        + "shape tri triangle\n"
        + "0 10 0 4 4 2 2 2      5 10 10 10 10 2 2 2\n"
        + "5 10 10 10 10 2 2 2      10 10 10 1 1 2 2 2", model.getAnimationLog());
  }

  @Test
  public void getAnimationLog2() {
    model.createShape("oval", oval);
    model.createShape("rect", rect);
    model.createShape("tri", tri);

    model.add("rect", 0, 5, new ChangePosition(1, 10, 5));
    model.add("rect", 0, 5, new ChangeDimension(10, 1, 5));
    model.add("rect", 5, 10, new ChangeDimension(5, 2, 5));
    model.add("rect", 3, 8, new ChangeColor(3, 3, 3, 5));

    model.add("oval", 0, 5, new ChangePosition(10, 10, 5));
    model.add("oval", 0, 5, new ChangeDimension(10, 10, 5));
    model.add("oval", 5, 10, new ChangeDimension(1, 1, 5));

    assertEquals("Columns:\n"
        + "t x y w h r g b      t x y w h r g b\n"
        + "shape rect rectangle\n"
        + "0 10 10 1 1 20 0 0      3 5 10 6 1 20 0 0\n"
        + "3 5 10 6 1 20 0 0      5 1 10 10 1 14 0 0\n"
        + "5 1 10 10 1 14 0 0      8 1 10 7 1 3 3 3\n"
        + "8 1 10 7 1 3 3 3      10 1 10 5 2 3 3 3\n"
        + "shape oval oval\n"
        + "0 2 2 7 7 1 2 3      5 10 10 10 10 1 2 3\n"
        + "5 10 10 10 10 1 2 3      10 10 10 1 1 1 2 3\n"
        + "shape tri triangle", model.getAnimationLog());
  }

  @Test
  public void getAnimationLog3Empty() {
    assertEquals("Columns:\n"
        + "t x y w h r g b      t x y w h r g b", model.getAnimationLog());
  }

  @Test
  public void getAnimationDescription() {
    model.createShape("oval", oval);
    model.createShape("rect", rect);
    model.createShape("tri", tri);

    model.add("rect", 0, 5, new ChangePosition(1, 10, 5));
    model.add("rect", 0, 5, new ChangeDimension(10, 1, 5));
    model.add("rect", 5, 10, new ChangeDimension(5, 2, 5));
    model.add("rect", 3, 8, new ChangeColor(3, 3, 3, 5));

    model.add("oval", 0, 5, new ChangePosition(10, 10, 5));
    model.add("oval", 0, 5, new ChangeDimension(10, 10, 5));
    model.add("oval", 5, 10, new ChangeDimension(1, 1, 5));
    assertEquals("Create rectangle named rect\n"
        + "Create oval named oval\n"
        + "Create triangle named tri\n"
        + "\n"
        + "From time 0 to time 3, rect moves from (10,10) to (5,10), "
        + "changes from 1x1 to 6x1, and stays red.\n"
        + "From time 3 to time 5, rect moves from (5,10) to (1,10), "
        + "changes from 6x1 to 10x1, and stays red.\n"
        + "From time 5 to time 8, rect stays put, changes from 10x1 to 7x1, and turns brown.\n"
        + "From time 8 to time 10, rect stays put, changes from 7x1 to 5x2, and stays brown.\n"
        + "\n"
        + "From time 0 to time 5, oval moves from (2,2) to (10,10), "
        + "changes from 7x7 to 10x10, and stays blue.\n"
        + "From time 5 to time 10, oval stays put, changes from "
        + "10x10 to 1x1, and stays blue.\n", model.getAnimationDescription());
  }

  @Test
  public void createShape() {
    model.createShape("oval", oval);
    model.createShape("rect", rect);
    model.createShape("tri", tri);

    assertEquals("name , type\n"
        + "-------------\n"
        + "rect , rectangle\n"
        + "oval , oval\n"
        + "tri , triangle\n", model.getShapeKeys());
  }

  @Test
  public void removeShape() {
    model.createShape("oval", oval);
    model.createShape("rect", rect);
    model.createShape("tri", tri);
    model.removeShape("tri");
    assertEquals("name , type\n"
        + "-------------\n"
        + "rect , rectangle\n"
        + "oval , oval\n", model.getShapeKeys());
  }


  //In ASynchronisedActionSet

  @Test(expected = IllegalArgumentException.class)
  public void getStartTickBad() {
    ISynchronisedActionSet ai = new SynchronizedActionSetImpl(0, 5, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getEndTickBad() {
    ISynchronisedActionSet ai = new SynchronizedActionSetImpl(0, -1, null);
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
    assertEquals("9 10 2 1 17 0 0", rect.toString());
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
    assertEquals(false, ai.containsInstanceOf(move));
  }

  @Test
  public void containsInstanceOfEmpty() {
    ISynchronisedActionSet ai = new SynchronizedActionSetImpl(0, 5000, listOfAnimationCommand);
    assertEquals(false, ai.containsInstanceOf(move));
  }

  @Test
  public void containsInstanceOfNull() {
    ISynchronisedActionSet ai = new SynchronizedActionSetImpl(0, 5000, listOfAnimationCommand);
    assertEquals(false, ai.containsInstanceOf(null));
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

  // In AShape
  @Test(expected = IllegalArgumentException.class)
  public void ishapeConstructor() {
    IShape shape = new Rectangle(null, new Dimension(3, 2), new Color(3, 2, 3));
  }

  @Test(expected = IllegalArgumentException.class)
  public void ishapeConstructor2() {
    IShape shape = new Triangle(new Posn(3, 2), null, new Color(3, 2, 3));
  }

  @Test(expected = IllegalArgumentException.class)
  public void ishapeConstructor3() {
    IShape shape = new Triangle(new Posn(3, 2), new Dimension(3, 2), null);
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
    assertEquals(true, shape.getVisibility());
  }

  // In Change Color, Change Size and Change location
  @Test
  public void mutateloc() {
    model.createShape("rect", tri);
    assertEquals("10 0 4 4 2 2 2", tri.toString());
    move.mutate(tri);
    assertEquals("9 2 4 4 2 2 2", tri.toString());
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
    assertEquals("9 2 6 4 2 2 2", tri.toString());
  }


  //-----------------------------------------------------------------------

  @Test
  public void testAttemptChangingColor() {
    model.createShape("rect", rect);
    model.createShape("tri", tri);

    //changing the color of rectangle
    model.add("rect", 0, 5, new ChangeColor(1, 3, 3, 5));

    //changing the color of rectangle again
    model.add("rect", 5, 10, new ChangeColor(7, 8, 15, 5));

    model.add("tri", 5, 10, new ChangeColor(9, 10, 10, 5));
    assertEquals("Columns:\n"
        + "t x y w h r g b      t x y w h r g b\n"
        + "shape rect rectangle\n"
        + "0 10 10 1 1 20 0 0      5 10 10 1 1 1 3 3\n"
        + "5 10 10 1 1 1 3 3      10 10 10 1 1 7 8 15\n"
        + "shape tri triangle\n"
        + "5 10 0 4 4 2 2 2      10 10 0 4 4 9 10 10", model.getAnimationLog());

  }

  @Test
  public void testAttemptOverlappingValidTicksSameShape() {
    model.createShape("rect", rect);

    //changing the color of rectangle
    model.add("rect", 0, 5, new ChangeColor(1, 3, 3, 5));

    //mutating a different field
    model.add("rect", 4, 7, new ChangePosition(1, 3, 5));

    //changing the color of rectangle again
    model.add("rect", 5, 10, new ChangeColor(7, 8, 15, 5));

    assertEquals("Columns:\n"
        + "t x y w h r g b      t x y w h r g b\n"
        + "shape rect rectangle\n"
        + "0 10 10 1 1 20 0 0      4 10 10 1 1 5 2 2\n"
        + "4 10 10 1 1 5 2 2      5 9 9 1 1 1 3 3\n"
        + "5 9 9 1 1 1 3 3      7 5 7 1 1 3 5 7\n"
        + "7 5 7 1 1 3 5 7      10 5 7 1 1 7 8 15", model.getAnimationLog());

  }

  @Test
  public void testAnimationDescription() {
    model.createShape("rect", rect);
    model.createShape("o", oval);

    //changing the color of rectangle
    model.add("rect", 0, 5, new ChangeColor(1, 3, 3, 5));

    //mutating a different field
    model.add("rect", 4, 7, new ChangePosition(1, 3, 5));

    //changing the color of rectangle again
    model.add("rect", 5, 10, new ChangeColor(7, 8, 15, 5));

    model.add("o", 5, 10, new ChangeDimension(1, 2, 5));

    // to test the description log
    assertEquals("Create rectangle named rect\n"
            + "Create oval named o\n"
            + "\n"
            + "From time 0 to time 4, rect stays put, stays size 1x1, and stays red.\n"
            + "From time 4 to time 5, rect moves from (10,10) to (9,9), stays size 1x1, "
            + "and turns green-ish blue-ish.\n"
            + "From time 5 to time 7, rect moves from (9,9) to (5,7), stays size 1x1, and "
            + "turns blue.\n"
            + "From time 7 to time 10, rect stays put, stays size 1x1, and stays blue.\n"
            + "\n"
            + "From time 5 to time 10, o stays put, changes from 7x7 to 1x2, and stays blue.",
        model.getAnimationDescription());

    // to make sure our values are correct
    assertEquals("Columns:\n"
        + "t x y w h r g b      t x y w h r g b\n"
        + "shape rect rectangle\n"
        + "0 5 7 1 1 7 8 15      4 5 7 1 1 7 8 15\n"
        + "4 5 7 1 1 7 8 15      5 3 5 1 1 7 8 15\n"
        + "5 3 5 1 1 7 8 15      7 1 3 1 1 7 8 15\n"
        + "7 1 3 1 1 7 8 15      10 1 3 1 1 7 8 15\n"
        + "shape o oval\n"
        + "5 2 2 1 2 1 2 3      10 2 2 1 2 1 2 3", model.getAnimationLog());

  }


  @Test
  public void testToStringOfFieldClasses() {
    Color c1 = new Color(1, 2, 3);
    Dimension s1 = new Dimension(1, 2);
    Posn p1 = new Posn(5, 5);

    // should print out spaces version of their respective fields
    assertEquals("1 2 3", c1.toString());
    assertEquals("1 2", s1.toString());
    assertEquals("5 5", p1.toString());
  }

  @Test
  public void testEqualsMetodsForClassFields() {
    Color c1 = new Color(1, 2, 3);
    Color c2 = new Color(1, 2, 3);
    Color c3 = new Color(1, 4, 3);
    Dimension d1 = new Dimension(1, 2);
    Dimension d2 = new Dimension(1, 2);
    Dimension d3 = new Dimension(7, 2);
    Posn p1 = new Posn(5, 5);
    Posn p2 = new Posn(5, 5);
    Posn p3 = new Posn(6, 5);

    // should be true
    assertTrue(c1.equals(c2));
    assertTrue(d1.equals(d2));
    assertTrue(p1.equals(p2));

    //should be false
    assertFalse(c2.equals(c3));
    assertFalse(d2.equals(d3));
    assertFalse(p2.equals(p3));
  }

  @Test
  public void testHashCodeOfFieldClasses() {
    Color c1 = new Color(1, 2, 3);
    Color c2 = new Color(1, 2, 3);
    Color c3 = new Color(1, 4, 3);
    Dimension d1 = new Dimension(1, 2);
    Dimension d2 = new Dimension(1, 2);
    Dimension d3 = new Dimension(7, 2);
    Posn p1 = new Posn(5, 5);
    Posn p2 = new Posn(5, 5);
    Posn p3 = new Posn(6, 5);

    // should be true
    assertEquals(c1.hashCode(), c2.hashCode());
    assertEquals(d1.hashCode(), d2.hashCode());
    assertEquals(p1.hashCode(), p2.hashCode());

    //should be false
    assertNotEquals(c2.hashCode(), c3.hashCode());
    assertNotEquals(d2.hashCode(), d3.hashCode());
    assertNotEquals(p2.hashCode(), p3.hashCode());
  }

  @Test
  public void testAnimationModelImplConstructor() {
    // should not cause any errors when instantiating
    IAnimationModel m = new AnimationModelImpl();
    HashMap<String, IShape> hs = new HashMap<String, IShape>();
    hs.put("tri", tri);
    IAnimationModel m2 = new AnimationModelImpl(hs);
    assertEquals("Columns:\n"
        + "t x y w h r g b      t x y w h r g b\n"
        + "shape tri triangle",m2.getAnimationLog());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidAnimationModelImplConstructor() {
    // map cannot be null
    ISynchronisedActionSet ia = new SynchronizedActionSetImpl(0, 5, null);
  }



  @Test
  public void testShapeFieldConstructorInstance() {
    // should not cause any errors when instantiating
    Color c = new Color(1, 2, 3);
    Dimension d = new Dimension(1, 2);
    Posn p = new Posn(1, 2);
    assertEquals("1 2 3", c.toString());
    assertEquals("1 2", d.toString());
    assertEquals("1 2", p.toString());
  }

  @Test
  public void testInvalidShapeFieldConstructor() {
    try {
      new Color(-10, -2, -3);
      fail("Color arguments cannot be negative");
    } catch (IllegalArgumentException iae) {
      assertTrue(iae.getMessage().length() > 0);
    }
    try {
      new Color(256, 1, 1);
      fail("Color arguments cannot be greater than 255");
    } catch (IllegalArgumentException iae) {
      assertTrue(iae.getMessage().length() > 0);
    }

    try {
      new Dimension(-1, -2);
      fail("Dimension arguments cannot be negative");
    } catch (IllegalArgumentException iae) {
      assertTrue(iae.getMessage().length() > 0);
    }
    try {
      new Dimension(0, 1);
      fail("Dimension arguments cannot be less than or equal to 0");
    } catch (IllegalArgumentException iae) {
      assertTrue(iae.getMessage().length() > 0);
    }

  }

  @Test
  public void testShapeConstructorInstanceAndOfficalShapeName() {
    // should not cause any errors when instantiating
    Triangle t = tri;
    Oval o = oval;
    Rectangle r = rect;

    assertEquals("triangle", t.officialShapeName());
    assertEquals("oval", o.officialShapeName());
    assertEquals("rectangle", r.officialShapeName());
  }



  @Test
  public void testInvalidAnimationCommandClassConstructor() {
    try {
      new ChangeColor(-10, -2, -3, -5);
      fail("Ticks cannot be less than or equal to 0 for an action");
    } catch (IllegalArgumentException iae) {
      assertTrue(iae.getMessage().length() > 0);
    }
    try {
      new ChangeColor(256, 1, 1, 0);
      fail("Ticks cannot be less than or equal to 0 for an action");
    } catch (IllegalArgumentException iae) {
      assertTrue(iae.getMessage().length() > 0);
    }
    try {
      new ChangeColor(256, 1, 1, -1);
      fail("Ticks cannot be less than or equal to 0 for an action");
    } catch (IllegalArgumentException iae) {
      assertTrue(iae.getMessage().length() > 0);
    }

    // ChangeDimension
    try {
      new ChangeDimension(0,  1, 0);
      fail("Ticks cannot be less than or equal to 0 for an action");
    } catch (IllegalArgumentException iae) {
      assertTrue(iae.getMessage().length() > 0);
    }
    try {
      new ChangeDimension(256, 1, -1);
      fail("Ticks cannot be less than or equal to 0 for an action");
    } catch (IllegalArgumentException iae) {
      assertTrue(iae.getMessage().length() > 0);
    }

    // ChangePosition
    try {
      new ChangePosition(256, 1,  -1);
      fail("Ticks cannot be less than or equal to 0 for an action");
    } catch (IllegalArgumentException iae) {
      assertTrue(iae.getMessage().length() > 0);
    }
    try {
      new ChangePosition(256, 1,  0);
      fail("Ticks cannot be less than or equal to 0 for an action");
    } catch (IllegalArgumentException iae) {
      assertTrue(iae.getMessage().length() > 0);
    }
  }

  @Test
  public void testAnimationImplAndIAnimationCommandClassConstructors() {
    // should not throw any errors during instantiation
    IActionCommand changeColor5 = new ChangeColor(1, 2 , 3 , 5);
    IActionCommand changeDimension5 = new ChangeDimension(1, 2, 5);
    IActionCommand changePosition = new ChangePosition(1, 2, 5);
    listOfAnimationCommand.add(changeColor5);
    listOfAnimationCommand.add(changeDimension5);
    listOfAnimationCommand.add(changePosition);
    ISynchronisedActionSet ia = new SynchronizedActionSetImpl(0, 5, listOfAnimationCommand);
    // ensures that the listOfAnimationCommand was applied to the ISynchronisedActionSet and that
    // the 'getCommandList()' constructor can accurately retrieve it
    assertEquals(listOfAnimationCommand,ia.getCommandList());
  }

  @Test
  public void testInvalidAnimationImplConstructor() {
    try {
      new SynchronizedActionSetImpl(0, 5, null);
      fail("Cannot pass null instead of a list of 'IActionCommand'.");
    } catch (IllegalArgumentException iae) {
      assertTrue(iae.getMessage().length() > 0);
    }
  }
}
