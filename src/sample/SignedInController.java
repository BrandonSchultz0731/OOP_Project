package sample;

import static java.lang.Math.random;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BoxBlur;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SignedInController {

  @FXML
  private Label welcomeLabel, ageLabel;
  public String currentUser;

  public void initialize() {
    //This method is called immediately on creation
    if (LogInController.user == null) {
      welcomeLabel.setText("Welcome, " + Controller.user + ", to the signed in page!");
      currentUser = Controller.user;
    } else {
      welcomeLabel.setText("Welcome, " + LogInController.user + ", to the signed in page!");
      currentUser = LogInController.user;
    }
    GetUserAge();
  }

  private void GetUserAge() {
    LocalDate current = LocalDate.now();
    String age;
    if (LogInController.birthday == null) {
      //System.out.println(Period.between(Controller.newAccountBday,current).getYears());
      age = Integer.toString(Period.between(Controller.newAccountBday, current).getYears());
      ageLabel.setText(age);
    } else {
      //System.out.println(Period.between(LogInController.birthday.toLocalDate(),current).getYears());
      age = Integer
          .toString(Period.between(LogInController.birthday.toLocalDate(), current).getYears());
      ageLabel.setText(age);
    }
  }

  @FXML
  public void coursesButtonClicked() throws IOException {
    Stage stage = Main.getPrimaryStage();

    Parent root = FXMLLoader.load(getClass().getResource("CourseScene.fxml"));

    stage.setScene(new Scene(root, 800, 422));
    stage.show();
  }

  @FXML
  public void circlesButtonClicked() {
    Stage stage = Main.getPrimaryStage();
    Group root = new Group();
    Scene scene = new Scene(root, 800, 600, Color.BLACK);
    stage.setScene(scene);

    Group circles = new Group();
    for (int i = 0; i < 30; i++) {
      Circle circle = new Circle(150, Color.web("white", 0.05));
      circle.setStrokeType(StrokeType.OUTSIDE);
      circle.setStroke(Color.web("white", 0.16));
      circle.setStrokeWidth(4);
      circles.getChildren().add(circle);
    }
    Rectangle colors = new Rectangle(scene.getWidth(), scene.getHeight(),
        new LinearGradient(0f, 1f, 1f, 0f, true, CycleMethod.NO_CYCLE, new
            Stop[]{
            new Stop(0, Color.web("#f8bd55")),
            new Stop(0.14, Color.web("#c0fe56")),
            new Stop(0.28, Color.web("#5dfbc1")),
            new Stop(0.43, Color.web("#64c2f8")),
            new Stop(0.57, Color.web("#be4af7")),
            new Stop(0.71, Color.web("#ed5fc2")),
            new Stop(0.85, Color.web("#ef504c")),
            new Stop(1, Color.web("#f2660f")),}));
    colors.widthProperty().bind(scene.widthProperty());
    colors.heightProperty().bind(scene.heightProperty());

    Group blendModeGroup =
        new Group(new Group(new Rectangle(scene.getWidth(), scene.getHeight(),
            Color.BLACK), circles), colors);
    colors.setBlendMode(BlendMode.OVERLAY);
    root.getChildren().add(blendModeGroup);

    circles.setEffect(new BoxBlur(10, 10, 3));

    Timeline timeline = new Timeline();
    for (Node circle : circles.getChildren()) {
      timeline.getKeyFrames().addAll(
          new KeyFrame(Duration.ZERO, // set start position at 0
              new KeyValue(circle.translateXProperty(), random() * 800),
              new KeyValue(circle.translateYProperty(), random() * 600)
          ),
          new KeyFrame(new Duration(40000), // set end position at 40s
              new KeyValue(circle.translateXProperty(), random() * 800),
              new KeyValue(circle.translateYProperty(), random() * 600)
          )
      );
    }
// play 40s of animation
    timeline.play();

    stage.show();
  }
}


