import java.sql.*;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Conn {
    private static Connection conn;
    private static Statement query;
    private static ResultSet resSet;

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
    public static void createDB() throws SQLException {
        query = conn.createStatement();
        query.execute("CREATE TABLE IF NOT EXISTS 'users' (" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'name' TEXT NOT NULL, " +
                "'access' INT NOT NULL, " +
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

    // -------- Adding users and documents --------
    public void addUser(String name, int access, String phone) throws SQLException {
        query.executeUpdate("INSERT INTO users (name, access, phone) " +
                "VALUES ('" + name + "','" + access + "','" + phone + "')");

        System.out.println("User added!");
    }

    //type, copy, reference, name, author, publisher, journal, edition, editor, released, bestseller, price, located, tags, taken_by, taken_when, due_when
    public void addDoc(int type, int copy, int reference, String name, String author, String publisher, String journal, int edition, String editor, String released, int bestseller, int price, String located, String tags) throws SQLException {
        query.executeUpdate("INSERT INTO docs (type, copy, reference, name, author, publisher, journal, edition, editor, released, bestseller, price, located, tags) " +
                "VALUES ('" + type + "','" + copy + "','" + reference + "','" + name + "','" + author + "','" + publisher + "','" + journal + "','" + edition + "','" + editor + "','" + released + "','" + bestseller + "','" + price + "','" + located + "','" + tags + "')");

        System.out.println("Document added!");
    }

    // ------- Deleting users and documents --------
    public void remove(String tab, int id) {
        String sql = "DELETE FROM " + tab + " WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            ps.executeUpdate();

            System.out.println("Item was removed.");
        } catch (SQLException e) {
            System.out.println("No such item exists.");
        }
    }

    public void findHeldDocs(int id) throws SQLException {
        resSet = query.executeQuery("SELECT id, name, holding FROM users WHERE id = " + id);
        String[] holding = resSet.getString("holding").split(" ");
        System.out.print("ID" + resSet.getString("id") + " " + resSet.getString("name") + " currently holds ");
        if (!holding[0].isEmpty()) System.out.print("ID(s) ");
        for (int i = 0; i < holding.length - 1; i++) {
            System.out.print(holding[i] + ",");
        }
        if (holding[0].isEmpty()) System.out.print("nothing");
        System.out.println(holding[holding.length - 1] + ".");
    }

    public void bookDocument(int user, int doc) throws SQLException {
        resSet = query.executeQuery("SELECT * FROM users WHERE id = " + user);
        boolean next = true;
        String[] holdi = new String[0];
        if (resSet.next())
            holdi = resSet.getString("holding").split(" ");
        else {
            next = false;
            System.out.println("No such user exists in the database.");
        }

        resSet = query.executeQuery("SELECT * FROM docs WHERE id = " + doc);
        int id = 0;
        if (next && resSet.next() && resSet.getInt("reference") == 0)
            id = resSet.getInt("copy");
        else {
            next = false;
            System.out.println("No such document is free or exists in the database.");
        }
        if (next) {
            boolean docSet = resSet.getString("taken_by") == null;
            resSet = query.executeQuery("SELECT id FROM docs WHERE copy = " + id);

            boolean copy = false;

            while (resSet.next()) {
                for (String i : holdi) {
                    if (i.equals(resSet.getString("id"))) copy = true;
                }
            }

            if (copy) System.out.println("User already has one copy of the document.");
            else if (!docSet) System.out.println("The document has already been checked out.");

            if (docSet && !copy) {
                resSet = query.executeQuery("SELECT * FROM users WHERE id = " + user);
                String holding = resSet.getString("holding");
                int lvl = resSet.getInt("access");

                String sql = "UPDATE users SET holding = ? WHERE id = " + user;
                holding = holding + (holding.isEmpty() ? "" : " ") + doc;
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, holding);
                ps.executeUpdate();

                resSet = query.executeQuery("SELECT * FROM docs WHERE id = " + doc);
                int type = resSet.getInt("type");
                int bestseller = resSet.getInt("bestseller");
                query.executeUpdate("UPDATE docs SET taken_by = " + user + " WHERE id = " + doc);

                Date now = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                query.executeUpdate("UPDATE docs SET taken_when = " + sdf.format(now) + " WHERE id = " + doc);
                Calendar c = Calendar.getInstance();
                c.setTime(now);
                if (type == 1 && lvl <= 1) c.add(Calendar.DATE, 28);
                else if (type == 1 && bestseller == 1) c.add(Calendar.DATE, 14);
                else if (type == 1) c.add(Calendar.DATE, 21);
                else c.add(Calendar.DATE, 14);
                now = c.getTime();

                query.executeUpdate("UPDATE docs SET due_when = " + sdf.format(now) + " WHERE id = " + doc);

                System.out.println("Document has been successfully checked out!");
            }
        }
    }

    // -------- Printing users and documents table ---------
    public boolean readUsers(int id) throws SQLException {
        resSet = query.executeQuery("SELECT * FROM users" + (id == 0 ? "" : " WHERE id = " + id));
        boolean result = false;

        while (resSet.next()) {
            int i = resSet.getInt("id");
            int lvl = resSet.getInt("access");
            String name = resSet.getString("name");
            String phone = resSet.getString("phone");
            String holding = resSet.getString("holding");
            System.out.print("ID" + i);
            System.out.print(lvl == 0 ? " Librarian" : lvl == 1 ? " Faculty" : lvl == 2 ? " Student" : "");
            System.out.print(" " + name);
            System.out.print(", phone " + phone);
            if (!holding.isEmpty()) System.out.print(", holds ID(s) " + holding);
            System.out.println(".");
            result = true;
        }
        if (!result) System.out.println("No such user exists in the database.");
        return result;
    }

    public boolean readDocs(int id) throws SQLException {
        resSet = query.executeQuery("SELECT * FROM docs" + (id == 0 ? "" : " WHERE id = " + id));
        boolean result = false;

        while (resSet.next()) {
            int i = resSet.getInt("id");
            String name = resSet.getString("name");
            String author = resSet.getString("author");
            int type = resSet.getInt("type");
            int holder = resSet.getInt("taken_by");
            java.sql.Date due = resSet.getDate("due_when");
            System.out.print("ID" + i);
            System.out.print(" " + name);
            System.out.print(type == 1 ? ", book written" : type == 2 ? ", article written" : type == 3 ? ", AV material made" : "");
            System.out.print(" by " + author + ". ");
            if (resSet.getInt("reference") == 1) System.out.print("Reference material. ");
            if (resSet.getInt("bestseller") == 1) System.out.print("Current bestseller. ");
            if (resSet.getString("taken_by") != null && !resSet.getString("taken_by").isEmpty()) {
                System.out.print("Currently is held by ID" + holder);
                System.out.print(", due " + due + ".");
            }
            System.out.println();
            result = true;
        }
        if (!result) System.out.println("No such document exists in the database.");
        return result;
    }


    // -------- Terminating access --------
    public static void terminate() throws SQLException {
        if (query != null) query.close();
        if (conn != null) conn.close();
        if (resSet != null) resSet.close();

        System.out.println("Terminated.");
    }

}
