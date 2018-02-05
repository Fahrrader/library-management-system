import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Librarian lib = new Librarian();
        Conn.Conn();
        Conn.CreateDB();

        Conn.CloseDB();

    }

}
