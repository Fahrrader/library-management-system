import java.sql.*;

public class Conn {
    public static Connection conn;
    public static Statement query;
    public static ResultSet resSet;

    // -------- ACCESSING DATABASE ---------
    public static void access() throws ClassNotFoundException, SQLException {
        conn = null;
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:library.db");

        System.out.println("Database accessed!");
    }

    // -------- Creating tables --------
    public static void createDB() throws SQLException {
        query = conn.createStatement();
        query.execute("CREATE TABLE IF NOT EXISTS 'users' (" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'name' TEXT NOT NULL, " +
                "'access' INT NOT NULL, " +
                "'password' TEXT NOT NULL, " +
                "'phone' TEXT NOT NULL, " +
                "'holding' TEXT DEFAULT '');");
        query.execute("CREATE TABLE IF NOT EXISTS 'docs' (" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'type' INT NOT NULL, " +
                "'copy' INT NOT NULL, " +
                "'reference' BIT DEFAULT 0, " +
                "'name' TEXT NOT NULL, " +
                "'author' TEXT NOT NULL, " +
                "'publisher' TEXT, " +
                "'journal' TEXT, " +
                "'edition' INT, " +
                "'editor' TEXT, " +
                "'released' DATE, " +
                "'bestseller' BIT DEFAULT 0, " +
                "'price' INT NOT NULL, " +
                "'located' TEXT NOT NULL, " +
                "'tags' TEXT DEFAULT '', " +
                "'taken_by' INTEGER, " +
                "'taken_when' DATE, " +
                "'due_when' DATE, " +
                "FOREIGN KEY ('taken_by') REFERENCES 'users'('id'), " +
                "CHECK (price>=0));");
    }

    // -------- Terminating access --------
    public static void terminate() throws SQLException {
        if (query != null) query.close();
        if (conn != null) conn.close();
        if (resSet != null) resSet.close();

        System.out.println("Terminated.");
    }


}
