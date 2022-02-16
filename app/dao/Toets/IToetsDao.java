package dao.Toets;

import model.Gebruiker;
import model.analyze.Toets.Toets;
import model.analyze.Toets.ToetsAntwoord;
import play.db.jpa.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface IToetsDao {
    void insert(Toets toets);

    Toets getById(int aId);

    Toets getByTijd(Gebruiker gebruiker, LocalDateTime beginTijd);

    List<Toets> getByGebruiker(Gebruiker gebruiker);

    List<ToetsAntwoord> getWoordenByUser(Gebruiker gebruiker);

    void insertWoord(ToetsAntwoord woord);

    @Transactional
    boolean updateEindTijd(Toets toets);

    void delete(Toets regel);

    void deleteByDateAndLeerling(Toets regel);
}
