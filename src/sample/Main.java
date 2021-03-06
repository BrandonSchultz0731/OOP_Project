package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

  //Creating one main Stage to be used throughout the program
  private static Stage primaryStage;


  @Override
  public void start(Stage primaryStage) throws Exception {
    setPrimaryStage(primaryStage); //set passed in Stage to global Stage
    Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
    primaryStage.setTitle("Hello World");
    primaryStage.setScene(new Scene(root, 500, 400));
    primaryStage.show();
  }


  public static void main(String[] args) {
    launch(args);
  }

  private void setPrimaryStage(Stage primaryStage) {
    Main.primaryStage = primaryStage;
  }

  public static Stage getPrimaryStage() {
    return primaryStage;
  }
}
