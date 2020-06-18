import static org.junit.Assert.assertEquals;

import cs3500.easyanimator.controller.ControllerImpl;
import cs3500.easyanimator.controller.IControllerFeatures;
import cs3500.easyanimator.model.AnimationModelImpl.Builder;
import cs3500.easyanimator.model.IAnimationModel;
import cs3500.easyanimator.model.IReadOnlyModel;
import cs3500.easyanimator.model.ReadOnlyModelImpl;
import cs3500.easyanimator.util.AnimationBuilder;
import cs3500.easyanimator.util.AnimationReader;
import cs3500.easyanimator.view.ViewImplSVG;
import cs3500.easyanimator.view.ViewImplTextual;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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
    String content = Files
        .readString(Path.of("test/testTextualAns.txt"), StandardCharsets.US_ASCII);
    assertEquals(content,
        appendable.toString());
  }

  @Test
  public void testSVG() throws IOException {
    File file = new File("test/toh-3.txt");
    AnimationBuilder<IAnimationModel> builder = new Builder();
    IAnimationModel model1 = AnimationReader.parseFile(new FileReader(file), builder);
    IReadOnlyModel read = new ReadOnlyModelImpl(model1);
    Appendable appendable = new StringBuilder();
    ViewImplSVG svg = new ViewImplSVG(appendable, 10, read);
    // creates a controller to call the private method in the view
    IControllerFeatures c = new ControllerImpl(model1, svg);
    c.runAnimator();
    String content = Files.readString(Path.of("test/testSVGAns.txt"),
        StandardCharsets.US_ASCII);
    assertEquals(content,
        appendable.toString());

  }

  @Test
  public void testSVG2() throws IOException {
    File file = new File("test/buildings.txt");
    AnimationBuilder<IAnimationModel> builder = new Builder();
    IAnimationModel model1 = AnimationReader.parseFile(new FileReader(file), builder);
    IReadOnlyModel read = new ReadOnlyModelImpl(model1);
    Appendable appendable = new StringBuilder();
    ViewImplSVG svg = new ViewImplSVG(appendable, 20, read);
    // creates a controller to call the private method in the view
    IControllerFeatures c = new ControllerImpl(model1, svg);
    c.runAnimator();
    String content = Files.readString(Path.of("test/testSVG2Ans.txt"),
        StandardCharsets.US_ASCII);
    assertEquals(content,
        appendable.toString());

  }


}
