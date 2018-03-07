import java.sql.SQLException;

public class Book implements Document
{
    private int id;

    public static int checkOutTime = 21;
    // with conditions

    Book(int _id) throws SQLException
    {
        if (!Conn.query.executeQuery("SELECT id FROM books WHERE id = " + _id).next())
            id = -1;
        else
            id = _id;
    }

    public boolean add(String[] args, String[] common) throws SQLException
    {
        if (args.length != 8 || common.length != 3)
            return false;
        Conn.query.executeUpdate("INSERT INTO books (type, copy, name, author, publisher, edition, released, reference, bestseller, price, located, tags) " +
                "VALUES ('" + 1 + "','" + args[0] + "','" + args[1] + "','" + args[2] + "','" + args[3] + "','" + args[4] + "','" + args[5] + "','" + args[6] + "','" + args[7] + "','"
                + common[0] + "','" + common[1] + "','" + common[2] + "')");
        Conn.resSet = Conn.query.executeQuery("SELECT max(id) FROM books");
        id = Conn.resSet.getInt("id");
        return true;
    }

    public boolean delete() throws SQLException
    {
        if (Conn.query.executeQuery("SELECT taken_by FROM books WHERE id = " + id).getString("taken_by") != null)
            return false;
        Conn.query.executeQuery("DELETE FROM books WHERE id = " + id);
        return true;
    }

    public boolean modify(String[] args, String[] common) throws SQLException
    {
        if (id == -1 || args.length != 7 || common.length != 3
                || Conn.query.executeQuery("SELECT taken_by FROM books WHERE id = " + id).getString("taken_by") != null)
            return false;
        Conn.query.executeUpdate("UPDATE books "
                + "SET name = " + args[0] + ", author = " + args[1] + ", publisher = " + args[2] + ", edition = " + args[3] + ", released = " + args[4] + ", reference = " + args[5] + ", bestseller = " + args[6]
                + ", price = " + common[0] + ", located = " + common[1] + ", tags = " + common[2]
                + " WHERE id = " + id);
        return true;
    }

    public int returnDocument() throws SQLException
    {
        Conn.resSet = Conn.query.executeQuery("SELECT taken_by FROM books WHERE id = " + id);
        if (!Conn.resSet.next())
            return -1;
        if (Conn.resSet.getString("taken_by") == null || Conn.resSet.getString("taken_by").equals(""))
            return 0;
        Conn.query.executeQuery("UPDATE books SET taken_by = NULL WHERE id = " + id);
        return 1;
    }

    public int checkDocument(int user_id) throws SQLException
    {
        Conn.resSet = Conn.query.executeQuery("SELECT name FROM users WHERE id " + user_id);
        if (!Conn.resSet.next())
            return -1;
        boolean isFaculty = Conn.resSet.getInt("type") == 1;
        Conn.resSet = Conn.query.executeQuery("SELECT taken_by FROM books WHERE id = " + id);
        if (!Conn.resSet.next())
            return -1;
        if (Conn.resSet.getString("taken_by") == null || Conn.resSet.getString("taken_by").equals("")
                || Conn.resSet.getInt("reference") == 1)
            return 0;

        Conn.query.executeQuery("UPDATE books SET taken_by = " + user_id + " WHERE id = " + id);
        Conn.query.executeQuery("UPDATE books SET taken_when = CURRENT_DATE WHERE id = " + id);

        String dueTime = "date('now',";
        if (isFaculty) dueTime += "'+28 day'";
        else if (Conn.resSet.getInt("bestseller") == 1) dueTime += "'+14 day'";
        dueTime += ")";
        Conn.query.executeQuery("UPDATE books SET due_when = " + dueTime + " WHERE id = " + id);
        return 1;
    }

    public void view() throws SQLException
    {
        Conn.resSet = Conn.query.executeQuery("SELECT * FROM books WHERE id = " + id);
        if (Conn.resSet.next()) {
            System.out.println("ID1-" + Conn.resSet.getInt("id"));
            System.out.println("Type: Book");
            System.out.println("Title: " + Conn.resSet.getString("name"));
            System.out.println("Author: " + Conn.resSet.getString("author"));
            System.out.println("Publisher: " + Conn.resSet.getString("publisher"));
            System.out.println("Edition: " + Conn.resSet.getString("edition"));
            System.out.println("Released: " + Conn.resSet.getString("released"));
            System.out.println("Value: " + Conn.resSet.getInt("price") + " rubles.");
            boolean noteMade = false;
            System.out.print("Notes: ");
            if (Conn.resSet.getInt("bestseller") == 1) {
                noteMade = true;
                System.out.print("bestseller");
            }
            if (Conn.resSet.getInt("reference") == 1) {
                if (noteMade) System.out.print(", ");
                System.out.print("reference");
            }
            if (!noteMade)
                System.out.print("none");
            if (Conn.resSet.getString("taken_by") != null)
            {
                System.out.print(" Currently is held by ID");
                System.out.print(Conn.resSet.getInt("taken_by"));
                System.out.print(" since ");
                System.out.print(Conn.resSet.getString("taken_when"));
                System.out.print(", due ");
                System.out.print(Conn.resSet.getString("due_when") + ".");
                System.out.println("");
            }
            System.out.println();
        }
        else
            System.out.println("No such article exists in the database.");
    }

    public int viewHolder() throws SQLException
    {
        Conn.resSet = Conn.query.executeQuery("SELECT taken_by FROM books WHERE id = " + id);
        if (Conn.resSet.next())
        {
            if (Conn.resSet.getString("taken_by") == null)
                return 0;
            else
                return Conn.resSet.getInt("taken_by");
        }
        return -1;
    }

    public int calculateFine() throws SQLException
    {
        Conn.resSet = Conn.query.executeQuery("SELECT due_when, price FROM books WHERE id = " + id);
        if (Conn.resSet.next())
            return Conn.resSet.getInt("price");
        return 0;
    }
}
