import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Conn conn = new Conn();

        Conn.access();
        Conn.createDB();

        Scanner in = new Scanner(System.in);
        String input;
        boolean run = true;

        System.out.println("Enter command - you can always type 'help':");
        while (in.hasNextLine() && run) {
            input = in.next();
            switch (input) {
                case "help":
                    System.out.println("### List of available commands:");
                    System.out.println("check 'user_id' 'book_id' -- User checks out the document.");
                    System.out.println("calculate 'user_id' -- see what books the User has.");
                    System.out.println("see u 'user_id' -- see the User card. Replace 'user_id' with 0 to print the whole lot.");
                    System.out.println("see d 'doc_id' -- see the Document card. Replace 'doc_id' with 0 to to print the whole lot.");
                    System.out.println("quit -- exit the application.");
                    break;
                case "check":
                    input = in.next();
                    conn.bookDocument(Integer.parseInt(input), Integer.parseInt(in.next()));
                    break;
                case "calculate":
                    input = in.next();
                    conn.findHeldDocs(Integer.parseInt(input));
                    break;
                case "see":
                    input = in.next();
                    if (input.equals("u")) {
                        input = in.next();
                        conn.readUsers(Integer.parseInt(input));
                    } else if (input.equals("d")) {
                        input = in.next();
                        conn.readDocs(Integer.parseInt(input));
                    } else
                        System.out.println("Unrecognised command.");
                    break;
                case "quit":
                    System.out.println("Exiting the application...");
                    run = false;
                    break;
                default:
                    System.out.println("Unrecognised command.");
            }
        }

        Conn.terminate();
    }
}
