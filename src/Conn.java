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

    // -------- ACCESSING DATABASE ---------
    public static void Conn() throws ClassNotFoundException, SQLException {
        conn = null;
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:library.db");

        System.out.println("Database accessed!");
    }

    // -------- Creating tables --------
    public static void CreateDB() throws ClassNotFoundException, SQLException {
        statmt = conn.createStatement();
        statmt.execute("CREATE TABLE IF NOT EXISTS 'users' (" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'name' TEXT NOT NULL, " +
                "'access' INT NOT NULL, " +
                "'phone' TEXT NOT NULL, " +
                "'holding' TEXT);");
        statmt.execute("CREATE TABLE IF NOT EXISTS 'docs' (" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'type' INT NOT NULL, " +
                "'reference' BOOL DEFAULT FALSE, " +
                "'name' TEXT NOT NULL, " +
                "'author' TEXT NOT NULL, " +
                "'publisher' TEXT, " +
                "'journal' TEXT, " +
                "'edition' INT, " +
                "'editor' TEXT, " +
                "'released' DATE, " +
                "'price' INT NOT NULL, " +
                "'located' TEXT NOT NULL, " +
                "'tags' TEXT, " +
                "'taken_by' INT, " +
                "'taken_when' DATE, " +
                "'due_when' DATE," +
                "CHECK (price>=0));");
    }

    // -------- Adding users and documents --------
    public static void addUser(String name, int access, int phone) throws SQLException {
        statmt.executeUpdate("INSERT INTO users (name, access, phone) " +
                "VALUES ('" + name + "','" + access + "','" + phone + "')");

        System.out.println("User added!");
    }
    //type, reference, name, author, publisher, ournal, edition, editor, released, price, located, tags, taken_by, taken_when, due_when
    public static void addDoc(String name_doc, String type_doc, String author_doc, int year_doc) throws SQLException {
        statmt.executeUpdate("INSERT INTO docs (type, referenename, author, year, holder, date_of_taking, date_of_returning) " +
                "VALUES ('" + name_doc + "','" + type_doc + "','" + author_doc + "','" + year_doc + "','" + "none" + "','" + 0 + "','" + 0 + "')");

        System.out.println("Document added!");
    }

    //-------- Deleting users and documents --------
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

    public void removeUser_by_name(String name) {
        String sql = "DELETE FROM users WHERE name = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);

            pstmt.executeUpdate();

        } catch (SQLException e) {

        }
    }


    // -------- Printing users and documents table ---------
    public static void ReadDBUsers() throws ClassNotFoundException, SQLException {
        resSetUser = statmt.executeQuery("SELECT * FROM users");

        while (resSetUser.next()) {
            int id = resSetUser.getInt("id");
            int lvl = resSetUser.getInt("access");
            String name = resSetUser.getString("name");
            String holding = resSetUser.getString("holding");
            System.out.print("ID" + id);
            System.out.print(" LVL" + lvl);
            System.out.print(" " + name);
            System.out.println(" holds IDs " + holding);
            System.out.println();
        }

        System.out.println("Users printed.");
    }

    public static void ReadDBDocs() throws ClassNotFoundException, SQLException {
        resSetDoc = statmt.executeQuery("SELECT * FROM docs");

        while (resSetDoc.next()) {
            int id = resSetDoc.getInt("id");
            String name = resSetDoc.getString("name");
            String author = resSetDoc.getString("author");
            String type = resSetDoc.getString("type");
            String holder = resSetDoc.getString("taken_by");
            String date = resSetDoc.getString("taken_when");
            String due = resSetDoc.getString("due_when");
            System.out.print("ID" + id);
            System.out.print(" TYPE-" + type);
            System.out.print(" " + name);
            System.out.println(" by " + author + ".");
            if (resSetDoc.getString("taken_by") != null && resSetDoc.getString("taken_by").isEmpty()) {
                System.out.print("Currently is held by " + holder);
                System.out.println(", due " + date + ".");
            }
            System.out.println();
        }

        System.out.println("Documents printed.");
    }


    // -------- Terminating access --------
    public static void CloseDB() throws ClassNotFoundException, SQLException {
        conn.close();
        //statmt.close();
        resSetUser.close();
        resSetDoc.close();

        System.out.println("Terminated.");
    }

}
