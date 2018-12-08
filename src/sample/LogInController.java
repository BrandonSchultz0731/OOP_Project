package sample;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LogInController {

  static String user;

  @FXML
  private TextField enterUserField;

  @FXML
  private PasswordField enterPassField;

  public static Date birthday;

  public LogInController() {
    DatabaseConnection db = new DatabaseConnection();


  }

  @FXML
  private void signInButtonClicked() throws Exception {
    user = enterUserField.getText();
    String pass = enterPassField.getText();
    int validCount = 1;

    try {
      Statement statement = DatabaseConnection.conn.createStatement();
      ResultSet res = statement.executeQuery("SELECT * FROM Users");
      while (res.next()) {
//        System.out.println(
//            res.getString("Name") + " " + res.getString("Password"));
        String rightUser = res.getString("Name");
        String rightPass = res.getString("Password");
        if (rightUser.equals(user) && rightPass.equals(pass)) {
          //validCount = 0;
          birthday = res.getDate("DOB");

          System.out.println("Signed in Success!!!");
          Stage stage = Main.getPrimaryStage();

          Parent root = FXMLLoader.load(getClass().getResource("signedIn.fxml"));

          stage.setScene(new Scene(root, 600, 450));
          stage.show();
          return;
        } else {
          System.out
              .println("Not found...." + res.getString("Name") + " " + res.getString("Password"));
        }
      }
      if (validCount == 1) {
        Alert alert = new Alert(AlertType.ERROR,
            "Username and/or Password is Incorrect...Please Try Again");
        alert.setTitle("Invalid Login");
        alert.setHeaderText("Error");
        alert.showAndWait();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
