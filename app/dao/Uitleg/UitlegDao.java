package dao.Uitleg;

import model.Uitleg;
import model.UitlegRegel;
import org.hibernate.criterion.Restrictions;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Predicate;
import java.util.List;

public class UitlegDao implements IUitlegDao{

    @Override
    @Transactional
    public void insert(Uitleg uitleg) {
        JPA.em().persist(uitleg);
    }

    @Override
    @Transactional
    public List<Uitleg> getBySpellingRegelNaam(String naam) {
        String queryStr = "" +
                "FROM Uitleg u " +
                "WHERE u.spellingRegel.naam = :naam ";

        TypedQuery<Uitleg> query = JPA.em().createQuery(queryStr, Uitleg.class);
        query.setParameter("naam", naam);

        try{
            return query.getResultList();
        }catch(NoResultException e){
            return null;
        }
    }

    @Override
    @Transactional
    public Uitleg getByUuId(String uuId) {
        String queryStr = "" +
                "FROM Uitleg u " +
                "WHERE u.uuid = :uuid ";

        TypedQuery<Uitleg> query = JPA.em().createQuery(queryStr, Uitleg.class);
        query.setParameter("uuid", uuId);

        try{
            return query.getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }

    @Override
    @Transactional
    public List<UitlegRegel> getUitlegRegels(String uuId) {
        String queryStr = "" +
                "SELECT regels FROM Uitleg u " +
                "JOIN u.uitlegRegels regels " +
                "WHERE u.uuid = :uuid ";

        TypedQuery<UitlegRegel> query = JPA.em().createQuery(queryStr, UitlegRegel.class);
        query.setParameter("uuid", uuId);

        try{
            return query.getResultList();
        }catch(NoResultException e){
            return null;
        }
    }

    @Override
    @Transactional
    public void removeIncludesRegels(Uitleg uitleg) {
        String queryRegelsStr = "DELETE UitlegRegel WHERE id = :id";
        Query queryRegels = JPA.em().createQuery(queryRegelsStr);
        for (UitlegRegel regel: uitleg.getUitlegRegels()) {
            queryRegels.setParameter("id", regel.getId());
            queryRegels.executeUpdate();
        }

        String queryStr = "DELETE Uitleg WHERE uuid = :uuid";
        Query query = JPA.em().createQuery(queryStr);

        query.setParameter("uuid", uitleg.getUuid());
        query.executeUpdate();
    }
}
