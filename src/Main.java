import java.sql.SQLException;
import java.util.Scanner;

public class Main
{
    public static void main (String[] args) throws SQLException, ClassNotFoundException
    {


        Conn.access();
        Conn.createDB();

        Scanner in = new Scanner(System.in);
        String input;

        boolean flag = true;
        while (flag)
        {
            System.out.print("Please, enter your ID: ");
            int id = in.nextInt();
            System.out.println("Please, enter your password: ");
            String pw = in.next();
            if (Support.findAccessLvl(id, pw) != -1)
            {
                int access = Support.findAccessLvl(id, pw);
                user = new CurrentUser(id, pw, access);
                System.out.println("Welcome, ID" + id + "!");
                flag = false;
            }
            else
                System.out.println("User not recognised. Try again?");
        }

        boolean run = true;
        System.out.println("Enter a command - you can always try typing 'help':");
        while (in.hasNextLine() && run)
        {
            input = in.next();
            if (user.access > 0)
            {
                switch (input)
                {
                    case "help":
                        System.out.println("### List of available commands:");
                        System.out.println("see 'doc_id' -- see the Document card. Replace 'doc_id' with 0 to to print the whole lot.");
                        System.out.println("check 'doc_id' -- book a document.");
                        System.out.println("return 'doc_id' -- return a document you hold.");
                        System.out.println("quit -- exit the application.");
                        break;
                    case "check":
                        input = in.next();
                        user.bookDocument(Integer.parseInt(input));
                        break;
                    case "return":
                        input = in.next();
                        user.returnDocument(Integer.parseInt(input));
                        break;
                    case "see":
                        input = in.next();
                        lib.readDocs(Integer.parseInt(input));
                        break;
                    case "quit":
                        System.out.println("Exiting the application...");
                        run = false;
                        break;
                    default:
                        System.out.println("Unrecognised command.");
                }
            }
            else
            {
                switch (input)
                {
                    case "help":
                        System.out.println("### List of available commands:");
                        System.out.println("calculate 'user_id' -- see what books the User has.");
                        System.out.println("see u 'user_id' -- see the User card. Replace 'user_id' with 0 to print the whole lot.");
                        System.out.println("see d 'doc_id' -- see the Document card. Replace 'doc_id' with 0 to to print the whole lot.");
                        System.out.println("add u -- add user to the library.");
                        System.out.println("add d -- add document to the library.");
                        System.out.println("remove u 'user_id -- remove user from the library.");
                        System.out.println("remove d 'doc_id -- remove document from the library.");
                        System.out.println("modify u 'user_id' -- modify User card.");
                        System.out.println("modify d 'doc_id' -- modify Document card.");
                        System.out.println("quit -- exit the application.");
                        break;
                    case "calculate":
                        input = in.next();
                        lib.findHeldDocs(Integer.parseInt(input));
                        break;
                    case "see":
                        input = in.next();
                        if (input.equals("u"))
                        {
                            input = in.next();
                            lib.readUsers(Integer.parseInt(input));
                        }
                        else if (input.equals("d"))
                        {
                            input = in.next();
                            lib.readDocs(Integer.parseInt(input));
                        }
                        else
                            System.out.println("Unrecognised command.");
                        break;
                    case "add":
                        input = in.next();
                        if (input.equals("u"))
                        {
                            System.out.println("Please, enter user's name:");
                            String name = in.next();
                            System.out.println("Please, enter access lvl of this user:");
                            System.out.println("Note: 0 = admin, 1 = faculty, 2 = student.");
                            int lvl = in.nextInt();
                            System.out.println("Please, enter password of this user:");
                            String password = in.next();
                            System.out.println("Please, enter phone number of this user:");
                            String phone = in.next();
                            lib.addUser(name, lvl, password, phone);
                        }
                        else if (input.equals("d"))
                        {
                            System.out.println("Please, enter type of doc:");
                            System.out.println("Note: 1 = book, 2 = journal article, 3 = AV material.");
                            int type = in.nextInt();
                            System.out.println("How many copies is to add:");
                            int copy = in.nextInt();
                            System.out.println("Is this doc reference:");
                            System.out.println("Note: 1 = yes, 0 = no.");
                            int reference = in.nextInt();
                            System.out.println("Title:");
                            String name = in.next();
                            System.out.println("Author:");
                            String author = in.next();
                            System.out.println("Name of publisher:");
                            String publisher = in.next();
                            System.out.println("Name of the journal:");
                            String journal = in.next();
                            System.out.println("Number of the edition:");
                            int edition = in.nextInt();
                            System.out.println("Name of the editor:");
                            String editor = in.next();
                            System.out.println("When it was released:");
                            String released = in.next();
                            System.out.println("Is it bestseller:");
                            System.out.println("Note: 1 = yes, 0 = no.");
                            int bestseller = in.nextInt();
                            System.out.println("Enter the price of the document (in rubles):");
                            int price = in.nextInt();
                            /*System.out.println("Please, enter destination of doc:");
                            String location = in.next();
                            System.out.println("Add some tags for searching this doc");
                            String tags = in.next();*/

                            lib.addDoc(type,copy,reference,name,author,publisher,journal,edition,editor,released,bestseller,price,location,tags);
                        } else
                            System.out.println("Unrecognised command.");
                        break;
                    case "remove":
                        input = in.next();
                        if (input.equals("u"))
                        {
                            input = in.next();
                            lib.remove("users", Integer.parseInt(input));
                        }
                        else if (input.equals("d"))
                        {
                            input = in.next();
                            lib.remove("docs", Integer.parseInt(input));
                        }
                        else
                            System.out.println("Unrecognised command.");
                        break;
                    case "modify":  //ПЕРЕДЕЛАТЬ
                        input = in.next();
                        if (input.equals("u"))
                        {
                            System.out.println("НАДО ДОДЕЛАТЬ");
                        }
                        else if (input.equals("d"))
                        {
                            System.out.println("НАДО ДОДЕЛАТЬ");
                        }
                        else
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

        }
        Conn.terminate();
    }
}
