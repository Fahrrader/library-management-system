import java.sql.SQLException;

public class Book implements Document
{
    private int id;

    public int checkOutTime = 3;
    // with exceptions

    public boolean setId (int _id) throws SQLException
    {
        if (!Conn.query.executeQuery("SELECT id FROM books WHERE id = " + _id).next())
            return false;
        id = _id;
        return true;
    }

    public boolean add(String[] args, String[] common) throws SQLException
    {
        if (args.length != 8 || common.length != 3) return false;
        Conn.query.executeUpdate("INSERT INTO books (type, copy, reference, name, author, publisher, edition, released, bestseller, price, located, tags) " +
                "VALUES ('" + 1 + "','" + args[0] + "','" + args[1] + "','" + args[2] + "','" + args[3] + "','" + args[4] + "','" + args[5] + "','" + args[6] + "','" + args[7] + "','"
                + common[0] + "','" + common[1] + "','" + common[2] + "')");
        Conn.resSet = Conn.query.executeQuery("SELECT max(id) FROM books ");
        id = Conn.resSet.getInt("id");
        return true;
    }

    public boolean delete() throws SQLException
    {
        return true;
    }

    public boolean modify(String[] args, String[] common) throws SQLException
    {
        return false;
    }

    public boolean returnDocument() throws SQLException
    {
        return false;
    }

    public boolean takeDocument(int _id) throws SQLException
    {
        return false;
    }

    public void view() throws SQLException
    {
        Conn.resSet = Conn.query.executeQuery("SELECT FROM books WHERE id = " + id);
        if (Conn.resSet.next()) {
            System.out.print("Book ID3-");
            System.out.print(Conn.resSet.getString("id"));
            System.out.print(" " + Conn.resSet.getString("name"));
            System.out.print(", edition ");
            System.out.print(Conn.resSet.getString("edition"));
            System.out.print(", by " + Conn.resSet.getString("author"));
            System.out.print(" published by ");
            System.out.print(Conn.resSet.getString("publisher"));
            System.out.print(" in ");
            System.out.print(Conn.resSet.getString("released"));
            System.out.print(", with value of " + Conn.resSet.getInt("price"));
            System.out.println(" rubbles. ");
            boolean noteMade = false;
            if (Conn.resSet.getInt("bestseller") == 1)
            {
                System.out.print("Notes: ");
                noteMade = true;
                System.out.print("bestseller");
            }
            if (Conn.resSet.getInt("reference") == 1)
            {
                if (noteMade) System.out.print(", ");
                else System.out.print("Notes: ");
                System.out.print("reference");
            }
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
        Conn.resSet = Conn.query.executeQuery("SELECT FROM books WHERE id = " + id);
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
        Conn.resSet = Conn.query.executeQuery("SELECT FROM books WHERE id = " + id);
        if (Conn.resSet.next())
            return Conn.resSet.getInt("price");
        return 0;
    }
}
