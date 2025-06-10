package top.cinema.app.entities;


import jakarta.persistence.*;
import org.hibernate.annotations.Formula;
import top.cinema.app.model.CinemaChain;

import java.util.List;

@Entity
@Table(name = "cinemas")
public class Cinema {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String name;

    private String location;

    private CinemaChain cinemaChain;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;

    @OneToMany(mappedBy = "cinema", fetch = FetchType.LAZY)
    private List<Showing> showings;

    @Formula("(SELECT COUNT(*) FROM showings WHERE cinema_id = id)")
    private Integer showingCounts;


    public Cinema() {
    }

}
