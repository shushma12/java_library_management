package com.library.dao;
import com.library.model.*;
import com.library.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.time.LocalDate;
import java.util.List;

public class LibraryDAO {
    public void saveBook(Book book) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = s.beginTransaction();
            if (book.getId() == null) book.setAvailableCopies(book.getTotalCopies());
            s.merge(book);
            tx.commit();
        }
    }
    public void saveMember(Member member) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = s.beginTransaction();
            s.merge(member);
            tx.commit();
        }
    }
    public String issueBook(Long bId, Long mId, LocalDate dDate) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            Book b = s.get(Book.class, bId); Member m = s.get(Member.class, mId);
            if (b != null && b.getAvailableCopies() > 0) {
                b.setAvailableCopies(b.getAvailableCopies() - 1);
                BorrowTransaction t = new BorrowTransaction();
                t.setBook(b); t.setMember(m); t.setIssueDate(LocalDate.now()); t.setDueDate(dDate); t.setStatus("ISSUED");
                s.merge(b); s.persist(t);
                tx.commit(); return "success";
            }
            return "error";
        } catch (Exception e) { if (tx != null) tx.rollback(); e.printStackTrace(); return "error"; }
    }
    public void returnBook(Long tId) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            BorrowTransaction t = s.get(BorrowTransaction.class, tId);
            if (t != null && "ISSUED".equals(t.getStatus())) {
                t.setStatus("RETURNED"); t.setReturnDate(LocalDate.now());
                Book b = t.getBook(); b.setAvailableCopies(b.getAvailableCopies() + 1);
                s.merge(t); s.merge(b);
            }
            tx.commit();
        } catch (Exception e) { if (tx != null) tx.rollback(); e.printStackTrace(); }
    }
    public List<Book> getAllBooks() { try (Session s = HibernateUtil.getSessionFactory().openSession()) { return s.createQuery("FROM Book", Book.class).list(); } }
    public List<Member> getAllMembers() { try (Session s = HibernateUtil.getSessionFactory().openSession()) { return s.createQuery("FROM Member", Member.class).list(); } }
    public List<BorrowTransaction> getActiveTransactions() { try (Session s = HibernateUtil.getSessionFactory().openSession()) { return s.createQuery("FROM BorrowTransaction WHERE status='ISSUED'", BorrowTransaction.class).list(); } }
    public List<BorrowTransaction> getOverdueTransactions() { try (Session s = HibernateUtil.getSessionFactory().openSession()) { return s.createQuery("FROM BorrowTransaction WHERE status='ISSUED' AND dueDate < CURRENT_DATE", BorrowTransaction.class).list(); } }
}
