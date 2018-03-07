import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Student implements Patron
{
    private int id;

    private static int type = 2;

    Student (int _id)
    {
        id = _id;
    }

    public int getId()
    {
        return id;
    }

    public boolean checkDocument(int doc_type, int doc_id) throws SQLException
    {
        Conn.resSet = Conn.query.executeQuery("SELECT copy, taken_by FROM " + Conn.getDocumentTable(doc_type) + " WHERE id = " + doc_id);
        if (!Conn.resSet.next())
        {
            System.out.println("No such document exists in the library.");
            return false;
        }
        if (Conn.resSet.getString("taken_by") == null)
        {
            System.out.println("This copy is already checked out.");
            return false;
        }

        String copy = Conn.resSet.getString("copy");
        Conn.resSet = Conn.query.executeQuery("SELECT holding FROM users WHERE id = " + id);
        String holding = Conn.resSet.getString("holding");
        if (holding == null)
            holding = "";
        String[] hold = holding.split(" ");
        String[] tId;
        Conn.resSet = Conn.query.executeQuery("SELECT id FROM " + Conn.getDocumentTable(doc_type) + " WHERE copy = " + copy);

        boolean hasCopy = false;
        while (Conn.resSet.next() && !hasCopy)
        {
            for (String i : hold)
            {
                tId = i.split("-");
                if (tId[1].equals(Conn.resSet.getString("id"))) hasCopy = true;
            }
        }

        if (hasCopy)
        {
            System.out.println("User already has a copy of the document.");
            return false;
        }

        Document doc = Conn.getDocumentType(doc_type, doc_id);
        doc.checkDocument(id);

        String sql = "UPDATE users SET holding = ? WHERE id = " + id;
        holding = holding + (holding.isEmpty() ? "" : " ") + doc_type + "-" + doc_id;
        PreparedStatement ps = Conn.conn.prepareStatement(sql);
        ps.setString(1, holding);
        ps.executeUpdate();

        System.out.println("Document successfully checked out!");
        Conn.addEntryToHistory(id, id + " successfully checked out document ID" + doc_type + "-" + doc_id);
        return true;
    }

    public boolean returnDocument(int doc_type, int doc_id) throws SQLException
    {
        Conn.resSet = Conn.query.executeQuery("SELECT * FROM " + Conn.getDocumentTable(doc_type) + " WHERE id = " + doc_id);
        if (!Conn.resSet.next())
        {
            System.out.println("No such document exists in the library.");
            return false;
        }
        if (Conn.resSet.getString("taken_by") == null)
        {
            System.out.println("This copy is already checked out.");
            return false;
        }

        Conn.resSet = Conn.query.executeQuery("SELECT holding FROM users WHERE id = " + id);
        String holding = Conn.resSet.getString("holding");
        String[] hold = holding.split(" ");
        String[] tId;
        int k = -1;

        holding = "";

        for (int i = 0; i < hold.length; i++)
        {
            tId = hold[i].split("-");
            if (tId[1].equals(doc_id +""))
                k = i;
            else
                holding += (holding.isEmpty() ? "" : " ") + hold[i];
        }

        if (k == -1)
        {
            System.out.println("User doesn't have the document!");
            return false;
        }

        Document doc = Conn.getDocumentType(doc_type, doc_id);
        doc.returnDocument();

        String sql = "UPDATE users SET holding = ? WHERE id = " + id;
        PreparedStatement ps = Conn.conn.prepareStatement(sql);
        ps.setString(1, holding);
        ps.executeUpdate();


        System.out.println("Document successfully returned!");
        Conn.addEntryToHistory(id, "ID" + id + " successfully returned document ID" + doc_type + "-" + doc_id);
        return true;
    }

    public void view() throws SQLException
    {
        Conn.resSet = Conn.query.executeQuery("SELECT * FROM users WHERE id = " + id);
        if (Conn.resSet.next()) {
            System.out.println("Name: " + Conn.resSet.getString("name"));
            System.out.println("Address: " + Conn.resSet.getInt("address"));
            System.out.println("Phone Number: " + Conn.resSet.getInt("phone"));
            System.out.println("Lib. card ID: " + Conn.resSet.getInt("id"));
            System.out.println("Type: Student");
            System.out.print("Currently holds ");
            if (Conn.resSet.getString("holding") != null)
                System.out.println(Conn.resSet.getString("holding").split(" ").length + " documents.");
            else
                System.out.println(" nothing.");
        }
        else
            System.out.println("No such user exists in the database.");
    }

    public void viewDocument(int doc_type, int doc_id) throws SQLException
    {
        Document doc = Conn.getDocumentType(doc_type, doc_id);
        if (doc != null)
            doc.view();
        else
            System.out.println("No such document type exists in the database.");
    }

    public void viewHeld() throws SQLException
    {
        Conn.resSet = Conn.query.executeQuery("SELECT * FROM users WHERE id = " + id);
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

    public int calculateFine() throws SQLException
    {
        Conn.resSet = Conn.query.executeQuery("SELECT * FROM users WHERE id = " + id);
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
