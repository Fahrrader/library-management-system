import java.sql.SQLException;

public interface OrdinaryUser extends User
{
    String returnDoc(int id) throws SQLException;

}