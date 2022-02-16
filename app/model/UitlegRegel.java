package model;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Table(name = "UitlegRegel")
public class UitlegRegel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NaturalId
    @Column(length = 30, nullable = false, unique = true)
    private String uuid;

    @Column()
    private String text;

    @Column()
    private String uitspraak;

    @Column()
    private String icon;

    @Column()
    private String iconText;

    @Column()
    private boolean overschrijven;

    @Column()
    private boolean intikken;

    @Column()
    private boolean controleren;

    @Column()
    private int wachttijd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uitleg_id")
    private Uitleg uitleg;

    protected UitlegRegel() { }

    public UitlegRegel(String uuid) {
        this(uuid, "");
    }

    public UitlegRegel(String uuid, String uitspraak) {
        this(uuid, "", uitspraak);
    }

    public UitlegRegel(String uuid, String text, String uitspraak) {
        this(uuid, text, uitspraak, "", "");
    }

    public UitlegRegel(String uuid, String text, String uitspraak, String icon, String iconText) {
        this(uuid, text, uitspraak, icon, iconText, false, 1000);
    }

    public UitlegRegel(String uuid, String text, String uitspraak, String icon, String iconText, boolean overschrijven, int wachttijd){
        this(uuid, text, uitspraak, icon, iconText, overschrijven, wachttijd, false, false);
    }

    public UitlegRegel(String uuid, String text, String uitspraak, String icon, String iconText, boolean overschrijven, int wachttijd, boolean intikken, boolean controleren) {
        this.uuid = uuid;
        this.text = text;
        this.uitspraak = uitspraak;
        this.icon = icon;
        this.iconText = iconText;
        this.overschrijven = overschrijven;
        this.wachttijd = wachttijd;
        this.intikken = intikken;
        this.controleren = controleren;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUitspraak() {
        return uitspraak;
    }

    public void setUitspraak(String uitspraak) {
        this.uitspraak = uitspraak;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIconText() {
        return iconText;
    }

    public void setIconText(String iconText) {
        this.iconText = iconText;
    }

    public boolean isOverschrijven() {
        return overschrijven;
    }

    public void setOverschrijven(boolean overschrijven) {
        this.overschrijven = overschrijven;
    }

    public boolean isIntikken() {
        return intikken;
    }

    public void setIntikken(boolean intikken) {
        this.intikken = intikken;
    }

    public boolean isControleren() {
        return controleren;
    }

    public void setControleren(boolean controleren) {
        this.controleren = controleren;
    }

    public int getWachttijd() {
        return wachttijd;
    }

    public void setWachttijd(int wachttijd) {
        this.wachttijd = wachttijd;
    }

    public Uitleg getUitleg() {
        return uitleg;
    }

    public void setUitleg(Uitleg uitleg) {
        this.uitleg = uitleg;
    }
}   //endregion
