package sample;

public class Courses {

  private String courseName;
  private char letterGrade;

  public String getCourseName() {
    return courseName;
  }

  public void setCourseName(String courseName) {
    this.courseName = courseName;
  }

  public char getLetterGrade() {
    return letterGrade;
  }

  public void setLetterGrade(char letterGrade) {
    this.letterGrade = letterGrade;
  }

  public Courses(String courseName, char letterGrade) {
    this.courseName = courseName;
    this.letterGrade = letterGrade;
  }

}
