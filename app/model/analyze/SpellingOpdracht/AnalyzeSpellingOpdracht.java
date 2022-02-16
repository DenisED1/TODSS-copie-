package model.analyze.SpellingOpdracht;

import model.Gebruiker;
import model.SpellingRegel;
import model.analyze.AnalyzeGeneriek;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "AnalyzeSpellingOpdracht")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class AnalyzeSpellingOpdracht extends AnalyzeGeneriek {
    @OneToMany(
            mappedBy = "regel",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<AnalyzeSpellingAntwoord> woorden = new ArrayList<>();

    @OneToOne(fetch = FetchType.EAGER)
    private SpellingRegel spellingRegel;

    public AnalyzeSpellingOpdracht(){
        super();
    }

    public AnalyzeSpellingOpdracht(LocalDateTime beginTijd, LocalDateTime eindTijd, Gebruiker leerling) {
        super(beginTijd, eindTijd, leerling);
    }

    //region getters/setters
    public void voegAntwoordToe(AnalyzeSpellingAntwoord woord){
        woord.setRegel(this);

        woorden.add(woord);
    }

    public List<AnalyzeSpellingAntwoord> getWoorden() {
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
