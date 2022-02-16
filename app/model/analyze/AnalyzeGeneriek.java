package model.analyze;

import model.Gebruiker;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@MappedSuperclass
public abstract class AnalyzeGeneriek {
    @TableGenerator(table = "myseq", name = "test_assignment_gen", pkColumnName = "seq_name", valueColumnName = "seq_val",
            pkColumnValue = "Test_assignment", allocationSize = 1, initialValue = 100)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "test_assignment_gen")
    private int id;

    @Column()
    protected LocalDateTime beginTijd;

    @Column()
    protected LocalDateTime eindTijd;

    @OneToOne()
    protected Gebruiker leerling;

    protected AnalyzeGeneriek(){}

    protected AnalyzeGeneriek(LocalDateTime beginTijd, LocalDateTime eindTijd, Gebruiker leerling){
        this.beginTijd = beginTijd;
        this.eindTijd = eindTijd;
        this.leerling = leerling;
    }

    //region getters/setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getBeginTijd() {
        return beginTijd;
    }

    public void setBeginTijd() {
        this.beginTijd = getCurrentTime();
    }

    public void setBeginTijd(LocalDateTime beginTijd) {
        this.beginTijd = beginTijd;
    }

    public LocalDateTime getEindTijd() {
        return eindTijd;
    }

    public void setEindTijd() {
        this.eindTijd = getCurrentTime();
    }

    public void setEindTijd(LocalDateTime eindTijd) {
        this.eindTijd = eindTijd;
    }

    public Gebruiker getLeerling() {
        return leerling;
    }

    public void setLeerling(Gebruiker leerling) {
        this.leerling = leerling;
    }

    public String getUserId(){
        return leerling.getUuid();
    }
    //endregion

    public Date getDatum(){
        if(getBeginTijd() != null) {
            return Date.valueOf(getBeginTijd().toLocalDate());
        }

        return null;

    }

    public Time getBeginTijdLocalTime() {
        if(getBeginTijd() != null) {
            return Time.valueOf(getBeginTijd().toLocalTime());
        }

        return null;
    }

    public Time getEindTijdLocalTime() {
        if(getEindTijd() != null) {
            return Time.valueOf(getEindTijd().toLocalTime());
        }

        return null;
    }

    private LocalDateTime getCurrentTime(){
        return LocalDateTime.now();
    }
}
