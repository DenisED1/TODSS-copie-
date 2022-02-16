package model.analyze.Toets;

import model.Gebruiker;
import model.SpellingRegel;
import model.analyze.AnalyzeGeneriek;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Toets")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Toets extends AnalyzeGeneriek {
    @OneToMany(
            mappedBy = "regel",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ToetsAntwoord> woorden = new ArrayList<>();

    @OneToOne(fetch = FetchType.EAGER)
    private SpellingRegel spellingRegel;

    public Toets(){
        super();
    }

    public Toets(LocalDateTime beginTijd, LocalDateTime eindTijd, Gebruiker leerling) {
        super(beginTijd, eindTijd, leerling);
    }

    //region getters/setters
    public void voegAntwoordToe(ToetsAntwoord woord){
        woord.setRegel(this);

        woorden.add(woord);
    }

    public List<ToetsAntwoord> getWoorden() {
        return woorden;
    }

    public SpellingRegel getSpellingRegel() {
        return spellingRegel;
    }

    public String getSpellingRegelNaam() { return spellingRegel.getNaam(); }

    public void setSpellingRegel(SpellingRegel spellingRegel) {
        this.spellingRegel = spellingRegel;
    }

    //endregion
}
