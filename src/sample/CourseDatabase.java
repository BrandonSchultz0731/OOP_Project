package sample;


import java.io.File;
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
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

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
  private ChoiceBox<Character> gradeChoiceBox;

  static Connection conn;
  static ResultSet res;
  static Statement statement;

  public CourseDatabase() {
    try {
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

  }

  @FXML
  private void addCourseToTableClicked() {

    String courseName = courseField.getText();
    char letterGrade = gradeChoiceBox.getSelectionModel().getSelectedItem();
    insertIntoTable(courseName, letterGrade); //Puts data into database
//    courseColumn.setCellValueFactory(new PropertyValueFactory<Courses,String>("courseName"));
//    letterGradeColumn.setCellValueFactory(new PropertyValueFactory<Courses,Character>("letterGrade"));

    //tableView.setItems(getCourses());

    UpdateTable();

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
    double count = 0, numOfClasses = 0;
    double result;
    try {
      res = statement.executeQuery("SELECT LETTERGRADE FROM COURSESTABLE");
      while (res.next()) {
        numOfClasses++;
        grade = res.getString(1).charAt(0);
        switch (grade) {
          case 'A':
            count += 4;
            break;
          case 'B':
            count += 3;
            break;
          case 'C':
            count += 2;
            break;
          case 'D':
            count += 1;
            break;
          default:
            count += 0;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    result = count / numOfClasses;

    gpaLabel.setText(Double.toString(result));
  }

  @FXML
  private void updateGradeButtonClicked() {
    String courseName = courseField.getText();
    char letterGrade = gradeChoiceBox.getSelectionModel().getSelectedItem();
    try {
      //Only update a course with the wrong letter grade
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
            .add(new Courses(res.getString("COURSENAME"), res.getString("LETTERGRADE").charAt(0)));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    courseColumn.setCellValueFactory(new PropertyValueFactory<Courses, String>("courseName"));
    letterGradeColumn
        .setCellValueFactory(new PropertyValueFactory<Courses, Character>("letterGrade"));

    tableView.setItems(courses);

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
    courseColumn.setCellValueFactory(new PropertyValueFactory<Courses, String>("courseName"));
    letterGradeColumn
        .setCellValueFactory(new PropertyValueFactory<Courses, Character>("letterGrade"));

    tableView.setItems(courses);
  }
}
