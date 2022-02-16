package dao.AnalyzeSpellingRegel;

import model.Gebruiker;
import model.analyze.SpellingOpdracht.AnalyzeSpellingOpdracht;
import model.analyze.SpellingOpdracht.AnalyzeSpellingAntwoord;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

public class AnalyzeSpellingOpdrachtDao implements IAnalyzeSpellingOpdrachtDao {
    @Override
    @Transactional
    public void insert(AnalyzeSpellingOpdracht analyzeSpellingOpdracht) {
        JPA.em().persist(analyzeSpellingOpdracht);
    }

    @Override
    @Transactional
    public void insertWoord(AnalyzeSpellingAntwoord woord) {
        JPA.em().merge(woord);
    }

    @Override
    @Transactional
    public AnalyzeSpellingOpdracht getById(int aId) {
        return JPA.em().find(AnalyzeSpellingOpdracht.class, aId);
    }

    @Override
    @Transactional
    public AnalyzeSpellingOpdracht getByTijd(Gebruiker gebruiker, LocalDateTime beginTijd) {
        String queryStr = "FROM AnalyzeSpellingOpdracht where leerling = :gebruiker AND beginTijd = :beginTijd";

        TypedQuery<AnalyzeSpellingOpdracht> query = JPA.em().createQuery(queryStr, AnalyzeSpellingOpdracht.class);
        query.setParameter("gebruiker", gebruiker);
        query.setParameter("beginTijd", beginTijd);

        return query.getSingleResult();
    }

    @Override
    @Transactional
    public List<AnalyzeSpellingOpdracht> getByGebruiker(Gebruiker gebruiker) {
        String queryStr = "FROM AnalyzeSpellingOpdracht where leerling = :gebruiker ORDER BY beginTijd";

        TypedQuery<AnalyzeSpellingOpdracht> query = JPA.em().createQuery(queryStr, AnalyzeSpellingOpdracht.class);
        query.setParameter("gebruiker", gebruiker);

        try{
            return query.getResultList();
        }catch(NoResultException e){
            return null;
        }
    }

    @Override
    @Transactional
    public List<AnalyzeSpellingAntwoord> getWoordenByUser(Gebruiker gebruiker) {

        String queryStr =
                "SELECT woorden FROM AnalyzeSpellingOpdracht ar " +
                        "JOIN ar.woorden woorden " +
                        "WHERE ar.leerling = :gebruiker ";

        TypedQuery<AnalyzeSpellingAntwoord> query = JPA.em().createQuery(queryStr, AnalyzeSpellingAntwoord.class);
        query.setParameter("gebruiker", gebruiker);

        try{
            return query.getResultList();
        }catch(NoResultException e){
            return null;
        }
    }

    @Override
    @Transactional
    public boolean updateEindTijd(AnalyzeSpellingOpdracht opdracht) {

        String queryStr =
                "UPDATE AnalyzeSpellingOpdracht " +
                        "SET eindTijd = :eindTijd " +
                        "WHERE id = :id";

        Query query = JPA.em().createQuery(queryStr);
        query.setParameter("eindTijd", opdracht.getEindTijd());
        query.setParameter("id", opdracht.getId());

        try{
            return query.executeUpdate() == 1;
        } catch(Exception e){
            return false;
        }
    }



    @Override
    @Transactional
    public void delete(AnalyzeSpellingOpdracht opdracht) {
        JPA.em().remove(opdracht);
    }

    @Override
    public void deleteByDateAndLeerling(AnalyzeSpellingOpdracht opdracht) {
        //Remove the regel
        LocalDateTime datum = opdracht.getBeginTijd().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime datumPlus1 = datum.withHour(23).withMinute(59).withSecond(59);

        //Use time because where deleting per date
        String queryStr =
                "FROM AnalyzeSpellingOpdracht " +
                        "WHERE beginTijd between :datum AND :datumPlus1 " +
                        "AND leerling = :leering";


        TypedQuery<AnalyzeSpellingOpdracht> query = JPA.em().createQuery(queryStr, AnalyzeSpellingOpdracht.class);
        query.setParameter("datum", datum);
        query.setParameter("datumPlus1", datumPlus1);
        query.setParameter("leering", opdracht.getLeerling());

        try{
            List<AnalyzeSpellingOpdracht> regels =  query.getResultList();

            //TODO Bad practice to do SQL in a loop.
            for (AnalyzeSpellingOpdracht analyzeSpellingOpdracht : regels ) {
                JPA.em().remove(analyzeSpellingOpdracht);
            }

        }catch(Exception e){
            e.printStackTrace();
            return;
        }

    }
}
