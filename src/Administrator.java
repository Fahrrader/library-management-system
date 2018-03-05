import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Administrator extends User {

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

    public void addUser(String name, int access, String password, String phone) throws SQLException {
        Conn.query.executeUpdate("INSERT INTO users (name, access, password, phone) " +
                "VALUES ('" + name + "','" + access + "','" + password + "','" + phone + "')");

        System.out.println("User added!");
    }

    public void addDocument(int type, String[] args, String[] common) throws SQLException {
        boolean successful = false;
        switch (type) {
            case 1:
                Book book = new Book();
                successful = book.add(args, common);
                break;
            case 2:
                Article article  = new Article();
                successful = article.add(args, common);
                break;
            case 3:
                AudioVideo avm = new AudioVideo();
                successful = avm.add(args, common);
                break;
        }
        if (successful)
            System.out.println("Document added!");
        else
            System.out.println("Something went wrong...");
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