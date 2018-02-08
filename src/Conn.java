import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;


public class Conn {
    private static Connection conn;
    private static Statement query;
    private static ResultSet resSetUser;
    private static ResultSet resSetDoc;

    private Connection connect() {
        String url = "jdbc:sqlite:library.db";
        conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    // -------- ACCESSING DATABASE ---------
    public static void access() throws ClassNotFoundException, SQLException {
        conn = null;
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:library.db");

        System.out.println("Database accessed!");
    }

    // -------- Creating tables --------
    public static void createDB() throws ClassNotFoundException, SQLException {
        query = conn.createStatement();
        query.execute("CREATE TABLE IF NOT EXISTS 'users' (" +
                "'id' INT PRIMARY KEY AUTOINCREMENT, " +
                "'name' TEXT NOT NULL, " +
                "'access' INT NOT NULL, " +
                "'phone' TEXT NOT NULL, " +
                "'holding' TEXT);");
        query.execute("CREATE TABLE IF NOT EXISTS 'docs' (" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'type' INT NOT NULL, " +
                "'reference' BIT DEFAULT 0, " +
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
                "'due_when' DATE, " +
                "FOREIGN KEY ('taken_by') REFERENCES 'users'('id')" +
                "CHECK (price>=0));");
    }

    // -------- Adding users and documents --------
    public static void addUser(String name, int access, String phone) throws SQLException {
        query.executeUpdate("INSERT INTO users (name, access, phone) " +
                "VALUES ('" + name + "','" + access + "','" + phone + "')");

        System.out.println("User added!");
    }
    //type, reference, name, author, publisher, journal, edition, editor, released, price, located, tags, taken_by, taken_when, due_when
    public static void addDoc(int type, boolean reference, String name, String author, String publisher, String journal, int edition, String editor, String released, int price, String located, String tags) throws SQLException {
        query.executeUpdate("INSERT INTO docs (type, reference, name, author, publisher, journal, edition, editor, released, price, located, tags) " +
                "VALUES ('" + type + "','" + reference + "','" + name + "','" + author + "','" + publisher + "','" + journal + "','" + edition + "','" + editor + "','" + released + "','" + price + "','" + located + "','" + tags + "')");

        System.out.println("Document added!");
    }

    //-------- Deleting users and documents --------
    public void removeDocId(int id) {
        String sql = "DELETE FROM docs WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeDocName(String name) {
        String sql = "DELETE FROM docs WHERE name = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);

            pstmt.executeUpdate();

        } catch (SQLException e) {

        }
    }

    public void removeUserId(int id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            pstmt.executeUpdate();

        } catch (SQLException e) {

        }
    }

    public void removeUserName(String name) {
        String sql = "DELETE FROM users WHERE name = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);

            pstmt.executeUpdate();

        } catch (SQLException e) {

        }
    }

    // -------- Printing users and documents table ---------
    public static void readUsers() throws ClassNotFoundException, SQLException {
        resSetUser = query.executeQuery("SELECT * FROM users");

        while (resSetUser.next()) {
            int id = resSetUser.getInt("id");
            int lvl = resSetUser.getInt("access");
            String name = resSetUser.getString("name");
            String holding = resSetUser.getString("holding");
            System.out.print("ID" + id);
            System.out.print(" LVL" + lvl);
            System.out.print(" " + name);
            System.out.println(" holds IDs " + holding);
        }

        System.out.println("Users printed.");
    }

    public static void readDocs() throws ClassNotFoundException, SQLException {
        resSetDoc = query.executeQuery("SELECT * FROM docs");

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
        }

        System.out.println("Documents printed.");
    }


    // -------- Terminating access --------
    public static void terminate() throws ClassNotFoundException, SQLException {
        if (query != null) query.close();
        if (conn != null) conn.close();
        if (resSetUser != null) resSetUser.close();
        if (resSetDoc != null) resSetDoc.close();

        System.out.println("Terminated.");
    }

}
