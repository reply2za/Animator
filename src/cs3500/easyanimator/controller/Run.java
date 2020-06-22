package cs3500.easyanimator.controller;

import cs3500.easyanimator.model.AnimationModelImpl.Builder;
import cs3500.easyanimator.model.IAnimationModel;
import cs3500.easyanimator.model.IReadOnlyModel;
import cs3500.easyanimator.model.ReadOnlyModelImpl;
import cs3500.easyanimator.util.AnimationBuilder;
import cs3500.easyanimator.util.AnimationReader;
import cs3500.easyanimator.view.IView;
import cs3500.easyanimator.view.ViewImplSVG;
import cs3500.easyanimator.view.ViewImplTextual;
import cs3500.easyanimator.view.ViewImplVisual;
import cs3500.easyanimator.view.ViewImplVisualControllable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * The class that holds the main method for the Animation Project. This runs the project and must
 * take in a minimum of an input and a view type for the arguments.
 */
public class Run {

  /**
   * Runs the animation based on the given command line arguments. If no command line arguments then
   * an exception will be thrown. Type of command line arguments include "-in" for an input file,
   * "-view" for the type of view, "-out" for writing to an output file, and "-speed" for
   * determining the speed of the program. If no "-out" is specified then the program will print to
   * the console. Valid "-view" types are "text" for textual outputs, "svg" for SVG based outputs,
   * and "visual" for GUI outputs.
   *
   * @param args The arguments for the main methods to specify how the controller and views are
   *             formatted.
   * @throws FileNotFoundException    If the file is unable to be found.
   * @throws IllegalArgumentException When the command line argument is invalid.
   * @throws IllegalStateException    If the command line arguments do not initialize the mandatory
   *                                  commands.
   */
  public static void main(String[] args) throws IOException {


    // ------- START: NOT A PART OF THE ASSIGNMENT --------------
    // this section allows command line arguments in the console
    Scanner s = new Scanner(System.in);
    if (args.length == 0) {
      args = new String[6];
      args[0] = "-in";
      String n = s.next();
      s.close();
      // connecting a letter to a file
      if (n.equalsIgnoreCase("b")) {
        args[1] = "big-bang-big-crunch.txt";
      } else if (n.equalsIgnoreCase("bb")) {
        args[1] = "buildings.txt";
      } else if (n.equalsIgnoreCase("t") || n.equalsIgnoreCase("t12")) {
        args[1] = "toh-12.txt";
      } else if (n.equalsIgnoreCase("t8")) {
        args[1] = "toh-8.txt";
      } else if (n.equalsIgnoreCase("t5")) {
        args[1] = "toh-5.txt";
      } else if (n.equalsIgnoreCase("t3")) {
        args[1] = "toh-3.txt";
      } else if (n.equalsIgnoreCase("h")) {
        args[1] = "hanoi.txt";
      }
      args[2] = "-view";
      args[3] = "edit"; // set your view
      args[4] = "-speed";
      args[5] = "1";
    }

    // -in big-bang-big-crunch.txt -view visual -speed 20

    // ------- END: NOT A PART OF THE ASSIGNMENT ----------------

    String fileString = "";
    String outString = "";
    String viewString = "";
    String speedString = "1";
    File file;
    IView view;
    boolean isInputCalled = false;
    boolean isViewCalled = false;

    if (args.length > 0 && !args[0].equals("")) {
      for (int i = 0; i < args.length; i += 2) {
        switch (args[i]) {
          case ("-in"):
            fileString = args[i + 1];
            isInputCalled = true;
            break;
          case ("-view"):
            viewString = args[i + 1];
            isViewCalled = true;
            break;
          case ("-out"):
            outString = args[i + 1];
            break;
          case ("-speed"):
            speedString = args[i + 1];
            break;
          default:
            throw new IllegalArgumentException("Not a valid command");
        }

      }
    } else {
      throw new IllegalStateException("No command line arguments found.");
    }
    if (!isInputCalled || !isViewCalled) {
      throw new IllegalStateException("Filepath or type-of-view not found.");
    }

    // Gets the input file
    file = new File(fileString);
    StringBuilder sb = new StringBuilder();
    // Creates a model from the input file
    AnimationBuilder<IAnimationModel> builder = new Builder();
    IAnimationModel model = AnimationReader.parseFile(new FileReader(file), builder);
    // Creates a read-only model for the view to retrieve data
    IReadOnlyModel readOnlyModel = new ReadOnlyModelImpl(model);

    // Creates the view
    switch (viewString) {
      case ("text"):
        view = new ViewImplTextual(sb, readOnlyModel);
        break;
      case ("svg"):
        view = new ViewImplSVG(sb, Integer.parseInt(speedString), readOnlyModel);
        break;
      case ("visual"):
        view = new ViewImplVisual(Integer.parseInt(speedString), readOnlyModel);
        break;
      case ("edit"):
        view = new ViewImplVisualControllable(Integer.parseInt(speedString), readOnlyModel);
        break;
      default:
        throw new IllegalArgumentException("Invalid view argument");
    }

    ControllerImpl controller = new ControllerImpl(model, view);

    // print to console or file
    controller.runAnimator();
    if (outString.isEmpty()) {
      System.out.println(sb.toString());
    } else {
      FileWriter fw = new FileWriter(outString);
      fw.write(sb.toString());
      fw.close();
    }

  }

}

