import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Conn conn = new Conn();

        Conn.access();
        Conn.createDB();

        Conn.readUsers();
        //Conn.readDocs();
        Conn.terminate();
    }
}
