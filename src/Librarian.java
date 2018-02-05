import java.sql.SQLException;

public class Librarian implements User {

    private Conn conn = new Conn();

    @Override
    public int find(String name) {
        return 0;
    }

    @Override
    public void return_book(String name) {

    }

    @Override
    public void take_the_book(int id) {

    }

    //------------Adding doc---------------
    public void add_doc(String name, String type, String author, int year) throws SQLException {
        Conn.addDoc(name, type, author, year);
    }

    //-----------Modifying doc-----------------
    public void modify_doc(int id) {

    }

    //-----------Adding User object Faculty-----------
    public void add_faculty(String name, String surname) throws SQLException {
        Conn.addUser(name, surname, 2);
    }

    //-----------Adding User object Librarian-----------
    public void add_librarian(String name, String surname) throws SQLException {
        Conn.addUser(name, surname, 3);
    }

    //-----------Adding User object Student-----------
    public void add_student(String name, String surname) throws SQLException {
        Conn.addUser(name, surname, 1);
    }

    //------------Deleting docs and users-----------------
    public void remove_user_by_id(int id) {
        conn.removeUser_by_id(id);
    }

    public void remove_doc_by_id(int id) {
        conn.removeDoc_by_id(id);
    }

    public void remove_user_by_name(String name, String surname) {
        conn.removeUser_by_name(name, surname);
    }

    public void remove_doc_by_name(String name) {
        conn.removeDoc_by_name(name);
    }
}
