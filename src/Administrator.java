import java.sql.SQLException;

public interface Administrator
{
    int getId();

    boolean addUser(String[] args) throws SQLException;

    boolean deleteUser(int id) throws SQLException;

    boolean modifyUser(int id, String[] args) throws SQLException;

    boolean addDocument(String[] args, String[] common) throws SQLException;

    boolean deleteDocument(int type, int id) throws SQLException;

    boolean modifyDocument(int type, int id, String[] args, String[] common) throws SQLException;

    // see summary about a user
    void viewUser(int id) throws SQLException;

    void viewDocument(int type, int id) throws SQLException;

    // see IDs of the held documents of a user
    void viewHeld(int id) throws SQLException;

    // return the total cost of the current fine of a user
    int calculateFine(int id) throws SQLException;
}