import java.sql.*;

public class Librarian implements Administrator
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

    public boolean addUser(String args[]) throws SQLException
    {
        Conn.query.executeUpdate("INSERT INTO users (type, name, address, phone, password) " +
                "VALUES ('" + args[0] + "','" + args[1] + "','" + args[2] + "','" + args[2] + "','" + args[3] + "')");
        System.out.println("User added!");
        return true;
    }

    public boolean deleteUser(int user_id) throws SQLException
    {
        if (Conn.query.executeQuery("SELECT holding FROM users WHERE id = " + user_id).getString("holding") != null)
        {
            System.out.println("Can't delete user; he has not yet returned checked out documents.");
            return false;
        }
        Conn.query.executeQuery("DELETE FROM users WHERE id = " + user_id);
        System.out.println("User is no longer in the system.");
        return true;
    }

    public boolean addDocument(int type, String[] args, String[] common) throws SQLException
    {
        Document doc = Conn.documentType(type, 0);
        if (doc == null)
        {
            System.out.println("No such type exists in the database.");
            return false;
        }
        if (doc.add(args, common))
        {
            System.out.println("Document added!");
            return true;
        }
        System.out.println("Oops! Something went wrong...");
        return false;
    }

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

    String modify(int id) throws SQLException {
        return null;
    }
}
