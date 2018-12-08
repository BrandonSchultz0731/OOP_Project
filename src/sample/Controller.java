package sample;


import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Controller {

  DatabaseConnection db;

  @FXML
  private TextField userNameField;

  @FXML
  private PasswordField passwordField;

  @FXML
  private Button buttonID;

  @FXML
  private DatePicker dobPicker;

  public static String user;
  public static LocalDate newAccountBday;

  @FXML
  private void dbButtonClicked() throws IOException {
    String username = userNameField.getText();
    if(username.equals("")){
      Alert nameAlert = new Alert(AlertType.ERROR, "No Username Entered. Please Enter Name");
      nameAlert.setTitle("No User");
      nameAlert.setHeaderText("Alert");
      nameAlert.showAndWait();
      return;
    }
    String password = passwordField.getText();
    if(password.equals("")){
      Alert psswdAlert = new Alert(AlertType.ERROR, "No Password Entered. Please Enter Password");
      psswdAlert.setTitle("No Password");
      psswdAlert.setHeaderText("Alert");
      psswdAlert.showAndWait();
      return;
    }
    if(dobPicker.getValue() == null){
      Alert bdayAlert = new Alert(AlertType.ERROR, "No Birthday Entered. Please Enter Birthday");
      bdayAlert.setTitle("No Birthday");
      bdayAlert.setHeaderText("Alert");
      bdayAlert.showAndWait();
      return;
    }
    user = username;
    String dob = dobPicker.getValue().toString();
    newAccountBday = dobPicker.getValue();
    //System.out.println("Username is: " + username + "\n" + "Password is: " + password);
    db = new DatabaseConnection(); //Create object to make database connection
    //db.createTable(); only create table once?
    db.insertIntoTable(username, password, dob);
    Alert alert = new Alert(AlertType.INFORMATION, "Account Created Successfully");
    alert.setTitle("Success");
    alert.setHeaderText("Alert");
    db.printAll();
    alert.showAndWait();
    Stage stage = Main.getPrimaryStage(); //Accessing Stage from global variable in Main

    Parent root = FXMLLoader.load(getClass().getResource("signedIn.fxml"));

    stage.setScene(new Scene(root, 600, 450));
    stage.show();
  }

  @FXML
  private void haveAccountButton() throws Exception {
    Stage stage = Main.getPrimaryStage(); //Accessing Stage from global variable in Main

    Parent root = FXMLLoader.load(getClass().getResource("logIn.fxml"));

    stage.setScene(new Scene(root, 600, 450));
    stage.show();
  }

}
