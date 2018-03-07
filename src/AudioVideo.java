import java.sql.SQLException;

public class AudioVideo implements Document
{
    private int id;

    public static int checkOutTime = 14;

    AudioVideo (int _id) throws SQLException
    {
        if (!Conn.query.executeQuery("SELECT id FROM a_v_materials WHERE id = " + _id).next())
            id = -1;
        else
            id = _id;
    }

    public boolean add (String[] args, String[] common) throws SQLException
    {
        if (args.length != 3 || common.length != 3)
            return false;
        Conn.query.executeUpdate("INSERT INTO a_v_materials (type, copy, name, author, price, located, tags) " +
                "VALUES ('" + 3 + "','" + args[0] + "','" + args[1] + "','" + args[2] + "','"
                + common[0] + "','" + common[1] + "','" + common[2] + "')");
        Conn.resSet = Conn.query.executeQuery("SELECT max(id) FROM a_v_materials");
        id = Conn.resSet.getInt("id");
        Conn.addEntryToHistory(id, "ID3-" + id + " was added to the system");
        return true;
    }

    public boolean delete() throws SQLException
    {
        if (Conn.query.executeQuery("SELECT taken_by FROM a_v_materials WHERE id = " + id).getString("taken_by") != null)
            return false;
        Conn.query.executeQuery("DELETE FROM a_v_materials WHERE id = " + id);
        Conn.addEntryToHistory(id, "ID3-" + id + " was removed from the system");
        return true;
    }

    public boolean modify(String[] args, String[] common) throws SQLException
    {
        if (id == -1 || args.length != 2 || common.length != 3
                || Conn.query.executeQuery("SELECT taken_by FROM a_v_materials WHERE id = " + id).getString("taken_by") != null)
            return false;
        Conn.query.executeUpdate("UPDATE a_v_materials "
                + "SET name = " + args[0] + ", author = " + args[1] // -type, copy
                + ", price = " + common[0] + ", located = " + common[1] + ", tags = " + common[2]
                + " WHERE id = " + id);
        Conn.addEntryToHistory(id, "ID3-" + id + " was modified");
        return true;
    }

    public int returnDocument() throws SQLException
    {
        Conn.resSet = Conn.query.executeQuery("SELECT taken_by FROM a_v_materials WHERE id = " + id);
        if (!Conn.resSet.next())
            return -1;
        if (Conn.resSet.getString("taken_by") == null || Conn.resSet.getString("taken_by").equals(""))
            return 0;
        Conn.query.executeQuery("UPDATE a_v_materials SET taken_by = NULL WHERE id = " + id);
        Conn.addEntryToHistory(id, "ID3-" + id + " was returned");
        return 1;
    }

    public int checkDocument(int user_id) throws SQLException
    {
        Conn.resSet = Conn.query.executeQuery("SELECT name FROM users WHERE id " + user_id);
        if (!Conn.resSet.next())
            return -1;
        Conn.resSet = Conn.query.executeQuery("SELECT taken_by FROM a_v_materials WHERE id = " + id);
        if (!Conn.resSet.next())
            return -1;
        if (Conn.resSet.getString("taken_by") == null || Conn.resSet.getString("taken_by").equals(""))
            return 0;
        Conn.query.executeQuery("UPDATE a_v_materials SET taken_by = " + user_id + " WHERE id = " + id);
        Conn.query.executeQuery("UPDATE a_v_materials SET taken_when = CURRENT_DATE WHERE id = " + id);
        String dueTime = "date('now','+14 day')";
        Conn.query.executeQuery("UPDATE a_v_materials SET due_when = " + dueTime + " WHERE id = " + id);
        Conn.addEntryToHistory(id, "ID3-" + id + " was checked out");
        return 1;
    }

    public void view() throws SQLException
    {
        Conn.resSet = Conn.query.executeQuery("SELECT * FROM a_v_materials WHERE id = " + id);
        if (Conn.resSet.next()) {
            System.out.println("ID3-" + Conn.resSet.getInt("id"));
            System.out.println("Type: Audio/Video Material");
            System.out.println("Title: " + Conn.resSet.getString("name"));
            System.out.println("Authors: " + Conn.resSet.getString("author"));
            System.out.println("Value: " + Conn.resSet.getInt("price") + " rubles.");
            if (Conn.resSet.getString("taken_by") != null)
            {
                System.out.print("Currently is held by ID");
                System.out.print(Conn.resSet.getInt("taken_by"));
                System.out.print(" since ");
                System.out.print(Conn.resSet.getString("taken_when"));
                System.out.print(", due ");
                System.out.print(Conn.resSet.getString("due_when") + ".");
                System.out.println();
            }
        }
        else
            System.out.println("No such material exists in the database.");
    }

    public int viewHolder() throws SQLException
    {
        Conn.resSet = Conn.query.executeQuery("SELECT taken_by FROM a_v_materials WHERE id = " + id);
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
        Conn.resSet = Conn.query.executeQuery("SELECT due_when, price FROM a_v_materials WHERE id = " + id);
        if (Conn.resSet.next())
            return Conn.resSet.getInt("price");
        return 0;
    }
}
