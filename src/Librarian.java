import java.sql.*;

public class Librarian implements Administrator
{
    public String add (String name, int access, String phone) throws SQLException
    {
        return "INSERT INTO users (name, access, phone) " +
                "VALUES ('" + name + "','" + access + "','" + phone + "')";
    }

    public String delete (int id) throws SQLException
    {
        return "DELETE FROM users WHERE id = ?";
    }

    public String modify (int id) throws SQLException
    {
        return "";
    }


    public String returnDocument(int id) throws SQLException {
        return null;
    }


    public String takeDocument(int id) throws SQLException {
        return null;
    }

    public String view () throws SQLException
    {
        return "";
    }


    public String viewHeld() throws SQLException {
        return null;
    }


    public String calculateHeld() throws SQLException {
        return null;
    }
}
