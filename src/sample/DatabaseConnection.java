package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

  public static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
  public static final String JDBC_URL = "jdbc:derby:DerbyDemo;";

  static Connection conn;

  public DatabaseConnection() {
    try {
      conn = DriverManager.getConnection(JDBC_URL);
      if (conn != null) {
        System.out.println("Connected!");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void createTable() {

    try {
      conn.createStatement()
          .execute("Create TABLE Users (Name varchar(50),Password varchar(20))");
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

  public void insertIntoTable(String name, String password, String dob) {
    try {
      conn.createStatement()
          .execute("INSERT INTO Users VALUES ('" + name + "','" + password + "','" + dob + "')");
      conn.createStatement().execute("SELECT * FROM USERS ORDER BY DOB");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void printAll() {

    try {
      Statement statement = this.conn.createStatement();
      ResultSet res = statement.executeQuery("SELECT * FROM Users");
      while (res.next()) {
        System.out.println(
            res.getString("Name") + " " + res.getString("Password"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

}
