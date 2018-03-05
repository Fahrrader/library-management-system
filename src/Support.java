import java.sql.SQLException;
import java.util.Objects;

public class Support {

    public static int findAccessLvl(int id, String password) throws SQLException {
        Conn.resSet = Conn.query.executeQuery("SELECT access, password FROM users WHERE id = " + id);
        if (!Conn.resSet.next()) {
            System.out.println("No such user exists.");
            return -1;
        }
        String password1 = Conn.resSet.getString("password");
        if (Objects.equals(password1, password)){
            return Conn.resSet.getInt("access");
        }
        else{
            System.out.println("Wrong password");
            return -1;
        }
    }

}
