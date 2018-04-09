import java.sql.*;

public class Conn {
    public static Connection conn;
    public static Statement query;
    public static ResultSet resSet;

    public static int docTypesNum = 3;

    // -------- ACCESSING DATABASE ---------
    public static void access() throws ClassNotFoundException, SQLException {
        conn = null;
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:library.db");

        System.out.println("Database accessed!");
    }

    // -------- Creating tables --------
    public static boolean createDB() throws SQLException {
        query = conn.createStatement();
        query.execute("CREATE TABLE IF NOT EXISTS 'users' (" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'name' TEXT NOT NULL, " +
<<<<<<< HEAD
                "'type' INT NOT NULL, " +
                "'address' TEXT NOT NULL, " +
=======
                "'access' INT NOT NULL, " +
>>>>>>> parent of eb94a7d... Я хз
                "'phone' TEXT NOT NULL, " +
                "'password' TEXT NOT NULL, " +
                "'holding' TEXT DEFAULT '');");

        query.execute("CREATE TABLE IF NOT EXISTS 'books' (" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'type' INT NOT NULL, " +
                "'copy' INT NOT NULL, " +
                // ----------------------
                "'name' TEXT NOT NULL, " +
                "'author' TEXT NOT NULL, " +
                "'publisher' TEXT, " +
                "'edition' INT, " +
                "'released' DATE, " +
                "'reference' BIT DEFAULT 0, " +
                "'bestseller' BIT DEFAULT 0, " +
                // ----------------------
                "'price' INT NOT NULL, " +
                "'located' TEXT NOT NULL, " +
                "'tags' TEXT DEFAULT '', " +
                "'taken_by' INTEGER, " +
                "'taken_when' DATE, " +
                "'due_when' DATE, " +
                "FOREIGN KEY ('taken_by') REFERENCES 'users'('id'), " +
                "CHECK (price>=0));");

        query.execute("CREATE TABLE IF NOT EXISTS 'journal_articles' (" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'type' INT NOT NULL, " +
                "'copy' INT NOT NULL, " +
                // -----------------------
                "'name' TEXT NOT NULL, " +
                "'author' TEXT NOT NULL, " +
                "'journal' TEXT, " +
                "'publisher' TEXT, " +
                "'issue' DATE, " +
                "'editor' TEXT, " +
                "'reference' BIT DEFAULT 0, " +
                // ------------------------
                "'price' INT NOT NULL, " +
                "'located' TEXT NOT NULL, " +
                "'tags' TEXT DEFAULT '', " +
                "'taken_by' INTEGER, " +
                "'taken_when' DATE, " +
                "'due_when' DATE, " +
                "FOREIGN KEY ('taken_by') REFERENCES 'users'('id'), " +
                "CHECK (price>=0));");

        query.execute("CREATE TABLE IF NOT EXISTS 'a_v_materials' (" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'type' INT NOT NULL, " +
                "'copy' INT NOT NULL, " +
                // ----------------------
                "'name' TEXT NOT NULL, " +
                "'author' TEXT NOT NULL, " +
                // ----------------------
                "'price' INT NOT NULL, " +
                "'located' TEXT NOT NULL, " +
                "'tags' TEXT DEFAULT '', " +
                "'taken_by' INTEGER, " +
                "'taken_when' DATE, " +
                "'due_when' DATE, " +
                "FOREIGN KEY ('taken_by') REFERENCES 'users'('id'), " +
                "CHECK (price>=0));");

        query.execute("CREATE TABLE IF NOT EXISTS 'history' (" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'key_figure' INT, " +
                "'message' TEXT NOT NULL, " +
                "'time' TIMESTAMP NOT NULL);");

        return !Conn.query.executeQuery("SELECT * FROM users").next();
    }

    public static int requestLogIn (int id, String password) throws SQLException
    {
        Conn.resSet = Conn.query.executeQuery("SELECT type, password FROM users WHERE id = " + id);
        if (!Conn.resSet.next())
            return -1;

        String real_password = Conn.resSet.getString("password");
        if (password.equals(real_password))
            return Conn.resSet.getInt("type");
        return -1;
    }

    public static void addEntryToHistory (int key, String message) throws SQLException
    {
        String sql = "INSERT INTO history (key_figure, message, time) " +
                "VALUES (?, ?, CURRENT_TIMESTAMP )";

        PreparedStatement ps = Conn.conn.prepareStatement(sql);
        ps.setInt(1, key);
        ps.setString(2, message);
        ps.executeUpdate();
    }

    public static Document getDocumentType (int type, int id) throws SQLException
    {
        switch (type)
        {
            case 1:
                return new Book(id);
            case 2:
                return new Article(id);
            case 3:
                return new AudioVideo(id);
            default:
                return null;
        }
    }

    public static String getDocumentTable (int type)
    {
        switch (type)
        {
            case 1:
                return "books";
            case 2:
                return "journal_articles";
            case 3:
                return "a_v_materials";
            default:
                return "";
        }
    }

    public static String getUserType (int type)
    {
        switch (type)
        {
            case 0:
                return "Librarian";
            case 1:
                return "Faculty";
            case 2:
                return "Student";
            default:
                return "";
        }
    }

    // -------- Terminating access --------
    public static void terminate() throws SQLException
    {
        if (query != null) query.close();
        if (conn != null) conn.close();
        if (resSet != null) resSet.close();

        System.out.println("Terminated.");
    }


}
