import java.sql.SQLException;
import java.util.Scanner;

public class Main
{
    public static void main (String[] args) throws SQLException, ClassNotFoundException
    {
        Conn.access();
        boolean logged = Conn.createDB();

        Scanner in = new Scanner(System.in);
        String input;

        Patron user = new Faculty(-1);
        Administrator librarian = new Librarian(-1);

        if (logged)
        {
            String[] newUser = {"0", "admin", "...", "...", "1234"};
            librarian.addUser(newUser);
            librarian = new Librarian(1);
            Conn.addEntryToHistory(1, "A librarian entered the empty library.");
        }

        while (!logged)
        {
            System.out.print("Please, enter your ID: ");
            int id = in.nextInt();
            System.out.print("Please, enter your password: ");
            String pw = in.next();
            int access = Conn.requestLogIn(id, pw);
            logged = access != -1;
            switch (access)
            {
                case 0:
                    librarian = new Librarian(id);
                    break;
                case 1:
                    user = new Faculty(id);
                    break;
                case 2:
                    user = new Student(id);
                    break;
                default:
                    System.out.println("User ID or password not recognised. Try again?");
            }
            if (logged)
                System.out.println("Welcome, ID" + id + "!");
        }


        boolean run = true;

        System.out.println("Enter a command - you can always try typing 'help':");
        while (in.hasNextLine() && run)
        {
            input = in.next();
            if (user.getId() != -1)
            {
                switch (input)
                {
                    case "help":
                        System.out.println("### List of available commands:");
                        System.out.println("book 'doc_type' 'doc_id' -- book a document.");
                        System.out.println("return 'doc_type' 'doc_id' -- return a document you hold.");
                        System.out.println("see 'doc_type' 'doc_id' -- see the Document card. Replace 'doc-type' 'doc_id' with 0 0 to print the whole lot.");
                        System.out.println("profile -- see your library profile. ");
                        System.out.println("quit -- exit the application.");
                        System.out.println("-- Codes for document types:");
                        System.out.println("1 - book, 2 - journal article, 3 - A/V material");
                        break;
                    case "book":
                        user.checkDocument(in.nextInt(), in.nextInt());
                        break;
                    case "return":
                        user.returnDocument(in.nextInt(), in.nextInt());
                        break;
                    case "see":
                        user.viewDocument(in.nextInt(), in.nextInt());
                        break;
                    case "profile":
                        user.view();
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
                        System.out.println("see d 'doc_type' 'doc_id' -- see the Document card. Replace 'doc_type' 'doc_id' with 0 0 to to print the whole lot.");
                        System.out.println("add u -- add user to the library.");
                        System.out.println("add d 'doc_type' -- add document to the library.");
                        System.out.println("remove u 'user_id -- remove user from the library.");
                        System.out.println("remove d 'doc_type' 'doc_id -- remove document from the library.");
                        System.out.println("modify u 'user_id' -- modify User card.");
                        System.out.println("modify d 'doc_type' 'doc_id' -- modify Document card.");
                        System.out.println("quit -- exit the application.");
                        System.out.println("-- Codes for document types:");
                        System.out.println("1 - book, 2 - journal article, 3 - A/V material");
                        break;
                    case "calculate":
                        input = in.next();
                        librarian.viewHeld(Integer.parseInt(input));
                        break;
                    case "o":
                        Conn.addEntryToHistory(1, "my message my message");
                        break;
                    case "see":
                        input = in.next();
                        if (input.equals("u"))
                        {
                            input = in.next();
                            librarian.viewUser(Integer.parseInt(input));
                        }
                        else if (input.equals("d"))
                        {
                            librarian.viewDocument(in.nextInt(), in.nextInt());
                        }
                        else
                            System.out.println("Unrecognised command.");
                        break;
                    case "add":
                        input = in.next();
                        if (input.equals("u"))
                        {
                            String[] newUser = new String[5];
                            System.out.println("Enter the type of user:");
                            System.out.println("Note: 0 = librarian, 1 = faculty, 2 = student.");
                            newUser[0] = in.nextInt()+"";
                            System.out.println("Enter the user's name:");
                            newUser[1] = in.next();
                            System.out.println("Enter the address of the user:");
                            newUser[2] = in.next();
                            System.out.println("Enter the phone number of the user:");
                            newUser[3] = in.next();
                            System.out.println("Enter the new password for the user:");
                            newUser[4] = in.next();
                            librarian.addUser(newUser);
                        }
                        else if (input.equals("d"))
                        {
                            input = in.nextInt()+"";
                            String[] nd = {};
                            String[] cd = new String[3];
                            switch (input)
                            {
                                case "1":
                                    nd = new String[9];
                                    nd[0] = "1";
                                    System.out.println("Of what document this one is a copy:");
                                    nd[1] = in.nextInt()+"";
                                    System.out.println("Title:");
                                    nd[2] = in.next();
                                    System.out.println("Author:");
                                    nd[3] = in.next();
                                    System.out.println("Publisher:");
                                    nd[4] = in.next();
                                    System.out.println("Edition #:");
                                    nd[5] = in.nextInt()+"";
                                    System.out.println("The date of release:");
                                    nd[6] = in.next();
                                    System.out.println("Is it a reference book:");
                                    System.out.println("Note: 1 = yes, 0 = no.");
                                    nd[7] = in.nextInt()+"";
                                    System.out.println("Is it a bestseller:");
                                    System.out.println("Note: 1 = yes, 0 = no.");
                                    nd[8] = in.nextInt()+"";
                                    System.out.println("Price in rubles:");
                                    cd[0] = in.nextInt()+"";
                                    System.out.println("The location in the library:");
                                    cd[1] = in.next();
                                    System.out.println("Tags:");
                                    System.out.println("Note: separate with commas.");
                                    cd[2] = in.next();
                                    librarian.addDocument(nd, cd);
                                    break;
                                case "2":
                                    nd = new String[10];
                                    nd[0] = "2";
                                    System.out.println("Of what document this one is a copy:");
                                    nd[1] = in.nextInt()+"";
                                    System.out.println("Title:");
                                    nd[2] = in.next();
                                    System.out.println("Author:");
                                    nd[3] = in.next();
                                    System.out.println("Journal's name:");
                                    nd[4] = in.next();
                                    System.out.println("Publisher:");
                                    nd[4] = in.next();
                                    System.out.println("Issue of (date):");
                                    nd[6] = in.next();
                                    System.out.println("Editor:");
                                    nd[7] = in.next();
                                    System.out.println("Is it a reference article:");
                                    System.out.println("Note: 1 = yes, 0 = no.");
                                    nd[8] = in.nextInt()+"";
                                    System.out.println("Price in rubles:");
                                    cd[0] = in.nextInt()+"";
                                    System.out.println("The location in the library:");
                                    cd[1] = in.next();
                                    System.out.println("Tags:");
                                    System.out.println("Note: separate with commas.");
                                    cd[2] = in.next();
                                    librarian.addDocument(nd, cd);
                                    break;
                                case "3":
                                    nd = new String[4];
                                    nd[0] = "3";
                                    System.out.println("Of what document this one is a copy:");
                                    nd[1] = in.nextInt()+"";
                                    System.out.println("Title:");
                                    nd[2] = in.next();
                                    System.out.println("Author:");
                                    nd[3] = in.next();
                                    System.out.println("Price in rubles:");
                                    cd[0] = in.nextInt()+"";
                                    System.out.println("The location in the library:");
                                    cd[1] = in.next();
                                    System.out.println("Tags:");
                                    System.out.println("Note: separate with commas.");
                                    cd[2] = in.next();
                                    librarian.addDocument(nd, cd);
                                    break;
                                default:
                                    System.out.println("Unrecognised command.");
                            }
                        } else
                            System.out.println("Unrecognised command.");
                        break;
                    case "remove":
                        input = in.next();
                        if (input.equals("u"))
                        {
                            librarian.deleteUser(in.nextInt());
                        }
                        else if (input.equals("d"))
                        {
                            librarian.deleteDocument(in.nextInt(), in.nextInt());
                        }
                        else
                            System.out.println("Unrecognised command.");
                        break;
                    case "modify":
                        input = in.next();
                        if (input.equals("u"))
                        {
                            String[] newUser = new String[5];
                            System.out.println("Enter the type of user:");
                            System.out.println("Note: 0 = librarian, 1 = faculty, 2 = student.");
                            newUser[0] = in.nextInt()+"";
                            System.out.println("Enter the user's name:");
                            newUser[1] = in.next();
                            System.out.println("Enter the address of the user:");
                            newUser[2] = in.next();
                            System.out.println("Enter the phone number of the user:");
                            newUser[3] = in.next();
                            System.out.println("Enter the new password for the user:");
                            newUser[4] = in.next();
                            librarian.modifyUser(in.nextInt(), newUser);
                        }
                        else if (input.equals("d"))
                        {
                            input = in.nextInt()+"";
                            String[] nd;
                            String[] cd = new String[3];
                            switch (input) {
                                case "1":
                                    nd = new String[7];
                                    System.out.println("Title:");
                                    nd[0] = in.next();
                                    System.out.println("Author:");
                                    nd[1] = in.next();
                                    System.out.println("Publisher:");
                                    nd[2] = in.next();
                                    System.out.println("Edition #:");
                                    nd[3] = in.nextInt() + "";
                                    System.out.println("The date of release:");
                                    nd[4] = in.next();
                                    System.out.println("Is it a reference book:");
                                    System.out.println("Note: 1 = yes, 0 = no.");
                                    nd[5] = in.nextInt() + "";
                                    System.out.println("Is it a bestseller:");
                                    System.out.println("Note: 1 = yes, 0 = no.");
                                    nd[6] = in.nextInt() + "";
                                    System.out.println("Price in rubles:");
                                    cd[0] = in.nextInt() + "";
                                    System.out.println("The location in the library:");
                                    cd[1] = in.next();
                                    System.out.println("Tags:");
                                    System.out.println("Note: separate with commas.");
                                    cd[2] = in.next();
                                    librarian.modifyDocument(in.nextInt(), in.nextInt(), nd, cd);
                                    break;
                                case "2":
                                    nd = new String[8];
                                    System.out.println("Title:");
                                    nd[0] = in.next();
                                    System.out.println("Author:");
                                    nd[1] = in.next();
                                    System.out.println("Journal's name:");
                                    nd[2] = in.next();
                                    System.out.println("Publisher:");
                                    nd[3] = in.next();
                                    System.out.println("Issue of (date):");
                                    nd[4] = in.next();
                                    System.out.println("Editor:");
                                    nd[5] = in.next();
                                    System.out.println("Is it a reference article:");
                                    System.out.println("Note: 1 = yes, 0 = no.");
                                    nd[6] = in.nextInt() + "";
                                    System.out.println("Price in rubles:");
                                    cd[0] = in.nextInt() + "";
                                    System.out.println("The location in the library:");
                                    cd[1] = in.next();
                                    System.out.println("Tags:");
                                    System.out.println("Note: separate with commas.");
                                    cd[2] = in.next();
                                    librarian.modifyDocument(in.nextInt(), in.nextInt(), nd, cd);
                                    break;
                                case "3":
                                    nd = new String[2];
                                    System.out.println("Title:");
                                    nd[0] = in.next();
                                    System.out.println("Author:");
                                    nd[1] = in.next();
                                    System.out.println("Price in rubles:");
                                    cd[0] = in.nextInt() + "";
                                    System.out.println("The location in the library:");
                                    cd[1] = in.next();
                                    System.out.println("Tags:");
                                    System.out.println("Note: separate with commas.");
                                    cd[2] = in.next();
                                    librarian.modifyDocument(in.nextInt(), in.nextInt(), nd, cd);
                                    break;
                                default:
                                    System.out.println("Unrecognised command.");
                            }
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
