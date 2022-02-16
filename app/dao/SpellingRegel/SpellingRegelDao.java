package dao.SpellingRegel;

import model.Categorie;
import model.Moeilijkheidsgraad;
import model.SpellingRegel;
import model.Woord;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class SpellingRegelDao implements ISpellingRegelDao {
    private final Class<SpellingRegel> spellingRegelClass = SpellingRegel.class;

    @Override
    @Transactional
    public void insert(SpellingRegel spellingRegel) {
        JPA.em().persist(spellingRegel);
    }

    @Override
    @Transactional
    public SpellingRegel getByName(String naam) {
        String queryStr = "FROM SpellingRegel WHERE naam = :naam";

        TypedQuery<SpellingRegel> query = JPA.em().createQuery(queryStr, spellingRegelClass);
        query.setParameter("naam", naam);

        try{
            return query.getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }

    @Override
    public SpellingRegel getByIdEmpty(int spellingregelid) {
        String queryStr = "FROM SpellingRegel WHERE id = :spellingregelid";

        TypedQuery<SpellingRegel> query= JPA.em().createQuery(queryStr, spellingRegelClass);
        query.setParameter("spellingregelid", spellingregelid);

        try{
            return query.getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }

    @Override
    @Transactional
    public List<SpellingRegel> getAllSpellingRegelsEmpty() {
        String queryStr = "FROM SpellingRegel";

        TypedQuery<SpellingRegel> query = JPA.em().createQuery(queryStr, spellingRegelClass);

        try{
            return query.getResultList();
        }catch(NoResultException e){
            return null;
        }
    }

    @Override
    @Transactional
    public List<Categorie> getCategorieenBySpellingregelId(int id) {
        String queryStr = "" +
                "SELECT c FROM SpellingRegel s " +
                "join s.categorieen c " +
                "where s.id = :spellingRegelId";

        TypedQuery<Categorie> query = JPA.em().createQuery(queryStr, Categorie.class);
        query.setParameter("spellingRegelId", id);

        try{
            return query.getResultList();
        }catch(NoResultException e){
            return null;
        }
    }

    @Override
    @Transactional
    public Woord getWoord(int id, Woord woord) {
        String queryStr = "" +
                "SELECT w FROM SpellingRegel s " +
                "JOIN s.woorden w " +
                "WHERE s.id = :spellingRegelId " +
                "  AND w.id = :woord";

        TypedQuery<Woord> query = JPA.em().createQuery(queryStr, Woord.class);
        query.setParameter("spellingRegelId", id);
        query.setParameter("woord", woord.getId());

        try{
            return query.getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }

    @Override
    @Transactional
    public List<Woord> getWoordenBySpellingRegel(SpellingRegel spellingRegel) {
        String queryStr = "" +
                "SELECT w FROM SpellingRegel s " +
                "JOIN s.woorden w " +
                "WHERE s.id = :spellingRegelId ";

        TypedQuery<Woord> query = JPA.em().createQuery(queryStr, Woord.class);
        query.setParameter("spellingRegelId", spellingRegel.getId());

        try{
            return query.getResultList();
        }catch(NoResultException e){
            return null;
        }
    }

    @Override
    public List<Woord> getWoordenBySpellingRegel(SpellingRegel spellingRegel, Moeilijkheidsgraad moeilijkheidsgraad) {
        String queryStr = "" +
                "SELECT w FROM SpellingRegel s " +
                "JOIN s.woorden w " +
                "WHERE s.id = :spellingRegelId " +
                "AND w.moeilijkheidsgraad = :moeilijkheidsgraad";

        TypedQuery<Woord> query = JPA.em().createQuery(queryStr, Woord.class);
        query.setParameter("spellingRegelId", spellingRegel.getId());
        query.setParameter("moeilijkheidsgraad", moeilijkheidsgraad);

        try{
            return query.getResultList();
        }catch(NoResultException e){
            return null;
        }
    }

    @Override
    @Transactional
    public boolean updateKorteUitleg(SpellingRegel spellingRegel) {
        String queryStr = "update SpellingRegel set korteUitleg = :korteUitleg where id = :id";

        Query query = JPA.em().createQuery(queryStr);
        query.setParameter("korteUitleg", spellingRegel.getKorteUitleg());
        query.setParameter("id", spellingRegel.getId());

        return query.executeUpdate() == 1;
    }

    @Override
    @Transactional
    public boolean updateOpdrachtUitleg(SpellingRegel spellingRegel) {
        String queryStr = "update SpellingRegel set opdrachtUitleg = :opdrachtUitleg where id = :id";

        Query query = JPA.em().createQuery(queryStr);
        query.setParameter("opdrachtUitleg", spellingRegel.getOpdrachtUitleg());
        query.setParameter("id", spellingRegel.getId());

        return query.executeUpdate() == 1;
    }

    @Override
    @Transactional
    public void remove(SpellingRegel spellingRegel) {
        if(spellingRegel == null) return;

        JPA.em().remove(spellingRegel);
    }

    @Override
    @Transactional
    public SpellingRegel update(SpellingRegel spellingRegel) {
        return JPA.em().merge(spellingRegel);
    }


}
