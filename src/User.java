import java.sql.SQLException;

public class User {
    public boolean readDocs(int id) throws SQLException {
        Conn.resSet = Conn.query.executeQuery("SELECT * FROM docs" + (id == 0 ? "" : " WHERE id = " + id));
        boolean result = false;

        while (Conn.resSet.next()) {
            int i = Conn.resSet.getInt("id");
            String name = Conn.resSet.getString("name");
            String author = Conn.resSet.getString("author");
            int type = Conn.resSet.getInt("type");
            int holder = Conn.resSet.getInt("taken_by");
            java.sql.Date due = Conn.resSet.getDate("due_when");
            System.out.print("ID" + i);
            System.out.print(" " + name);
            System.out.print(type == 1 ? ", book written" : type == 2 ? ", article written" : type == 3 ? ", AV material made" : "");
            System.out.print(" by " + author + ". ");
            if (Conn.resSet.getInt("reference") == 1) System.out.print("Reference material. ");
            if (Conn.resSet.getInt("bestseller") == 1) System.out.print("Current bestseller. ");
            if (Conn.resSet.getString("taken_by") != null && !Conn.resSet.getString("taken_by").isEmpty()) {
                System.out.print("Currently is held by ID" + holder);
                System.out.print(", due " + due + ".");
            }
            System.out.println();
            result = true;
        }
        if (!result) System.out.println("No such document exists in the database.");
        return result;
    }


}
