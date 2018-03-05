import java.sql.SQLException;

public interface User
{
    String view() throws SQLException;

    boolean readDocs(int id) throws SQLException;
}
