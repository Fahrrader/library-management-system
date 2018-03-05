import java.sql.SQLException;

public class Article implements Document
{
    private int id;

    public int checkOutTime = 2;

    public boolean setId (int _id) throws SQLException
    {
        if (!Conn.query.executeQuery("SELECT id FROM journal_articles WHERE id = " + _id).next())
            return false;
        id = _id;
        return true;
    }

    public boolean add(String[] args, String[] common) throws SQLException
    {
        if (args.length != 9) return false;
        if (common.length != 3) return false;
        Conn.query.executeUpdate("INSERT INTO journal_articles (type, copy, reference, name, author, journal, publisher, issue, editor, released, price, located, tags) " +
                "VALUES ('" + 2 + "','" + args[0] + "','" + args[1] + "','" + args[2] + "','" + args[3] + "','" + args[4] + "','" + args[5] + "','" + args[6] + "','" + args[7] + "','" + args[8] + "','"
                + common[0] + "','" + common[1] + "','" + common[2] + "')");
        Conn.resSet = Conn.query.executeQuery("SELECT max(id) FROM journal_articles");
        id = Conn.resSet.getInt("id");
        return true;
    }

    public boolean delete() throws SQLException
    {
        return false;
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
        Conn.resSet = Conn.query.executeQuery("SELECT FROM journal_articles WHERE id = " + id);
        if (Conn.resSet.next()) {
            System.out.println("ID3-" + Conn.resSet.getInt("id"));
            System.out.println("Type: Journal Article");
            System.out.println("Title: " + Conn.resSet.getString("name"));
            System.out.println("Author: " + Conn.resSet.getString("author"));
            System.out.println("Editor: " + Conn.resSet.getString("editor"));
            System.out.println("Journal: " + Conn.resSet.getString("journal"));
            System.out.println("Date: " + Conn.resSet.getString("released"));
            System.out.println("Value: " + Conn.resSet.getInt("price") + " rubbles.");
            if (Conn.resSet.getInt("reference") == 1)
            {
                System.out.print("Note: ");
                System.out.print("reference");
            }
            if (Conn.resSet.getString("taken_by") != null)
            {
                System.out.println();
                System.out.print(" Currently is held by ID");
                System.out.print(Conn.resSet.getInt("taken_by"));
                System.out.print(" since ");
                System.out.print(Conn.resSet.getString("taken_when"));
                System.out.print(", due ");
                System.out.print(Conn.resSet.getString("due_when") + ".");
                System.out.println();
            }
            System.out.println();
        }
        else
            System.out.println("No such article exists in the database.");
    }

    public int viewHolder() throws SQLException
    {
        Conn.resSet = Conn.query.executeQuery("SELECT FROM journal_articles WHERE id = " + id);
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
        Conn.resSet = Conn.query.executeQuery("SELECT FROM journal_articles WHERE id = " + id);
        if (Conn.resSet.next())
            return Conn.resSet.getInt("price");
        return 0;
    }
}
