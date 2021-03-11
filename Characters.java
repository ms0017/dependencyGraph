import javafx.animation.*;
import javafx.application.Application;
import javafx.collections.*;
import javafx.scene.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Characters extends Application {

  private static final String CIRCLE_TEXT = "  Welcome to Java  ";

  @Override
  public void start(final Stage stage) throws Exception {

    final Text text = new Text(CIRCLE_TEXT);
    text.setStyle("-fx-font-size: 40px");

    Circle circle = new Circle(200, 200, 100);

    final ObservableList<Text> parts = FXCollections.observableArrayList();
    final ObservableList<PathTransition> transitions
            = FXCollections.observableArrayList();

    for (char character : text.textProperty().get().toCharArray()) {
      Text c = new Text(character + "");
      c.setEffect(text.getEffect());
      c.setStyle(text.getStyle());
      parts.add(c);
      transitions.add(createPathTransition(circle, c));
    }

    AnchorPane ap = new AnchorPane();
    ap.getChildren().addAll(parts);

    for (int i = 0; i < parts.size(); i++) {
      final Transition t = transitions.get(i);
      t.stop();
      t.jumpTo(Duration.seconds(10).multiply((i + 0.5) * 1.0 / parts.size()));

      AnimationTimer timer = new AnimationTimer() {
        int frameCounter = 0;

        @Override
        public void handle(long l) {
          frameCounter++;
          if (frameCounter == 1) {
            t.stop();
            stop();
          }
        }
      };
      //timer.start();
      t.play();
    }

    stage.setTitle("Circle Text Sample");
    stage.setScene(new Scene(ap, 400, 400, Color.ALICEBLUE));
    stage.show();
  }

  private PathTransition createPathTransition(Shape shape, Text text) {
    final PathTransition trans
            = new PathTransition(Duration.seconds(10), shape, text);
    trans.setAutoReverse(false);
    trans.setCycleCount(PathTransition.INDEFINITE);
    trans.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
    trans.setInterpolator(Interpolator.LINEAR);

    return trans;
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }
}