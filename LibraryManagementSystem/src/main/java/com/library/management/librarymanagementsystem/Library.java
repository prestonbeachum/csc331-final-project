import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.util.random;

public class Library {

    public static void populateInventory(Book[] books) {
        Book[] books = new Book[50];
        Random rand = new Random();

        String[][] bookData = {
                {"To Kill a Mockingbird", "Harper Lee", "Fiction"},
                {"1984", "George Orwell", "Dystopian"},
                {"Pride and Prejudice", "Jane Austen", "Romance"},
                {"The Great Gatsby", "F. Scott Fitzgerald", "Classic"},
                {"The Hobbit", "J.R.R. Tolkien", "Fantasy"},
                {"The Catcher in the Rye", "J.D. Salinger", "Coming-of-Age"},
                {"Moby Dick", "Herman Melville", "Adventure"},
                {"War and Peace", "Leo Tolstoy", "Historical"},
                {"Brave New World", "Aldous Huxley", "Science Fiction"},
                {"The Lord of the Rings", "J.R.R. Tolkien", "Fantasy"},
                {"The Da Vinci Code", "Dan Brown", "Mystery"},
                {"Harry Potter and the Sorcerer's Stone", "J.K. Rowling", "Fantasy"},
                {"The Hunger Games", "Suzanne Collins", "Dystopian"},
                {"Jane Eyre", "Charlotte Brontë", "Romance"},
                {"Crime and Punishment", "Fyodor Dostoevsky", "Philosophical"},
                {"The Alchemist", "Paulo Coelho", "Adventure"},
                {"The Chronicles of Narnia", "C.S. Lewis", "Fantasy"},
                {"Dracula", "Bram Stoker", "Horror"},
                {"The Shining", "Stephen King", "Horror"},
                {"The Book Thief", "Markus Zusak", "Historical Fiction"},
                {"Frankenstein", "Mary Shelley", "Gothic"},
                {"Les Misérables", "Victor Hugo", "Historical"},
                {"The Giver", "Lois Lowry", "Dystopian"},
                {"Animal Farm", "George Orwell", "Satire"},
                {"Wuthering Heights", "Emily Brontë", "Gothic"},
                {"The Kite Runner", "Khaled Hosseini", "Drama"},
                {"A Tale of Two Cities", "Charles Dickens", "Historical"},
                {"The Picture of Dorian Gray", "Oscar Wilde", "Philosophical"},
                {"Great Expectations", "Charles Dickens", "Classic"},
                {"The Fault in Our Stars", "John Green", "Young Adult"},
                {"Life of Pi", "Yann Martel", "Adventure"},
                {"Gone Girl", "Gillian Flynn", "Thriller"},
                {"Memoirs of a Geisha", "Arthur Golden", "Historical"},
                {"The Girl with the Dragon Tattoo", "Stieg Larsson", "Crime"},
                {"Catch-22", "Joseph Heller", "Satire"},
                {"Fahrenheit 451", "Ray Bradbury", "Dystopian"},
                {"Slaughterhouse-Five", "Kurt Vonnegut", "Science Fiction"},
                {"A Game of Thrones", "George R.R. Martin", "Fantasy"},
                {"The Secret Garden", "Frances Hodgson Burnett", "Children's"},
                {"The Handmaid's Tale", "Margaret Atwood", "Dystopian"},
                {"The Road", "Cormac McCarthy", "Post-apocalyptic"},
                {"A Wrinkle in Time", "Madeleine L'Engle", "Science Fantasy"},
                {"The Time Traveler’s Wife", "Audrey Niffenegger", "Romance"},
                {"The Color Purple", "Alice Walker", "Historical Fiction"},
                {"The Bell Jar", "Sylvia Plath", "Psychological Fiction"},
                {"The Outsiders", "S.E. Hinton", "Young Adult"},
                {"Beloved", "Toni Morrison", "Historical Fiction"},
                {"The Maze Runner", "James Dashner", "Dystopian"},
                {"Dune", "Frank Herbert", "Science Fiction"},
                {"The Perks of Being a Wallflower", "Stephen Chbosky", "Coming-of-Age"}
        };

        for (int i = 0; i < books.length; i++) {
            String title = bookData[i][0];
            String author = bookData[i][1];
            String genre = bookData[i][2];
            boolean isCheckedOut = rand.nextBoolean();
            double price = 10 + (50 - 10) * rand.nextDouble(); // Random price between $10 and $50

            books[i] = new Book(title, author, genre, isCheckedOut, price);
        }

        try (FileWriter writer = new FileWriter("inventory.csv")) {
            writer.write("Title,Author,Genre,Checked Out,Date Checked Out,Price\n");
            for (Book b : books) {
                writer.write(String.format("\"%s\",\"%s\",\"%s\",%s,%s,%.2f\n",
                        b.getTitle(),
                        b.getAuthor(),
                        b.getGenre(),
                        b.isCheckedOut(),
                        b.getDateCheckedOut() != null ? "\"" + b.getDateCheckedOut() + "\"" : "",
                        b.getPrice()));
            }
            System.out.println("Inventory written to inventory.csv");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public static void populateUsers(User[] users) {

    }

    public static void checkingOut(User user, Book book) {
        if (user.hasOverdue()) {
            Book[] usersBooks = user.getBooks();
            for (Book book : usersBooks) {
                if (book.overdue) {
                    System.out.printf("The following book is overdue: %s", book.getTitle())
                }
            }
            System.out.println("The user has overdue books and is unable to check out more items until they are returned or cost of items is paid.")
        }

        if (book.isCheckedOut) {
            System.out.println("This book is already checked out")
        }
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader("people.csv"))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\n");
                for (String value : values) {
                    System.out.print(value + " ");
                }
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}