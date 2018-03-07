import java.sql.SQLException;

public interface Administrator
{
    boolean addDocument(String[] args, String[] common) throws SQLException;

    boolean deleteDocument(int type, int id) throws SQLException;

    boolean modifyDocument(String[] args, String[] common) throws SQLException;

    // add the newly created user to the table
    boolean addUser(String[] args) throws SQLException;

    boolean deleteUser() throws SQLException;

    boolean modifyUser(String[] args) throws SQLException;

    // see summary about a user
    void viewUser(int id) throws SQLException;

    void viewDocument(int type, int id) throws SQLException;

    // see IDs of the held documents of a user
    int viewHeld(int id) throws SQLException;

    // return the total cost of the current fine of a user
    int calculateFine() throws SQLException;
}