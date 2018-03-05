import java.sql.*;

public class Librarian extends Administrator
{
    public static Connection conn;


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

    public String add(String name, int access, String phone) throws SQLException {
        return null;
    }

    public String delete(int id) throws SQLException {
        return null;
    }

    public String modify(int id) throws SQLException {
        return null;
    }

    public String view() throws SQLException {
        return null;
    }

    // -------- Adding users and documents --------

    public void addUser(String name, int access, String phone) throws SQLException {
        Conn.query.executeUpdate("INSERT INTO users (name, access, phone) " +
                "VALUES ('" + name + "','" + access + "','" + phone + "')");

        System.out.println("User added!");
    }

    public void addDoc(int type, int copy, int reference, String name, String author, String publisher, String journal, int edition, String editor, String released, int bestseller, int price, String located, String tags) throws SQLException {
        Conn.query.executeUpdate("INSERT INTO docs (type, copy, reference, name, author, publisher, journal, edition, editor, released, bestseller, price, located, tags) " +
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


    // ------- Finding doc -------

    public void findHeldDocs(int id) throws SQLException {
        Conn.resSet = Conn.query.executeQuery("SELECT id, name, holding FROM users WHERE id = " + id);
        if (!Conn.resSet.next()) {
            System.out.println("No such user exists.");
            return;
        }
        String[] holding = Conn.resSet.getString("holding").split(" ");
        System.out.print("ID" + Conn.resSet.getString("id") + " " + Conn.resSet.getString("name") + " currently holds ");
        if (!holding[0].isEmpty()) System.out.print("ID(s) ");
        for (int i = 0; i < holding.length - 1; i++) {
            System.out.print(holding[i] + ",");
        }
        if (holding[0].isEmpty()) System.out.print("nothing");
        System.out.println(holding[holding.length - 1] + ".");
    }

    public boolean readUsers(int id) throws SQLException {
        Conn.resSet = Conn.query.executeQuery("SELECT * FROM users" + (id == 0 ? "" : " WHERE id = " + id));
        boolean result = false;
        
        while (Conn.resSet.next()) {
            int i = Conn.resSet.getInt("id");
            int lvl = Conn.resSet.getInt("access");
            String name = Conn.resSet.getString("name");
            String phone = Conn.resSet.getString("phone");
            String holding = Conn.resSet.getString("holding");
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


}
