import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.util.random;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Library {

    private static ObservableList<Book> books = FXCollections.observableArrayList();


    public static void populateInventory() {
        //Book[] books = new Book[50];
        //Random rand = new Random();

        //List<Book> books = new ArrayList<>();
        Random rand = new Random();

        String[][] bookData = {
                {"To Kill a Mockingbird", "Harper Lee", "Fiction", 115},
                {"1984", "George Orwell", "Dystopian"},
                {"Pride and Prejudice", "Jane Austen", "Romance", 134},
                {"The Great Gatsby", "F. Scott Fitzgerald", "Classic", 098},
                {"The Hobbit", "J.R.R. Tolkien", "Fantasy", 069},
                {"The Catcher in the Rye", "J.D. Salinger", "Coming-of-Age", 034},
                {"Moby Dick", "Herman Melville", "Adventure", 102},
                {"War and Peace", "Leo Tolstoy", "Historical", 043},
                {"Brave New World", "Aldous Huxley", "Science Fiction", 099},
                {"The Lord of the Rings", "J.R.R. Tolkien", "Fantasy", 052},
                {"The Da Vinci Code", "Dan Brown", "Mystery", 013},
                {"Harry Potter and the Sorcerer's Stone", "J.K. Rowling", "Fantasy", 124},
                {"The Hunger Games", "Suzanne Collins", "Dystopian", 108},
                {"Jane Eyre", "Charlotte Brontë", "Romance", 105},
                {"Crime and Punishment", "Fyodor Dostoevsky", "Philosophical", 032},
                {"The Alchemist", "Paulo Coelho", "Adventure", 019},
                {"The Chronicles of Narnia", "C.S. Lewis", "Fantasy", 111},
                {"Dracula", "Bram Stoker", "Horror", 027},
                {"The Shining", "Stephen King", "Horror", 094},
                {"The Book Thief", "Markus Zusak", "Historical Fiction", 145},
                {"Frankenstein", "Mary Shelley", "Gothic", 005},
                {"Les Misérables", "Victor Hugo", "Historical", 007},
                {"The Giver", "Lois Lowry", "Dystopian", 009},
                {"Animal Farm", "George Orwell", "Satire", 050},
                {"Wuthering Heights", "Emily Brontë", "Gothic", 090},
                {"The Kite Runner", "Khaled Hosseini", "Drama", 070},
                {"A Tale of Two Cities", "Charles Dickens", "Historical", 020},
                {"The Picture of Dorian Gray", "Oscar Wilde", "Philosophical", 011},
                {"Great Expectations", "Charles Dickens", "Classic", 001},
                {"The Fault in Our Stars", "John Green", "Young Adult", 120},
                {"Life of Pi", "Yann Martel", "Adventure", 107},
                {"Gone Girl", "Gillian Flynn", "Thriller", 085},
                {"Memoirs of a Geisha", "Arthur Golden", "Historical", 084},
                {"The Girl with the Dragon Tattoo", "Stieg Larsson", "Crime", 130},
                {"Catch-22", "Joseph Heller", "Satire", 122},
                {"Fahrenheit 451", "Ray Bradbury", "Dystopian", 133},
                {"Slaughterhouse-Five", "Kurt Vonnegut", "Science Fiction", 111},
                {"A Game of Thrones", "George R.R. Martin", "Fantasy", 144},
                {"The Secret Garden", "Frances Hodgson Burnett", "Children's", 104},
                {"The Handmaid's Tale", "Margaret Atwood", "Dystopian", 137},
                {"The Road", "Cormac McCarthy", "Post-apocalyptic", 006},
                {"A Wrinkle in Time", "Madeleine L'Engle", "Science Fantasy", 008},
                {"The Time Traveler’s Wife", "Audrey Niffenegger", "Romance", 118},
                {"The Color Purple", "Alice Walker", "Historical Fiction", 096},
                {"The Bell Jar", "Sylvia Plath", "Psychological Fiction", 092},
                {"The Outsiders", "S.E. Hinton", "Young Adult", 045},
                {"Beloved", "Toni Morrison", "Historical Fiction", 056},
                {"The Maze Runner", "James Dashner", "Dystopian", 073},
                {"Dune", "Frank Herbert", "Science Fiction", 091},
                {"The Perks of Being a Wallflower", "Stephen Chbosky", "Coming-of-Age", 067}
        };

        for (int i = 0; i < books.length; i++) {
            String title = bookData[i][0];
            String author = bookData[i][1];
            String genre = bookData[i][2];
            int book_id = Integer.parseInt(bookData[i][3]);


            //boolean isCheckedOut = rand.nextBoolean();
            //double price = 10 + (50 - 10) * rand.nextDouble(); // Random price between $10 and $50

            books.add(new Book(author, title, book_id, genre));
        }

        updateInventory();
    }


    private static void updateInventory(){
        try (FileWriter writer = new FileWriter("LibraryManagementSystem/src/main/resources/Inventory.csv")) {
            writer.write("Author, Title, ISBN, Genre\n");
            for (Book b : books) {
                writer.write(String.format("\"%s\",\"%s\",\"%.2d\",%s\"\n",
                        b.getAuthor(),
                        b.getTitle(),
                        b.getBook_id,
                        b.getGenre()));

            }
            System.out.println("Inventory written to inventory.csv");
            return true;
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
            return false;
        }


    }




    public static boolean addBook(String author, String title, int book_id, String genre) {
        Book newBook = new Book(author, title, book_id, genre);

        // Add to the ArrayList
        books.add(newBook);

        // Update the CSV file
        return updateInventoryFile();
    }



    public static boolean loadBooksFromCSV() {
        try (BufferedReader reader = new BufferedReader(new FileReader("LibraryManagementSystem/src/main/resources/Inventory.csv"))) {
            // Clear current book list
            books.clear();

            // Skip header line
            String line = reader.readLine();

            // Read book entries
            while ((line = reader.readLine()) != null) {
                // Simple CSV parsing (doesn't handle commas in quoted fields properly)
                // For a robust solution, consider using a CSV library
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String author = parts[0].replace("\"", "");
                    String title = parts[1].replace("\"", "");
                    int bookId = Integer.parseInt(parts[2].replace("\"", ""));
                    String genre = parts[3].replace("\"", "");

                    books.add(new Book(author, title, bookId, genre));
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
            return false;
        }
    }



    public static boolean removeBook(int book_id) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getBook_id() == book_id) {
                books.remove(i);
                updateInventoryFile();
                return true;
            }
        }
        return false;
    }





    public static ObservableList<Book> getBooks() {
        return books;
    }



    public static List<Book> searchByTitle(String titleQuery) {
        List<Book> results = new ArrayList<>();
        String query = titleQuery.toLowerCase();

        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(query)) {
                results.add(book);
            }
        }

        return results;
    }




    public static List<Book> searchBooks(String authorQuery) {
        List<Book> results = new ArrayList<>();
        String query = authorQuery.toLowerCase();

        for (Book book : books) {
            if (book.getAuthor().toLowerCase().contains(query) || book.getTitle().toLowerCase().contains(query) || book.getGenre) {
                results.add(book);
            }
        }

        return results;
    }

    public void editBook(Book book) {


    }

}




