import java.sql.SQLException;

public interface Document
{
    // add the newly created document to the table
    String add() throws SQLException;

    String delete() throws SQLException;

    String modify() throws SQLException;

    // return the document and return the status string (successful, no such document, etc.)
    String returnDocument() throws SQLException;

    // set the document to 'taken' and return the status string
    String takeDocument() throws SQLException;

    // see summary about the doc
    String view() throws SQLException;

    // see id of the holder
    String viewHolder() throws SQLException;

    // return cost of the fine if returned
    String calculateFine() throws SQLException;
}
