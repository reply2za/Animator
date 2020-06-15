import static org.junit.Assert.assertEquals;

import cs3500.easyanimator.controller.ControllerImpl;
import cs3500.easyanimator.controller.IControllerFeatures;
import cs3500.easyanimator.model.AnimationModelImpl;
import cs3500.easyanimator.model.AnimationModelImpl.Builder;
import cs3500.easyanimator.model.IAnimationModel;
import cs3500.easyanimator.model.IReadOnlyModel;
import cs3500.easyanimator.model.ReadOnlyModelImpl;
import cs3500.easyanimator.model.actions.ChangePosition;
import cs3500.easyanimator.model.shapes.*;
import cs3500.easyanimator.model.shapes.Rectangle;
import cs3500.easyanimator.util.AnimationBuilder;
import cs3500.easyanimator.util.AnimationReader;
import cs3500.easyanimator.view.IView;
import cs3500.easyanimator.view.ViewImplSVG;
import cs3500.easyanimator.view.ViewImplTextual;
import cs3500.easyanimator.view.ViewImplVisual;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.text.View;
import org.junit.Test;

/**
 * Tests public methods of the view of type {@link cs3500.easyanimator.view.IView}.
 */
public class TestAnimationViewImpl {

  @Test
  public void testTextual() throws IOException {
    File file = new File("test/toh-3.txt");
    AnimationBuilder<IAnimationModel> builder = new Builder();
    IAnimationModel model1 = AnimationReader.parseFile(new FileReader(file), builder);
    Appendable appendable = new StringBuilder();
    ViewImplTextual textual = new ViewImplTextual(appendable, new ReadOnlyModelImpl(model1));
    // creates a controller to call the private method in the view
    IControllerFeatures c = new ControllerImpl(model1, textual);
    c.runAnimator();
    assertEquals("Columns:\n"
            + "t x y w h r g b      t x y w h r g b\n"
            + "shape disk1 rectangle\n"
            + "1 190 180 20 30 0 49 90      25 191 180 20 30 0 49 90\n"
            + "shape disk2 rectangle\n"
            + "shape disk3 rectangle",
        appendable.toString());
  }

  @Test
  public void testSVG() throws FileNotFoundException {
    File file = new File("test/toh-3.txt");
    AnimationBuilder<IAnimationModel> builder = new Builder();
    IAnimationModel model1 = AnimationReader.parseFile(new FileReader(file), builder);
    IReadOnlyModel read = new ReadOnlyModelImpl(model1);
    Appendable appendable = new StringBuilder();
    ViewImplSVG svg = new ViewImplSVG(appendable, 1, read);
    // creates a controller to call the private method in the view
    IControllerFeatures c = new ControllerImpl(model1, svg);
    c.runAnimator();
    assertEquals("<svg width=\"410\" height=\"220\" version=\"1.1\"\n"
            + "     xmlns=\"http://www.w3.org/2000/svg\">\n"
            + "<rect>\n"
            + "<animate id=\"base\" begin=\"0;base.end\" dur=\"26000ms\" attributeName="
            + "\"visibility\" from=\"hide\" to=\"hide\"/></rect><rect id=\"disk1\" x=\"190\" y="
            + "\"180\" width=\"20\" height=\"30\" fill=\"rgb(0,49,90)\" visibility=\"visible\" >\n"
            + "<animate attributeType=\"xml\" begin=\"base.begin+1000ms\" dur=\"24000ms\" "
            + "attributeName=\"x\" from=\"190\" to=\"191\" fill=\"freeze\" />\n"
            + "<animate attributeType=\"xml\" begin=\"base.begin+1000ms\" dur=\"24000ms\" "
            + "attributeName=\"y\" from=\"180\" to=\"180\" fill=\"freeze\" />\n"
            + "</rect><rect id=\"disk2\" x=\"0\" y=\"0\" width=\"0\" height=\"0\" fill=\""
            + "rgb(0,0,0)\" visibility=\"visible\" >\n"
            + "</rect><rect id=\"disk3\" x=\"0\" y=\"0\" width=\"0\" height=\"0\" fill=\""
            + "rgb(0,0,0)\" visibility=\"visible\" >\n"
            + "</rect></svg>\n",
        appendable.toString());

  }


}
