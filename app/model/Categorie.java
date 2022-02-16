package model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Categorie")
public class Categorie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String naam;

    @OneToMany(
            mappedBy = "categorie",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<Woord> woorden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spellingRegel_id")
    SpellingRegel spellingRegel;

    public Categorie() { }

    public Categorie(String naam) {
        this.naam = naam;
    }

    public Categorie(int id, String naam) {
        this.id = id;
        this.naam = naam;
    }

    //region getters/setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public SpellingRegel getSpellingRegel() {
        return spellingRegel;
    }

    public void setSpellingRegel(SpellingRegel spellingRegel) {
        this.spellingRegel = spellingRegel;
    }

    public List<Woord> getWoorden() {
        return woorden;
    }

    public void addWoord(Woord woord) {
        woord.setCategorie(this);

        this.woorden.add(woord);
    }

    //endregion

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Categorie categorie = (Categorie) o;

        return naam.equals(categorie.naam);
    }

    @Override
    public int hashCode() {
        return naam.hashCode();
    }
}
