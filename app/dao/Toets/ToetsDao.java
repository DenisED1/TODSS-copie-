package dao.Toets;

import model.Gebruiker;
import model.analyze.Toets.Toets;
import model.analyze.Toets.ToetsAntwoord;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

public class ToetsDao implements IToetsDao{
    @Override
    @Transactional
    public void insert(Toets toets) {
        JPA.em().persist(toets);
    }

    @Override
    @Transactional
    public void insertWoord(ToetsAntwoord woord) {
        JPA.em().merge(woord);
    }

    @Override
    @Transactional
    public Toets getById(int aId) {
        return JPA.em().find(Toets.class, aId);
    }

    @Override
    @Transactional
    public Toets getByTijd(Gebruiker gebruiker, LocalDateTime beginTijd) {
        String queryStr = "FROM Toets where leerling = :gebruiker AND beginTijd = :beginTijd";

        TypedQuery<Toets> query = JPA.em().createQuery(queryStr, Toets.class);
        query.setParameter("gebruiker", gebruiker);
        query.setParameter("beginTijd", beginTijd);

        return query.getSingleResult();
    }

    @Override
    @Transactional
    public List<Toets> getByGebruiker(Gebruiker gebruiker) {
        String queryStr = "FROM Toets where leerling = :gebruiker ORDER BY beginTijd";

        TypedQuery<Toets> query = JPA.em().createQuery(queryStr, Toets.class);
        query.setParameter("gebruiker", gebruiker);

        try{
            return query.getResultList();
        }catch(NoResultException e){
            return null;
        }
    }

    @Override
    @Transactional
    public List<ToetsAntwoord> getWoordenByUser(Gebruiker gebruiker) {

        String queryStr =
                "SELECT woorden FROM Toets to " +
                        "JOIN to.woorden woorden " +
                        "WHERE to.leerling = :gebruiker ";

        TypedQuery<ToetsAntwoord> query = JPA.em().createQuery(queryStr, ToetsAntwoord.class);
        query.setParameter("gebruiker", gebruiker);

        try{
            return query.getResultList();
        }catch(NoResultException e){
            return null;
        }
    }

    @Override
    @Transactional
    public boolean updateEindTijd(Toets toets) {
        System.out.println("Kom ik hier?????");
        System.out.println(toets.getEindTijd());
        System.out.println(toets.getId());


        String queryStr = "UPDATE Toets " +
                        "SET eindTijd = :eindTijd " +
                        "WHERE id = :id";

        Query query = JPA.em().createQuery(queryStr);
        query.setParameter("eindTijd", toets.getEindTijd());
        query.setParameter("id", toets.getId());

        try{
            return query.executeUpdate() == 1;
        } catch(Exception e){
            return false;
        }
    }



    @Override
    @Transactional
    public void delete(Toets toets) {
        JPA.em().remove(toets);
    }

    @Override
    public void deleteByDateAndLeerling(Toets toets) {
        //Remove the regel
        LocalDateTime datum = toets.getBeginTijd().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime datumPlus1 = datum.withHour(23).withMinute(59).withSecond(59);

        //Use time because where deleting per date
        String queryStr = "FROM Toets " +
                "WHERE beginTijd between :datum AND :datumPlus1 " +
                "AND leerling = :leering";


        TypedQuery<Toets> query = JPA.em().createQuery(queryStr, Toets.class);
        query.setParameter("datum", datum);
        query.setParameter("datumPlus1", datumPlus1);
        query.setParameter("leering", toets.getLeerling());

        try {
            List<Toets> regels = query.getResultList();

            //TODO Bad practice to do SQL in a loop.
            for (Toets tt : regels) {
                JPA.em().remove(tt);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
}
