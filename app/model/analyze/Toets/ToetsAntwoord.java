package model.analyze.Toets;

import javax.persistence.*;

@Entity
@Table(name = "ToetsAntwoord")
public class ToetsAntwoord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column()
    private String vraag; //TODO make it of object woord instead of a string.

    @Column()
    private String antwoordDefinitief;

    @Column()
    private String antwoordVersie1;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST })
    @JoinColumn(name = "regel_id")
    private Toets regel;

    protected ToetsAntwoord(){}

    public ToetsAntwoord(String vraag, String antwoordVersie1) {
        this.vraag = vraag;
        this.antwoordVersie1 = antwoordVersie1;
    }

    public ToetsAntwoord(String vraag, String antwoordDefinitief, String antwoordVersie1) {
        this.vraag = vraag;
        this.antwoordDefinitief = antwoordDefinitief;
        this.antwoordVersie1 = antwoordVersie1;
    }

    public ToetsAntwoord(int id, String vraag, String antwoordDefinitief, String antwoordVersie1) {
        this.id = id;
        this.vraag = vraag;
        this.antwoordDefinitief = antwoordDefinitief;
        this.antwoordVersie1 = antwoordVersie1;
    }

    public ToetsAntwoord(String vraag, String antwoordDefinitief, String antwoordVersie1, Toets regel) {
        this.vraag = vraag;
        this.antwoordDefinitief = antwoordDefinitief;
        this.antwoordVersie1 = antwoordVersie1;
        this.regel = regel;
    }

    //region setters/getters
    public int getId() { return id; }

    public void setVraag(String vraag) {
        this.vraag = vraag;
    }

    public String getVraag() {
        return vraag;
    }

    public void setAntwoordDefinitief(String antwoordDefinitief) {
        this.antwoordDefinitief = antwoordDefinitief;
    }

    public String getAntwoordDefinitief() {
        return antwoordDefinitief;
    }

    public void setAntwoordVersie1(String antwoordVersie1) {
        this.antwoordVersie1 = antwoordVersie1;
    }

    public String getAntwoordVersie1() {
        return antwoordVersie1;
    }

    public Toets getRegel() {
        return regel;
    }

    public void setRegel(Toets regel) {
        this.regel = regel;
    }

    public int getRegelId(){
        return regel.getId();
    }
    //endregion
}
