package model;

import javax.persistence.*;

@Entity
@Table(name = "Woord")
public class Woord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 50, nullable = false)
    private String woord;

    @Column(nullable = false)
    private String uitspraak;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Moeilijkheidsgraad moeilijkheidsgraad;

    @ManyToOne()
    private Categorie categorie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spellingRegel_id")
    private SpellingRegel spellingRegel;

    public Woord(){

    }

    public Woord(String woord, String uitspraak, Moeilijkheidsgraad moeilijkheidsgraad, Categorie categorie) {
        this.woord = woord;
        this.uitspraak = uitspraak;
        this.moeilijkheidsgraad = moeilijkheidsgraad;
        this.categorie = categorie;
    }

    public Woord(String woord, Categorie categorie) {
        this.woord = woord;
        this.categorie = categorie;
    }

    public Woord(String woord, Moeilijkheidsgraad moeilijkheidsgraad, Categorie categorie) {
        this.woord = woord;
        this.moeilijkheidsgraad = moeilijkheidsgraad;
        this.categorie = categorie;
    }

    public Woord(int woordID, String woord, String uitspraak, Moeilijkheidsgraad moeilijkheidsgraad, Categorie categorie) {
        this.id = woordID;
        this.woord = woord;
        this.uitspraak = uitspraak;
        this.moeilijkheidsgraad = moeilijkheidsgraad;
        this.categorie = categorie;
    }

    //region getters/setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWoord() {
        return woord;
    }

    public void setWoord(String woord) {
        this.woord = woord;
    }

    public String getUitspraak() {
        return uitspraak;
    }

    public void setUitspraak(String uitspraak) {
        this.uitspraak = uitspraak;
    }

    public Moeilijkheidsgraad getMoeilijkheidsgraad() {
        return moeilijkheidsgraad;
    }

    public void setMoeilijkheidsgraad(Moeilijkheidsgraad moeilijkheidsgraad) {
        this.moeilijkheidsgraad = moeilijkheidsgraad;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public SpellingRegel getSpellingRegel() {
        return spellingRegel;
    }

    public void setSpellingRegel(SpellingRegel spellingRegel) {
        this.spellingRegel = spellingRegel;
    }
    //endregion

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Woord woord1 = (Woord) o;

        if (!woord.equals(woord1.woord)) return false;
        if (uitspraak != null ? !uitspraak.equals(woord1.uitspraak) : woord1.uitspraak != null) return false;
        if (moeilijkheidsgraad != woord1.moeilijkheidsgraad) return false;
        return categorie.equals(woord1.categorie);
    }

    @Override
    public int hashCode() {
        int result = woord.hashCode();
        result = 31 * result + (uitspraak != null ? uitspraak.hashCode() : 0);
        result = 31 * result + (moeilijkheidsgraad != null ? moeilijkheidsgraad.hashCode() : 0);
        result = 31 * result + categorie.hashCode();
        return result;
    }
}
