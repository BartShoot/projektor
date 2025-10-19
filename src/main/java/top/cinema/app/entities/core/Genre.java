package top.cinema.app.entities.core;

import jakarta.persistence.*;

@Entity
@Table(name = "genres")
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private MovieGenre name;

    public Genre() {}

    public Genre(MovieGenre name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MovieGenre getName() {
        return name;
    }

    public void setName(MovieGenre name) {
        this.name = name;
    }
}
