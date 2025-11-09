package com.library.model;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity @Table(name = "books")
public class Book implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private String title;
    @Column(nullable = false) private String author;
    @Column(unique = true, nullable = false) private String isbn;
    private int totalCopies;
    private int availableCopies;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public int getTotalCopies() { return totalCopies; }
    public void setTotalCopies(int totalCopies) { this.totalCopies = totalCopies; }
    public int getAvailableCopies() { return availableCopies; }
    public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies; }
    @Override public String toString() { return title + " (ISBN: " + isbn + ")"; }
    @Override public boolean equals(Object o) { return (o instanceof Book) && (id != null) && id.equals(((Book) o).id); }
    @Override public int hashCode() { return (id != null) ? id.hashCode() : 0; }
}
