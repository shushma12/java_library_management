package com.library.model;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity @Table(name = "members")
public class Member implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private String name;
    @Column(unique = true, nullable = false) private String email;
    private String phone;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    @Override public boolean equals(Object o) { return (o instanceof Member) && (id != null) && id.equals(((Member) o).id); }
    @Override public int hashCode() { return (id != null) ? id.hashCode() : 0; }
}
