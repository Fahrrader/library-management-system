import java.sql.SQLException;

public interface Document {
    // add the newly created document to the table
    boolean add(String[] args, String[] common) throws SQLException;

    boolean delete() throws SQLException;

    boolean modify(String[] args, String[] common) throws SQLException;

    // return the document and return the status string (successful, no such document, etc.)
    boolean returnDocument() throws SQLException;

    // set the document to 'taken' and return the status string
    boolean takeDocument(int id) throws SQLException;

    // see summary about the doc
    void view() throws SQLException;

    // see id of the holder
    int viewHolder() throws SQLException;

    // return cost of the fine if returned
    int calculateFine() throws SQLException;
}
