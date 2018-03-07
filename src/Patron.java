import java.sql.SQLException;

public interface Patron
{
    int getId();

    // add the document to user's 'holding'
    boolean checkDocument(int type, int id) throws SQLException;

    // return the document as user
    boolean returnDocument(int type, int id) throws SQLException;

    // see summary about the user
    void view() throws SQLException;

    void viewDocument(int type, int id) throws SQLException;

    // see IDs of the held documents
    void viewHeld() throws SQLException;

    // return the total cost of the current fine
    int calculateFine() throws SQLException;
}
