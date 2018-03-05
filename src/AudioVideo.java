import java.sql.SQLException;

public class AudioVideo implements Document
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

    public boolean add (String[] args, String[] common) throws SQLException
    {
        if (args.length != 3) return false;
        if (common.length != 3) return false;
        Conn.query.executeUpdate("INSERT INTO a_v_materials (type, copy, name, author, price, located, tags) " +
                "VALUES ('" + 3 + "','" + args[0] + "','" + args[1] + "','" + args[2] + "','"
                + common[0] + "','" + common[1] + "','" + common[2] + "')");
        Conn.resSet = Conn.query.executeQuery("SELECT max(id) FROM a_v_materials");
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
        Conn.resSet = Conn.query.executeQuery("SELECT FROM a_v_materials WHERE id = " + id);
        if (Conn.resSet.next()) {
            System.out.print("Audio/Video Material ID3-");
            System.out.print(Conn.resSet.getInt("id"));
            System.out.print(" " + Conn.resSet.getString("name"));
            System.out.print(" by " + Conn.resSet.getString("author"));
            System.out.print(" with value of " + Conn.resSet.getInt("price"));
            System.out.print(" rubbles.");
            if (Conn.resSet.getString("taken_by") != null)
            {
                System.out.print(" Currently is held by ID");
                System.out.print(Conn.resSet.getInt("taken_by"));
                System.out.print(" since ");
                System.out.print(Conn.resSet.getString("taken_when"));
                System.out.print(", due ");
                System.out.print(Conn.resSet.getString("due_when") + ".");
            }
            System.out.println();
        }
        else
            System.out.println("No such material exists in the database.");
    }

    public int viewHolder() throws SQLException
    {
        Conn.resSet = Conn.query.executeQuery("SELECT FROM a_v_materials WHERE id = " + id);
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
        Conn.resSet = Conn.query.executeQuery("SELECT FROM a_v_materials WHERE id = " + id);
        if (Conn.resSet.next())
            return Conn.resSet.getInt("price");
        return 0;
    }
}
