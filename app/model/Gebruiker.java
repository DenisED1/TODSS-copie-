package model;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "Gebruiker")
@Table(name = "Gebruiker")
public class Gebruiker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String gebruikersnaam;

    @Column(nullable = false)
    private String wachtwoord;

    @Column()
    private String voornaam;

    @Column()
    private String achternaam;

    @Column()
    private Date geboortedatum;

    @Column()
    private String klas;

    @Column()
    @Enumerated(EnumType.STRING)
    private Functie functie;

    @Column(unique = true, nullable = false, length = 30)
    @NaturalId
    private String uuid;

    @OneToOne()
    private Gebruiker begeleider;

    protected Gebruiker(){}

    public Gebruiker(String gebruikersnaam, String wachtwoord, String voornaam, String achternaam, Date geboortedatum, String klas, Functie functie, String uuid){
        this.gebruikersnaam = gebruikersnaam;
        this.wachtwoord = wachtwoord;
        this.voornaam = voornaam;
        this.achternaam = achternaam;
        this.geboortedatum = geboortedatum;
        this.klas = klas;
        this.functie = functie;
        this.uuid = uuid;
    }

    //region getters/setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGebruikersnaam() {
        return gebruikersnaam;
    }

    public void setGebruikersnaam(String gebruikersnaam) {
        this.gebruikersnaam = gebruikersnaam;
    }

    public String getWachtwoord() {
        return wachtwoord;
    }

    public void setWachtwoord(String wachtwoord) {
        this.wachtwoord = wachtwoord;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public void setVoornaam(String voornaam) {
        this.voornaam = voornaam;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    public Date getGeboortedatum() {
        return geboortedatum;
    }

    public void setGeboortedatum(Date geboortedatum) {
        this.geboortedatum = geboortedatum;
    }

    public String getKlas() {
        return klas;
    }

    public void setKlas(String klas) {
        this.klas = klas;
    }

    public Functie getFunctie() {
        return functie;
    }

    public void setFunctie(Functie functie) {
        this.functie = functie;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Gebruiker getBegeleider() {
        return begeleider;
    }

    public void setBegeleider(Gebruiker begeleider) {
        this.begeleider = begeleider;
    }
    //endregion

    public enum Functie {
        leerling,
        leraar,
        administrator
    }
}
