import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Librarian lab = new Librarian();
        Conn.Conn();
        Conn.CreateDB();
        /*lab.add_doc("DSA","book", "Adil Khan", 2016);
        lab.add_doc("Оловянный солдатик", "book", "Ne pomny", 1900);
        lab.add_faculty("Farhad", "Khakimov");
        lab.add_librarian("Игорь", "Лоскутников");
        lab.add_student("Christos", "Bentas");
        lab.remove_user_by_id(3);
        lab.remove_user_by_name("Игорь", "Лоскутников");
        lab.remove_doc_by_id(4);
        lab.remove_doc_by_name("DSA");*/
        Conn.ReadDBUsers();
        Conn.ReadDBDocs();
        Conn.CloseDB();

    }

}
