public interface Patron {
    int find (String name);

    void returnBook (String name);

    void takeBook (int id);
}
