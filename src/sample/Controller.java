package sample;


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

  @FXML
  private void dbButtonClicked() {
    String username = userNameField.getText();
    String password = passwordField.getText();
    String dob = dobPicker.getValue().toString();
    //System.out.println("Username is: " + username + "\n" + "Password is: " + password);
    db = new DatabaseConnection();
    //db.createTable(); only create table once?
    db.insertIntoTable(username, password,dob);
    Alert alert = new Alert(AlertType.INFORMATION,"Account Created Successfully");
    alert.setTitle("Success");
    alert.setHeaderText("Alert");
    db.printAll();
    alert.showAndWait();
  }

  @FXML
  private void haveAccountButton() throws Exception {
    Stage stage = Main.getPrimaryStage();

    Parent root = FXMLLoader.load(getClass().getResource("logIn.fxml"));

    stage.setScene(new Scene(root, 600, 450));
    stage.show();
  }

}
