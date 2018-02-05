import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;


public class Conn {
    public static java.sql.Connection conn;
    public static Statement statmt;
    public static ResultSet resSetUser;
    public static ResultSet resSetDoc;

    private Connection connect() {
        String url = "jdbc:sqlite:library.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    // --------Connecting to the database--------
    public static void Conn() throws ClassNotFoundException, SQLException {
        conn = null;
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:library.db");

        System.out.println("Connected to database!");
    }

    // --------Creating the database, if doesn't exist--------
    public static void CreateDB() throws ClassNotFoundException, SQLException {
        statmt = conn.createStatement();
        statmt.execute("CREATE TABLE if not exists 'users' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' text, 'surname' text, 'lvl' INT, 'holding' text);");
        statmt.execute("CREATE TABLE if not exists 'docs' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' text, 'type' text, 'author' text, 'year' INT, 'holder' text, 'date_of_taking' INT, 'date_of_returning' INT);");

        System.out.println("Tables are already created.");
    }

    // --------Adding Users and Documents--------
    public static void addUser(String name_user, String surname_user, int lvl) throws SQLException {
        statmt.executeUpdate("INSERT INTO users (name, surname, lvl, holding) " +
                "VALUES ('" + name_user + "','" + surname_user + "','" + lvl + "','" + "none" + "')");

        System.out.println("User is added!");
    }

    public static void addDoc(String name_doc, String type_doc, String author_doc, int year_doc) throws SQLException {
        statmt.executeUpdate("INSERT INTO docs (name, type, author, year, holder, date_of_taking, date_of_returning) " +
                "VALUES ('" + name_doc + "','" + type_doc + "','" + author_doc + "','" + year_doc + "','" + "none" + "','" + 0 + "','" + 0 + "')");

        System.out.println("Doc has added!");
    }

    //---------Removing Users and Documents--------
    public void removeDoc_by_id(int id) {
        String sql = "DELETE FROM docs WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeDoc_by_name(String name) {
        String sql = "DELETE FROM docs WHERE name = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);

            pstmt.executeUpdate();

        } catch (SQLException e) {

        }
    }

    public void removeUser_by_id(int id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            pstmt.executeUpdate();

        } catch (SQLException e) {

        }
    }

    public void removeUser_by_name(String name, String surname) {
        String sql = "DELETE FROM users WHERE name = ? AND surname = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);

            pstmt.setString(2, surname);

            pstmt.executeUpdate();

        } catch (SQLException e) {

        }
    }


    // --------Users table output--------
    public static void ReadDBUsers() throws ClassNotFoundException, SQLException {
        resSetUser = statmt.executeQuery("SELECT * FROM users");


        while (resSetUser.next()) {
            int id = resSetUser.getInt("id");
            String name = resSetUser.getString("name");
            String surname = resSetUser.getString("surname");
            int lvl = resSetUser.getInt("lvl");
            String holding = resSetUser.getString("holding");
            System.out.println("ID = " + id);
            System.out.println("name = " + name);
            System.out.println("surname = " + surname);
            System.out.println("lvl = " + lvl);
            System.out.println("holding docs = " + holding);
            System.out.println();
        }


        System.out.println("Table is printed.");
    }

    // --------Docs table output--------
    public static void ReadDBDocs() throws ClassNotFoundException, SQLException {

        resSetDoc = statmt.executeQuery("SELECT * FROM docs");

        while (resSetDoc.next()) {
            int id = resSetDoc.getInt("id");
            String name = resSetDoc.getString("name");
            String type = resSetDoc.getString("type");
            int year = resSetDoc.getInt("year");
            String holder = resSetDoc.getString("holder");
            String date_of_taking = resSetDoc.getString("date_of_taking");
            String date_of_returning = resSetDoc.getString("date_of_returning");
            System.out.println("ID = " + id);
            System.out.println("name = " + name);
            System.out.println("type = " + type);
            System.out.println("year = " + year);
            System.out.println("holder = " + holder);
            System.out.println("date of last taking = " + date_of_taking);
            System.out.println("date of last returning = " + date_of_returning);
            System.out.println();
        }

        System.out.println("Table is printed.");
    }


    // --------Closing the Database--------
    public static void CloseDB() throws ClassNotFoundException, SQLException {
        conn.close();
        statmt.close();
        resSetUser.close();
        resSetDoc.close();

        System.out.println("Connections are closed.");
    }

}
