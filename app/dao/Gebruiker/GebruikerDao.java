package dao.Gebruiker;

import model.Gebruiker;
import org.hibernate.Session;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

public class GebruikerDao implements IGebruikerDao {
    private final Class<Gebruiker> gebruikerClass = Gebruiker.class;

    @Override
    @Transactional
    public void insert(Gebruiker gebruiker) {
        JPA.em().persist(gebruiker);
    }

    @Override
    @Transactional
    public void update(Gebruiker gebruiker) {
        JPA.em().merge(gebruiker);
    }

    @Override
    @Transactional
    public Gebruiker getByID(int id) {
        return JPA.em().find(gebruikerClass, id);
    }

    @Override
    @Transactional
    public Gebruiker getByUUID(String uuId) {
        return JPA.em().unwrap(Session.class)
                .bySimpleNaturalId(gebruikerClass)
                .load(uuId);
    }

    @Override
    @Transactional
    public Gebruiker getByUsername(String username) {
        String queryStr = "SELECT u FROM Gebruiker u where u.gebruikersnaam = :gebruikersnaam";

        TypedQuery<Gebruiker> query = JPA.em().createQuery(queryStr, gebruikerClass);
        query.setParameter("gebruikersnaam", username);

        try{
            return query.getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }

    @Override
    @Transactional
    public List<Gebruiker> getAlleUsersVanBegeleider(Gebruiker gebruiker) {
        String queryStr = "FROM Gebruiker G WHERE begeleider = :begeleider";

        TypedQuery<Gebruiker> query = JPA.em().createQuery(queryStr, gebruikerClass);
        query.setParameter("begeleider", gebruiker);

        try{
            return query.getResultList();
        }catch(NoResultException e){
            return null;
        }

    }

    @Override
    @Transactional
    public List<Gebruiker> getAlleUsersVanFunctie(Gebruiker.Functie functie) {
        String queryStr = "FROM Gebruiker G WHERE functie = :functie";

        TypedQuery<Gebruiker> query = JPA.em().createQuery(queryStr, gebruikerClass);
        query.setParameter("functie", functie);

        try{
            return query.getResultList();
        }catch(NoResultException e){
            return null;
        }
    }

    @Override
    @Transactional
    public List<Gebruiker> getAlleUsersVanFuncties(List<Gebruiker.Functie> functies) {
        String queryStr = "FROM Gebruiker G WHERE functie in (:functies)";

        TypedQuery<Gebruiker> query = JPA.em().createQuery(queryStr, gebruikerClass);
        query.setParameter("functies", functies);

        try{
            return query.getResultList();
        }catch(NoResultException e){
            return null;
        }
    }

    @Override
    @Transactional
    public void remove(Gebruiker gebruiker) {
        if(gebruiker == null) return;

        JPA.em().remove(gebruiker);
    }

    @Override
    public void remove(String uuId) {
        String queryStr = "DELETE FROM Gebruiker G WHERE uuid = :uuid";

        TypedQuery<Gebruiker> query = JPA.em().createQuery(queryStr, gebruikerClass);
        query.setParameter("uuid", uuId);

        query.executeUpdate();
    }

}
