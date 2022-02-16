package dao.Woord;

import model.Moeilijkheidsgraad;
import model.Woord;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class WoordDao implements IWoordDao{
    @Override
    @Transactional
    public void create(Woord u) {
        JPA.em().persist(u);
    }

    @Override
    @Transactional
    public Woord getById(int id) {
        return JPA.em().find(Woord.class, id);
    }

    @Override
    @Transactional
    public Woord getByName(String woord) {
        String queryStr = "FROM Woord where woord = :woord";

        TypedQuery<Woord> query = JPA.em().createQuery(queryStr, Woord.class);
        query.setParameter("woord", woord);

        try{
            return query.getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }

    @Override
    @Transactional
    public List<Woord> getByCategorieId(int categorieId) {
        String queryStr = "SELECT w FROM Woord w where w.categorie.id = :id";

        TypedQuery<Woord> query = JPA.em().createQuery(queryStr, Woord.class);
        query.setParameter("id", categorieId);

        try{
            return query.getResultList();
        }catch(NoResultException e){
            return null;
        }
    }

    @Override
    @Transactional
    public List<Woord> getBySpellingIdWhereMoeilijkheidsgraad(int spellingregelid, Moeilijkheidsgraad moeilijkheidsgraad) {
        String queryStr = "" +
                "SELECT woorden " +
                "FROM SpellingRegel s " +
                "JOIN s.woorden woorden " +
                "WHERE s.id = :id " +
                "AND woorden.moeilijkheidsgraad = :moeilijkheidsgraad";

        TypedQuery<Woord> query = JPA.em().createQuery(queryStr, Woord.class);
        query.setParameter("id", spellingregelid);
        query.setParameter("moeilijkheidsgraad", moeilijkheidsgraad);

        try{
            return query.getResultList();
        }catch(NoResultException e){
            return null;
        }
    }

    @Override
    @Transactional
    public void update(Woord woord) {
        JPA.em().merge(woord);
    }

    @Override
    @Transactional
    public void delete(Woord woord) {
        if(woord == null) return;

        JPA.em().remove(woord);
    }

    @Override
    @Transactional
    public boolean deleteByString(String woord) {
        String queryStr = "DELETE Woord where woord = :woord";

        Query query = JPA.em().createQuery(queryStr);
        query.setParameter("woord", woord);

        return query.executeUpdate() == 1;
    }
}
