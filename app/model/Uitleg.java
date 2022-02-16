package model;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Uitleg")
public class Uitleg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NaturalId
    @Column(unique = true, nullable = false, length = 30)
    private String uuid;

    @Column(nullable = false)
    private String title;

    @Column()
    private int volgorde;

    @OneToMany(
        mappedBy = "uitleg",
        cascade = CascadeType.ALL
    )
    private List<UitlegRegel> uitlegRegels = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spellingRegel_id")
    SpellingRegel spellingRegel;

    public Uitleg(){}

    public Uitleg(String uuid, String title) {
        this.uuid = uuid;
        this.title = title;
    }

    public Uitleg(String title, int volgorde, String uuid) {
        this.title = title;
        this.volgorde = volgorde;
        this.uuid = uuid;
    }

    //region getters/setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getVolgorde() {
        return volgorde;
    }

    public void setVolgorde(int volgorde) {
        this.volgorde = volgorde;
    }

    public List<UitlegRegel> getUitlegRegels() {
        return uitlegRegels;
    }

    public void setUitlegRegels(List<UitlegRegel> uitlegRegels) {
        this.uitlegRegels = uitlegRegels;

        for(UitlegRegel uitlegRegel: uitlegRegels){
            uitlegRegel.setUitleg(this);
        }
    }

    public void addUitlegRegel(UitlegRegel uitlegRegel) {
        uitlegRegel.setUitleg(this);

        uitlegRegels.add(uitlegRegel);
    }

    public void removeUitlegRegel(UitlegRegel uitlegRegel) {
        uitlegRegel.setUitleg(null);

        uitlegRegels.remove(uitlegRegel);
    }

    public SpellingRegel getSpellingRegel() {
        return spellingRegel;
    }

    public void setSpellingRegel(SpellingRegel spellingRegel) {
        this.spellingRegel = spellingRegel;
    }

    //endregion
}
