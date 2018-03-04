import java.sql.SQLException;

public interface Administrator{
    String add(String name, int access, String phone) throws SQLException;

    String delete(int id) throws SQLException;

    String modify(int id) throws SQLException;

    String view() throws SQLException;
}