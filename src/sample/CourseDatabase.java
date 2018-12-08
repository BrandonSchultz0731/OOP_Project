package sample;


import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class CourseDatabase implements Initializable {

  public static final String JDBC_URL = "jdbc:derby:DerbyDemo;";
  ObservableList<Courses> courses = FXCollections.observableArrayList();
  ObservableList<Character> gradeChoice = FXCollections
      .observableArrayList('A', 'B', 'C', 'D', 'F');


  @FXML
  private TableView<Courses> tableView;
  @FXML
  private TableColumn<Courses, String> courseColumn;
  @FXML
  private TableColumn<Courses, Character> letterGradeColumn;
  @FXML
  private TextField courseField;
  @FXML
  private Label gpaLabel;
  @FXML
  private Label mssgLabel;
  @FXML
  private ChoiceBox<Character> gradeChoiceBox;
  @FXML
  private ImageView gradeIMG;

  static Connection conn;
  static ResultSet res;
  static Statement statement;

  public CourseDatabase() {
    try {
      //Attempt to establish a connection to the given database URL.
      conn = DriverManager.getConnection(JDBC_URL);
      statement = conn
          .createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      if (conn != null) {
        System.out.println("Connected from CourseDatabase");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }


  }

//  private ObservableList<Courses> getCourses() {
////    courses.add(new Courses("Calculus 3",'A'));
////    courses.add(new Courses("Object Oriented Prog",'A'));
////    courses.add(new Courses("Entreprenuriship",'B'));
//    courses.add(new Courses(courseField.getText(),letterGradeField.getText().charAt(0)));
//    //Statement statement = this.conn.createStatement();
//    //ResultSet res = statement.executeQuery("SELECT * FROM COURSESTABLE");
//
//
//    return courses;
//  }

  public void insertIntoTable(String courseName, char letterGrade) {
    try {
      //Inserting into database table "COURSETABLE"
      conn.createStatement()
          .execute(
              "INSERT INTO COURSESTABLE (COURSENAME,LETTERGRADE) VALUES ('" + courseName + "','"
                  + letterGrade + "')");
    } catch (SQLException e) {
      e.printStackTrace();
    }
//    finally {
//      if (statement != null) {
//        try {
//          statement.close();
//        } catch (SQLException e) { /* ignored */}
//      }
//      if (res != null) {
//        try {
//          res.close();
//        } catch (SQLException e) { /* ignored */}
//      }
//      if (conn != null) {
//        try {
//          conn.close();
//        } catch (SQLException e) { /* ignored */}
//      }
//    }

  }

  @FXML
  private void addCourseToTableClicked() {

    String courseName = courseField.getText();
    char letterGrade;
    try {
      letterGrade = gradeChoiceBox.getSelectionModel().getSelectedItem();
    } catch (NullPointerException n) {
      System.out.println("Empty letter grade");
      letterGrade = 'z';
    }

    if (courseName.isEmpty()) {
      Alert courseNameAlert = new Alert(AlertType.ERROR);
      courseNameAlert.setTitle("No Course Name");
      courseNameAlert.setContentText("Did not enter a course name...");
      courseNameAlert.showAndWait();
    } else if (letterGrade == 'z') {
      Alert letterGradeAlert = new Alert(AlertType.ERROR);
      letterGradeAlert.setTitle("No Letter Grade");
      letterGradeAlert.setContentText("Did not provide course letter grade...");
      letterGradeAlert.showAndWait();
    } else {
      int count = 0;
      try {
        res = statement.executeQuery("SELECT COURSENAME FROM COURSESTABLE");
        String name;

        while (res.next()) {
          name = res.getString("COURSENAME");
          if (name.equals(courseName)) {
            count++;
          }
        }

      } catch (SQLException e) {
        e.printStackTrace();
      }

      if (count == 1) {
        Alert alert = new Alert(AlertType.ERROR, "Course Entered Already Exists...");
        alert.setTitle("Invalid Course");
        alert.setHeaderText("Error");
        alert.showAndWait();
      } else {
        //Course entered is not already inside list

        insertIntoTable(courseName, letterGrade); //Puts data into database
//    courseColumn.setCellValueFactory(new PropertyValueFactory<Courses,String>("courseName"));
//    letterGradeColumn.setCellValueFactory(new PropertyValueFactory<Courses,Character>("letterGrade"));

        //tableView.setItems(getCourses());

        UpdateTable();
      }
    }

  }

  private void UpdateTable() {
    try {
      //ONLY WANT TO UPDATE TABLE WITH LATEST ENTRY
      res = statement.executeQuery("SELECT * FROM COURSESTABLE");
      res.absolute(courses.size() + 1);
      courses.add(new Courses(res.getString("COURSENAME"), res.getString("LETTERGRADE").charAt(0)));

    } catch (SQLException e) {
      e.printStackTrace();
    }
    courseColumn.setCellValueFactory(new PropertyValueFactory<Courses, String>("courseName"));
    letterGradeColumn
        .setCellValueFactory(new PropertyValueFactory<Courses, Character>("letterGrade"));

    tableView.setItems(courses);
  }

  @FXML
  private void deleteAllButtonClicked() {
    try {
      //Removes all data from database
      conn.createStatement()
          .execute("DELETE FROM COURSESTABLE");
    } catch (SQLException e) {
      e.printStackTrace();
    }
    courses.clear(); //clear list that displays on table

    //tableView.setItems(getCourses());
  }

  @FXML
  private void calculateGpaClicked() {
    char grade;
    int qualityPoints = 0;
    int numOfClasses = 0;
    double result;
    try {
      res = statement.executeQuery("SELECT LETTERGRADE FROM COURSESTABLE");
      while (res.next()) {
        numOfClasses++;
        grade = res.getString(1).charAt(0);
        switch (grade) {
          case 'A':
            qualityPoints += 4;
            break;
          case 'B':
            qualityPoints += 3;
            break;
          case 'C':
            qualityPoints += 2;
            break;
          case 'D':
            qualityPoints += 1;
            break;
          default:
            qualityPoints += 0;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    result = (double) qualityPoints / numOfClasses;

    gpaLabel.setText(Double.toString(result));
    //SpotBugs was giving me error about not using BigDecimal, so i used an absolute value instead
    //and got rid of error
    if (Math.abs(result - 4) < .0000001) {
      mssgLabel.setText("You have straight A's!\nKeep it up!!");
      gradeIMG.setImage(new Image("sample/Images/A_Student.jpg"));
    } else if (result < 4.0 && result >= 3.0) {
      mssgLabel.setText("You are a B student. Pretty good!");
      gradeIMG.setImage(new Image("sample/Images/B_Student.jpg"));
    } else if (result < 3.0 && result >= 2.0) {
      mssgLabel.setText("You are a C student. Could be better");
      gradeIMG.setImage(new Image("sample/Images/C_Student.jpg"));
    } else if (result < 2.0 && result >= 1.0) {
      mssgLabel.setText("You have a D average. Hit the books!");
      gradeIMG.setImage(new Image("sample/Images/D_Student.jpg"));
    } else {
      mssgLabel.setText("Maybe you should go to class...");
      gradeIMG.setImage(new Image("sample/Images/F_Student.jpg"));
    }
  }

  @FXML
  private void updateGradeButtonClicked() {
    String courseName;
    try {
      courseName = tableView.getSelectionModel().getSelectedItem().getCourseName();
    } catch (Exception e) {
      courseName = null;
    }

    char letterGrade = gradeChoiceBox.getSelectionModel().getSelectedItem();
    if (courseName != null) {
      //user didnt select from table
      try {
        //Only update a course with the wrong letter grade
        //Error says nonconstant variable inside SQL statement, however this is what i want it to do
        //for my programs purpose
        conn.createStatement().execute(
            "UPDATE COURSESTABLE SET LETTERGRADE = '" + letterGrade + "' WHERE COURSENAME = '"
                + courseName + "'");

      } catch (SQLException e) {
        e.printStackTrace();
      }
      courses.clear();
      try {
        res = statement.executeQuery("SELECT * FROM COURSESTABLE");
        while (res.next()) {
          courses
              .add(
                  new Courses(res.getString("COURSENAME"), res.getString("LETTERGRADE").charAt(0)));
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
      courseColumn.setCellValueFactory(new PropertyValueFactory<Courses, String>("courseName"));
      letterGradeColumn
          .setCellValueFactory(new PropertyValueFactory<Courses, Character>("letterGrade"));

      tableView.setItems(courses);
    } else {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("No selection");
      alert.setContentText("Please highlight a course in the table and try again");
      alert.setHeaderText("Could Not Find Selected Course");
      alert.showAndWait();
    }


  }

  @FXML
  private void deleteSelectedRowClicked() {
    ObservableList<Courses> allCourses, selectedCourses;
    allCourses = tableView.getItems();
    selectedCourses = tableView.getSelectionModel().getSelectedItems();
    for (int i = 0; i < selectedCourses.size(); i++) {
      String a = selectedCourses.get(i).getCourseName();
      System.out.println(a);
      //char b = selectedCourses.get(i).getLetterGrade();
      try {
        conn.createStatement().execute("DELETE FROM COURSESTABLE WHERE COURSENAME = '" + a + "'");
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    selectedCourses.forEach(allCourses::remove);

  }

  @FXML
  private void mainScreen() throws IOException {
    Stage stage = Main.getPrimaryStage();

    Parent root = FXMLLoader.load(getClass().getResource("signedIn.fxml"));

    stage.setScene(new Scene(root, 600, 450));
    stage.show();
  }


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    try {
      res = statement.executeQuery("SELECT * FROM COURSESTABLE");
      while (res.next()) {
        courses
            .add(new Courses(res.getString("COURSENAME"), res.getString("LETTERGRADE").charAt(0)));
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    gradeChoiceBox.setItems(gradeChoice);
    gradeChoiceBox.setValue('A');
    courseColumn.setCellValueFactory(new PropertyValueFactory<Courses, String>("courseName"));
    letterGradeColumn
        .setCellValueFactory(new PropertyValueFactory<Courses, Character>("letterGrade"));

    tableView.setItems(courses);
  }
}
