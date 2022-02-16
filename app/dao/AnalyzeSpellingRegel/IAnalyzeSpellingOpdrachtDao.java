package dao.AnalyzeSpellingRegel;

import model.Gebruiker;
import model.analyze.SpellingOpdracht.AnalyzeSpellingOpdracht;
import model.analyze.SpellingOpdracht.AnalyzeSpellingAntwoord;
import play.db.jpa.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface IAnalyzeSpellingOpdrachtDao {
    void insert(AnalyzeSpellingOpdracht analyzeSpellingOpdracht);

    AnalyzeSpellingOpdracht getById(int aId);

    AnalyzeSpellingOpdracht getByTijd(Gebruiker gebruiker, LocalDateTime beginTijd);

    List<AnalyzeSpellingOpdracht> getByGebruiker(Gebruiker gebruiker);

    List<AnalyzeSpellingAntwoord> getWoordenByUser(Gebruiker gebruiker);

    void insertWoord(AnalyzeSpellingAntwoord woord);

    @Transactional
    boolean updateEindTijd(AnalyzeSpellingOpdracht opdracht);

    void delete(AnalyzeSpellingOpdracht regel);

    void deleteByDateAndLeerling(AnalyzeSpellingOpdracht regel);
}
