import java.sql.SQLException;

public interface User
{
    String returnDocument(int id) throws SQLException;

    String takeDocument(int id) throws SQLException;

    String view() throws SQLException;

    String viewHeld() throws SQLException;

    String calculateHeld() throws SQLException;
}
