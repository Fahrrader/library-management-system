import java.sql.SQLException;

public class Librarian implements User {

    private Conn conn = new Conn();

    public int find(String name) {
        return 0;
    }

    public void return_book(String name) { }

    public void take_the_book(int id) { }

    // ----------- Adding document ---------------
    public void add_doc(String name, String type, String author, int year) throws SQLException {
        Conn.addDoc(name, type, author, year);
    }

    // ----------- Modding document --------------
    public void modify_doc(int id) {

    }

    // ----------- Adding user -------------------
    public void add_user(String name, int access, int phone) throws SQLException {
        Conn.addUser(name, access, phone);
    }

    // ----------- Deleting users and documents --
    public void remove_user_by_id(int id) {
        conn.removeUser_by_id(id);
    }

    public void remove_doc_by_id(int id) {
        conn.removeDoc_by_id(id);
    }

    public void remove_user_by_name(String name, String surname) {
        conn.removeUser_by_name(name);
    }

    public void remove_doc_by_name(String name) {
        conn.removeDoc_by_name(name);
    }
}
