import java.sql.SQLException;
import java.util.Scanner;

public class Main
{
    public static void main (String[] args) throws SQLException, ClassNotFoundException
    {
        Librarian lib = new Librarian();
        Student st = new Student();

        Conn.access();
        Conn.createDB();

        Scanner in = new Scanner(System.in);
        String input;

        String[] array = new String[3];
        boolean flag = true;
        while (flag)
        {
            System.out.print("Please, enter your ID: ");
            String id = in.next();
            System.out.println("Please, enter your password: ");
            String pw = in.next();
            if (Support.findAccessLvl(Integer.parseInt(id), pw) != -1)
            {
                String access = String.valueOf(Support.findAccessLvl(Integer.parseInt(id), pw));
                array[0]=id;
                array[1]=pw;
                array[2]=access;
                flag = false;
                System.out.println("Welcome, ID" + id + "!");
            }
            else
                System.out.println("User not recognised. Try again?");

        }

        boolean run = true;
        System.out.println("Enter a command - you can always try typing 'help':");
        while (in.hasNextLine() && run)
        {
            input = in.next();
            if (Integer.parseInt(array[2]) == 1 || Integer.parseInt(array[2]) == 2)
            {
                switch (input)
                {
                    case "help":
                        System.out.println("### List of available commands:");
                        System.out.println("check 'book_id' -- User checks out the document.");
                        System.out.println("see d 'doc_id' -- see the Document card. Replace 'doc_id' with 0 to to print the whole lot.");
                        System.out.println("return 'doc_id' -- НАДО ДОДЕЛАТЬ");
                        System.out.println("quit -- exit the application.");
                        break;
                    case "check":
                        input = in.next();
                        st.bookDocument(Integer.parseInt(array[0]), Integer.parseInt(input));
                        break;
                    case "see":
                        input = in.next();
                        if (input.equals("d"))
                        {
                            input = in.next();
                            lib.readDocs(Integer.parseInt(input));
                        } else
                            System.out.println("Unrecognised command.");
                        break;
                    case "quit":
                        System.out.println("Exiting the application...");
                        run = false;
                        break;
                    case "return":
                        System.out.println("НАДО ДОДЕЛАТЬ");
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
                        System.out.println("return 'doc_id' -- НАДО ДОДЕЛАТЬ");
                        System.out.println("add u -- allows you to add user");
                        System.out.println("add d -- allows you to add doc");
                        System.out.println("remove u 'user_id -- remove user");
                        System.out.println("remove d 'doc_id -- remove doc");
                        System.out.println("modify u 'user_id' -- allows you to modify user");
                        System.out.println("modify d 'doc_id' -- allows you to modify doc");
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
                        } else if (input.equals("d")) {
                            input = in.next();
                            lib.readDocs(Integer.parseInt(input));
                        } else
                            System.out.println("Unrecognised command.");
                        break;
                    case "return":
                        System.out.println("НАДО ДОДЕЛАТЬ");
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
                            System.out.println("Note: 1 = book, 2 = article, 3 = AV material.");
                            int type = in.nextInt();
                            System.out.println("How many copies you want to add:");
                            int copy = in.nextInt();
                            System.out.println("Is this doc reference:");
                            System.out.println("Note: 1 = yes, 0 = no.");
                            int reference = in.nextInt();
                            System.out.println("Please, enter name of this doc:");
                            String name = in.next();
                            System.out.println("Please, enter name of author of this doc:");
                            String author = in.next();
                            System.out.println("Please, enter name of publisher of this doc:");
                            String publisher = in.next();
                            System.out.println("ФАРХАД, ЧТО ТАКОЕ JOURNAL?:");
                            String journal = in.next();
                            System.out.println("Please, edition of this doc:");
                            int edition = in.nextInt();
                            System.out.println("Please, enter name of editor of this doc:");
                            String editor = in.next();
                            System.out.println("Please, enter date of released of this doc:");
                            String released = in.next();
                            System.out.println("Is this doc bestseller:");
                            System.out.println("Note: 1 = yes, 0 = no.");
                            int bestseller = in.nextInt();
                            System.out.println("Please, enter the prise of one copy of this doc (in rubles):");
                            int prise = in.nextInt();
                            System.out.println("Please, enter destination of doc:");
                            String location = in.next();
                            System.out.println("Add some tags for searching this doc");
                            String tags = in.next();

                            lib.addDoc(type,copy,reference,name,author,publisher,journal,edition,editor,released,bestseller,prise,location,tags);
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

//Здесь
