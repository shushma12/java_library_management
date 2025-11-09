package com.library.bean;

import com.library.dao.LibraryDAO;
import com.library.model.Book;
import com.library.model.BorrowTransaction;
import com.library.model.Member;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Named
@SessionScoped
public class LibraryBean implements Serializable {

    private LibraryDAO dao = new LibraryDAO();

    // Entities for Forms
    private Book newBook = new Book();
    private Member newMember = new Member();

    // Selection variables for Issue Book (MUST match XHTML exactly)
    private Long selectedBookId;
    private Long selectedMemberId;
    private LocalDate dueDate = LocalDate.now().plusDays(14); // Default 2 weeks from now

    // Data Lists (Cache)
    private List<Book> books;
    private List<Member> members;
    private List<BorrowTransaction> activeTrans;

    @PostConstruct
    public void init() {
        refreshData();
    }

    public void refreshData() {
        books = dao.getAllBooks();
        members = dao.getAllMembers();
        activeTrans = dao.getActiveTransactions();
    }

    // --- ACTION METHODS ---
    public String addBook() {
        dao.saveBook(newBook);
        newBook = new Book(); // Reset form
        refreshData();
        return "home";
    }

    public String addMember() {
        dao.saveMember(newMember);
        newMember = new Member(); // Reset form
        refreshData();
        return "home";
    }

    public String issueBook() {
        String result = dao.issueBook(selectedBookId, selectedMemberId, dueDate);
        refreshData();
        // Stay on same page if error, else go home
        return result.equals("success") ? "home" : "issue";
    }

    public String returnBookAction(Long transId) {
        dao.returnBook(transId);
        refreshData();
        return "return"; // Stay on return page to see update
    }

    // --- GETTERS & SETTERS (Required for JSF Binding) ---

    public Book getNewBook() { return newBook; }
    public void setNewBook(Book newBook) { this.newBook = newBook; }

    public Member getNewMember() { return newMember; }
    public void setNewMember(Member newMember) { this.newMember = newMember; }

    public Long getSelectedBookId() { return selectedBookId; }
    public void setSelectedBookId(Long selectedBookId) { this.selectedBookId = selectedBookId; }

    public Long getSelectedMemberId() { return selectedMemberId; }
    public void setSelectedMemberId(Long selectedMemberId) { this.selectedMemberId = selectedMemberId; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public List<Book> getBooks() { return books; }
    public List<Member> getMembers() { return members; }
    public List<BorrowTransaction> getActiveTrans() { return activeTrans; }

    // For Reports
    public List<BorrowTransaction> getOverdueList() {
        return dao.getOverdueTransactions();
    }
}