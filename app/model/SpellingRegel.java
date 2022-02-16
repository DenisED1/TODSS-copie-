package model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SpellingRegel")
public class SpellingRegel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column()
    private String naam;

    @Column()
    private String korteUitleg;

    @Column()
    private String opdrachtUitleg;

    @OneToMany(
            mappedBy = "spellingRegel",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<Woord> woorden;

    @OneToMany(
            mappedBy = "spellingRegel",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<Categorie> categorieen;


    @OneToMany(
            mappedBy = "spellingRegel",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<Uitleg> uitleg;

    protected SpellingRegel(){}

    public SpellingRegel(String naam) {
        this(naam, "", "");
    }

    public SpellingRegel(String naam, String korteUitleg, String opdrachtUitleg){
        this.naam = naam;
        this.korteUitleg = korteUitleg;
        this.opdrachtUitleg = opdrachtUitleg;
        woorden = new ArrayList<>();
        categorieen = new ArrayList<>();
        uitleg = new ArrayList<>();
    }

    public SpellingRegel(int spellingregelID, String naam, String korteUitleg, String opdrachtUitleg) {
        this(naam, korteUitleg, opdrachtUitleg);
        this.id = spellingregelID;
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

    public String getKorteUitleg() {
        return korteUitleg;
    }

    public void setKorteUitleg(String korteUitleg) {
        this.korteUitleg = korteUitleg;
    }

    public String getOpdrachtUitleg() {
        return opdrachtUitleg;
    }

    public void setOpdrachtUitleg(String opdrachtUitleg) {
        this.opdrachtUitleg = opdrachtUitleg;
    }

    public List<Woord> getWoorden() {
        return woorden;
    }

    public void addWoord(Woord woord) {
        woord.setSpellingRegel(this);

        this.woorden.add(woord);
    }

    public List<Categorie> getCategorieen() {
        return categorieen;
    }

    public void addCategorie(Categorie categorie){
        categorie.setSpellingRegel(this);

        this.categorieen.add(categorie);
    }

    public void removeCategorie(Categorie categorie){
        categorie.setSpellingRegel(null);

        this.categorieen.add(categorie);
    }

    public List<Uitleg> getUitleg() {
        return uitleg;
    }

    public void setUitleg(List<Uitleg> uitleg) {
        this.uitleg = uitleg;
    }


    //endregion

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpellingRegel that = (SpellingRegel) o;

        if (!naam.equals(that.naam)) return false;
        if (woorden != null ? !woorden.equals(that.woorden) : that.woorden != null) return false;
        return uitleg != null ? uitleg.equals(that.uitleg) : that.uitleg == null;
    }

    @Override
    public int hashCode() {
        int result = naam.hashCode();
        result = 31 * result + (woorden != null ? woorden.hashCode() : 0);
        result = 31 * result + (uitleg != null ? uitleg.hashCode() : 0);
        return result;
    }

}