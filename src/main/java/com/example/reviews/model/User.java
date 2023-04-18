package com.example.reviews.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(name = "users_email_uq", columnNames = "email"))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(min = 1, max = 255)
    private String firstName;

    @Size(min = 1, max = 255)
    private String lastName;

    @Email
    private String email;

    @OneToMany(mappedBy = "author")
    @Setter(AccessLevel.PRIVATE)
    @Builder.Default
    @ToString.Exclude
    private List<Review> reviews = new ArrayList<>();

    public void addReviews(Review review){
        reviews.add(review);
        review.setAuthor(this);
    }

    public void deleteReview(Review review){
        reviews.remove(review);
        review.setAuthor(null);
    }
}
