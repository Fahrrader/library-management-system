import java.sql.SQLException;

public class Article implements Document
{
    private int id;

    public static int checkOutTime = 14;

    Article (int _id) throws SQLException
    {
        if (!Conn.query.executeQuery("SELECT id FROM journal_articles WHERE id = " + _id).next())
            id = -1;
        else
            id = _id;
    }

    public boolean add(String[] args, String[] common) throws SQLException
    {
        if (args.length != 9 || common.length != 3)
            return false;
        Conn.query.executeUpdate("INSERT INTO journal_articles (type, copy, name, author, journal, publisher, issue, editor, released, reference, price, located, tags) " +
                "VALUES ('" + 2 + "','" + args[0] + "','" + args[1] + "','" + args[2] + "','" + args[3] + "','" + args[4] + "','" + args[5] + "','" + args[6] + "','" + args[7] + "','" + args[8] + "','"
                + common[0] + "','" + common[1] + "','" + common[2] + "')");
        Conn.resSet = Conn.query.executeQuery("SELECT max(id) FROM journal_articles");
        id = Conn.resSet.getInt("id");
        return true;
    }

    public boolean delete() throws SQLException
    {
        if (Conn.query.executeQuery("SELECT taken_by FROM journal_articles WHERE id = " + id).getString("taken_by") != null)
            return false;
        Conn.query.executeQuery("DELETE FROM journal_articles WHERE id = " + id);
        return true;
    }

    public boolean modify(String[] args, String[] common) throws SQLException
    {
        if (id == -1 || args.length != 8 || common.length != 3
                || Conn.query.executeQuery("SELECT taken_by FROM journal_articles WHERE id = " + id).getString("taken_by") != null)
            return false;
        Conn.query.executeUpdate("UPDATE journal_articles "
                + "SET name = " + args[0] + ", author = " + args[1] + ", journal = " + args[2] + ", publisher = " + args[3] + ", issue = " + args[4] + ", editor = " + args[5] + ", released = " + args[6] + ", reference = " + args[7]
                + ", price = " + common[0] + ", located = " + common[1] + ", tags = " + common[2]
                + " WHERE id = " + id);
        return true;
    }

    public int returnDocument() throws SQLException
    {
        Conn.resSet = Conn.query.executeQuery("SELECT taken_by FROM journal_articles WHERE id = " + id);
        if (!Conn.resSet.next())
            return -1;
        if (Conn.resSet.getString("taken_by") == null || Conn.resSet.getString("taken_by").equals(""))
            return 0;
        Conn.query.executeQuery("UPDATE journal_articles SET taken_by = NULL WHERE id = " + id);
        return 1;
    }

    public int checkDocument(int user_id) throws SQLException
    {
        Conn.resSet = Conn.query.executeQuery("SELECT name FROM users WHERE id " + user_id);
        if (!Conn.resSet.next())
            return -1;
        Conn.resSet = Conn.query.executeQuery("SELECT taken_by FROM journal_articles WHERE id = " + id);
        if (!Conn.resSet.next())
            return -1;
        if (Conn.resSet.getString("taken_by") == null || Conn.resSet.getString("taken_by").equals("")
                || Conn.resSet.getInt("reference") == 1)
            return 0;

        Conn.query.executeQuery("UPDATE journal_articles SET taken_by = " + user_id + " WHERE id = " + id);
        Conn.query.executeQuery("UPDATE journal_articles SET taken_when = CURRENT_DATE WHERE id = " + id);
        String dueTime = "date('now','+14 day')";
        Conn.query.executeQuery("UPDATE journal_articles SET due_when = " + dueTime + " WHERE id = " + id);
        return 1;
    }

    public void view() throws SQLException
    {
        Conn.resSet = Conn.query.executeQuery("SELECT * FROM journal_articles WHERE id = " + id);
        if (Conn.resSet.next()) {
            System.out.println("ID3-" + Conn.resSet.getInt("id"));
            System.out.println("Type: Journal Article");
            System.out.println("Title: " + Conn.resSet.getString("name"));
            System.out.println("Author: " + Conn.resSet.getString("author"));
            System.out.println("Editor: " + Conn.resSet.getString("editor"));
            System.out.println("Journal: " + Conn.resSet.getString("journal"));
            System.out.println("Date: " + Conn.resSet.getString("released"));
            System.out.println("Value: " + Conn.resSet.getInt("price") + " rubles.");
            System.out.print("Notes: ");
            if (Conn.resSet.getInt("reference") == 1)
                System.out.print("reference");
            else
                System.out.print("none");
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
        Conn.resSet = Conn.query.executeQuery("SELECT taken_by FROM journal_articles WHERE id = " + id);
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
        Conn.resSet = Conn.query.executeQuery("SELECT due_when, price FROM journal_articles WHERE id = " + id);
        if (Conn.resSet.next())
            return Conn.resSet.getInt("price");
        return 0;
    }
}
