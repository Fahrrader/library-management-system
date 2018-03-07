import java.sql.*;

public class Librarian implements Administrator
{
    public static Connection conn;

    private int id;

    private Connection connect() {
        String url = "jdbc:sqlite:library.db";
        conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    Librarian (int _id)
    {
        id = _id;
    }

    public int getId()
    {
        return id;
    }

    public boolean addUser(String args[]) throws SQLException
    {
        Conn.query.executeUpdate("INSERT INTO users (type, name, address, phone, password) " +
                "VALUES ('" + args[0] + "','" + args[1] + "','" + args[2] + "','" + args[2] + "','" + args[3] + "')");
        System.out.println("User added!");
        return true;
    }

    public boolean deleteUser(int user_id) throws SQLException
    {
        if (user_id == id)
        {
            System.out.println("You cannot delete your own account while logged in!");
            return false;
        }
        Conn.resSet = Conn.query.executeQuery("SELECT holding FROM users WHERE id = " + user_id);
        if (!Conn.resSet.next())
        {
            System.out.println("There is no such user in the library.");
            return false;
        }
        if (Conn.resSet.getString("holding") == null)
        {
            System.out.println("Can't delete user; he has not yet returned checked out documents.");
            return false;
        }
        Conn.query.executeQuery("DELETE FROM users WHERE id = " + user_id);
        System.out.println("User is no longer in the system.");
        return true;
    }

    public boolean modifyUser(int user_id, String[] args) throws SQLException {
        if (user_id == id)
        {
            System.out.println("You cannot change the type of your own account while logged in!");
        }
        Conn.resSet = Conn.query.executeQuery("SELECT holding FROM users WHERE id = " + user_id);
        if (!Conn.resSet.next())
        {
            System.out.println("There is no such user in the library.");
            return false;
        }
        if (Conn.resSet.getString("holding") == null)
        {
            System.out.println("Can't modify user; he has not yet returned checked out documents.");
            return false;
        }

        Conn.query.executeUpdate("UPDATE users "
                + "SET name = " + args[0] + (user_id == id ? ", type = " + args[1] + ", " : ", ")
                + "address = " + args[2] + ", phone = " + args[3] + ", password = " + args[4]
                + " WHERE id = " + user_id);

        System.out.println("The user successfully modified!");
        return true;
    }

    public boolean addDocument(String[] args, String[] common) throws SQLException
    {
        Document doc = Conn.getDocumentType(Integer.parseInt(args[0]), 0);
        if (doc == null)
        {
            System.out.println("No such type of document exists in the database.");
            return false;
        }
        if (doc.add(args, common))
        {
            System.out.println("Document added!");
            return true;
        }
        System.out.println("Oops! Something went wrong...");
        return false;
    }

    public boolean deleteDocument(int doc_type, int doc_id) throws SQLException
    {
        Document doc = Conn.getDocumentType(doc_type, doc_id);
        if (doc == null)
        {
            System.out.println("There is no such document in the library.");
            return false;
        }
        if (!doc.delete())
        {
            System.out.println("Can't delete document; it is currently checked out.");
            return false;
        }
        System.out.println("The document is no longer in the system.");
        return true;
    }

    public boolean modifyDocument(int doc_type, int doc_id, String[] args, String[] common) throws SQLException
    {
        Document doc = Conn.getDocumentType(doc_type, doc_id);
        if (doc == null)
        {
            System.out.println("No such document exists in the library.");
            return false;
        }
        if (!doc.modify(args, common))
        {
            System.out.println("Can't modify document; the number of arguments is wrong.");
            return false;
        }
        System.out.println("The document successfully modified!");
        return true;
    }

    public void viewUser(int user_id) throws SQLException
    {
        Conn.resSet = Conn.query.executeQuery("SELECT * FROM users" + (user_id == 0 ? " WHERE id = " + user_id : ""));
        if (Conn.resSet.next()) {
            System.out.println("Name: " + Conn.resSet.getString("name"));
            System.out.println("Address: " + Conn.resSet.getInt("address"));
            System.out.println("Phone Number: " + Conn.resSet.getInt("phone"));
            System.out.println("Lib. card ID: " + Conn.resSet.getInt("id"));
            int user_type = Conn.resSet.getInt("type");
            System.out.println("Type: " + Conn.getUserType(user_type));
            if (user_type != 0)
            {
                System.out.print("Currently holds ");
                if (Conn.resSet.getString("holding") != null)
                    System.out.println(Conn.resSet.getString("holding").split(" ").length + " documents.");
                else
                    System.out.println(" nothing.");
            }
        }
        else
            System.out.println("No such user exists in the database.");
    }

    public void viewDocument(int doc_type, int doc_id) throws SQLException
    {
        Document doc;
        int k = doc_type, n = doc_type;
        /*int p = doc_id;
        if (doc_type == 0)
        {
            k = 1;
            n = Conn.docTypesNum;
        }*/
        for (int i = k; i <= n; i++)
        {
            //Conn.resSet = Conn.query.executeQuery("SELECT * FROM " + Conn.getDocumentTable(i) + (doc_id != 0 ? " WHERE id = " + doc_id : ""));
            doc = Conn.getDocumentType(i, doc_id);
            if (doc != null)
                doc.view();
            else
                System.out.println("No such document type exists in the database.");
        }
    }

    public void viewHeld(int user_id) throws SQLException
    {
        Conn.resSet = Conn.query.executeQuery("SELECT * FROM users WHERE id = " + user_id);
        if (!Conn.resSet.next())
            return;
        System.out.print("ID" + Conn.resSet.getString("id"));
        System.out.print(" currently holds ");
        if (Conn.resSet.getString("holding") == null)
            System.out.println("nothing.");
        else
        {
            String[] hold = Conn.resSet.getString("holding").split(" ");
            String[] tId;
            System.out.println("IDs " + hold + ":");
            for (int i = 0; i < hold.length; i++)
            {
                tId = hold[i].split("-");
                Conn.resSet = Conn.query.executeQuery("SELECT copy, taken_when, due_when FROM " + Conn.getDocumentTable(Integer.parseInt(tId[0])) + " WHERE id = " + tId[1]);
                System.out.println(hold[i]
                        + ", copy #" + Conn.resSet.getString("copy")
                        + ", taken on " + Conn.resSet.getString("taken_when")
                        + ", due " + Conn.resSet.getString("due_when") + "");
            }
        }
    }

    public int calculateFine(int user_id) throws SQLException
    {
        Conn.resSet = Conn.query.executeQuery("SELECT * FROM users WHERE id = " + user_id);
        if (!Conn.resSet.next())
            return -1;
        if (Conn.resSet.getString("holding") == null)
            return 0;
        String[] holding = Conn.resSet.getString("holding").split(" ");
        String[] tId;
        int sum = 0;
        for (String i : holding)
        {
            tId = i.split("-");
            sum += Conn.getDocumentType(Integer.parseInt(tId[0]), Integer.parseInt(tId[1])).calculateFine();
        }
        return sum;
    }
}
