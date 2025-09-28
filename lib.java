import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;


public class LibraryManagementSystem {
    public static void main(String[] args) {
        Library library = new Library();
        Librarian librarian = new Librarian("bfgbgg", "alice@lib.com", "1234567890");
        StudentMember student = new StudentMember("John", "john@student.com", "1111111111");

        library.registerMember(librarian);
        library.registerMember(student);

        Book book1 = new Book("the java", "Joshua Bloch", "Programming");
        library.addBook(book1);

        library.issueBook(book1, student);
        library.viewIssuedBooks(student);
    }
}

public class Book {
    private UUID bookID;
    private String title;
    private String author;
    private String genre;
    private boolean isIssued;
    private Member issuedTo;
    private LocalDate dueDate;
    private Queue<Member> reservationQueue;

    public Book(String title, String author, String genre) {
        this.bookID = UUID.randomUUID();
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.isIssued = false;
        this.reservationQueue = new LinkedList<>();
    }

    public UUID getBookID() { return bookID; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getGenre() { return genre; }
    public boolean isIssued() { return isIssued; }
    public Member getIssuedTo() { return issuedTo; }
    public LocalDate getDueDate() { return dueDate; }
    public Queue<Member> getReservationQueue() { return reservationQueue; }

    public void issueTo(Member member, int allowedDays) {
        this.isIssued = true;
        this.issuedTo = member;
        this.dueDate = LocalDate.now().plusDays(allowedDays);
    }

    public void returnBook() {
        this.isIssued = false;
        this.issuedTo = null;
        this.dueDate = null;
    }

    @Override
    public String toString() {
        return title + " by " + author + " (" + genre + ")" +
                (isIssued ? " - Issued to " + issuedTo.getName() : " - Available");
    }
}

abstract class Member {
    protected UUID memberID;
    protected String name;
    protected String email;
    protected String phone;
    protected int maxBooksAllowed;
    protected List<Book> currentlyIssuedBooks;

    public Member(String name, String email, String phone, int maxBooksAllowed) {
        this.memberID = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.maxBooksAllowed = maxBooksAllowed;
        this.currentlyIssuedBooks = new ArrayList<>();
    }

    public String getName() { return name; }
    public List<Book> getCurrentlyIssuedBooks() { return currentlyIssuedBooks; }
    public abstract int getMaxAllowedDays();
    public abstract String getMemberType();
}

class StudentMember extends Member {
    public StudentMember(String name, String email, String phone) {
        super(name, email, phone, 3);
    }
    public int getMaxAllowedDays() { return 14; }
    public String getMemberType() { return "Student"; }
}

class TeacherMember extends Member {
    public TeacherMember(String name, String email, String phone) {
        super(name, email, phone, 5);
    }
    public int getMaxAllowedDays() { return 30; }
    public String getMemberType() { return "Teacher"; }
}

class GuestMember extends Member {
    public GuestMember(String name, String email, String phone) {
        super(name, email, phone, 1);
    }
    public int getMaxAllowedDays() { return 7; }
    public String getMemberType() { return "Guest"; }
}

class Librarian extends Member {
    public Librarian(String name, String email, String phone) {
        super(name, email, phone, Integer.MAX_VALUE);
    }
    public int getMaxAllowedDays() { return Integer.MAX_VALUE; }
    public String getMemberType() { return "Librarian"; }
}

class Library {
    private List<Book> books;
    private List<Member> members;

    public Library() {
        this.books = new ArrayList<>();
        this.members = new ArrayList<>();
    }

    public void addBook(Book book) {
        for (Book b : books) {
            if (b.getBookID().equals(book.getBookID())) {
                throw new IllegalArgumentException("Duplicate book ID detected!");
            }
        }
        books.add(book);
    }

    public void removeBook(Book book) {
        if (book.isIssued()) {
            throw new IllegalStateException("Cannot remove book that is currently issued.");
        }
        books.remove(book);
    }

    public void registerMember(Member member) {
        for (Member m : members) {
            if (m.email.equals(member.email) || m.phone.equals(member.phone)) {
                throw new IllegalArgumentException("Member already exists!");
            }
        }
        members.add(member);
    }

    public List<Book> searchBooks(String keyword) {
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                book.getAuthor().toLowerCase().contains(keyword.toLowerCase()) ||
                book.getGenre().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(book);
            }
        }
        return result;
    }

    public void issueBook(Book book, Member member) {
        if (book.isIssued()) {
            reserveBook(book, member);
            return;
        }
        if (member.getCurrentlyIssuedBooks().size() >= member.maxBooksAllowed) {
            throw new IllegalStateException(member.getMemberType() + " has reached book limit!");
        }
        book.issueTo(member, member.getMaxAllowedDays());
        member.getCurrentlyIssuedBooks().add(book);
    }

    public void returnBook(Book book, Member member) {
        if (!member.getCurrentlyIssuedBooks().contains(book)) {
            throw new IllegalStateException("This book was not issued by this member!");
        }
        member.getCurrentlyIssuedBooks().remove(book);
        book.returnBook();

        if (!book.getReservationQueue().isEmpty()) {
            Member next = book.getReservationQueue().poll();
            issueBook(book, next);
        }
    }

    public void reserveBook(Book book, Member member) {
        if (!book.isIssued()) {
            throw new IllegalStateException("Book is available, no need to reserve.");
        }
        book.getReservationQueue().add(member);
    }

    public void viewIssuedBooks(Member member) {
        for (Book book : member.getCurrentlyIssuedBooks()) {
            long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), book.getDueDate());
            System.out.println(book.getTitle() + " - Due in " + daysLeft + " days");
        }
    }

    public List<Book> viewOverdueBooks() {
        List<Book> overdueBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.isIssued() && LocalDate.now().isAfter(book.getDueDate())) {
                overdueBooks.add(book);
            }
        }
        return overdueBooks;
    }
}
