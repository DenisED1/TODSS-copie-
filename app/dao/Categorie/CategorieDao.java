package dao.Categorie;

import model.Categorie;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class CategorieDao implements ICategorieDao{
    @Override
    @Transactional
    public void insert(Categorie categorie) {
        JPA.em().persist(categorie);
    }

    @Override
    @Transactional
    public boolean updateNaam(String oudeNaam, String nieuweNaam) {
        String queryStr = "update Categorie set naam = :nieuweNaam where naam = :oudeNaam";

        Query query = JPA.em().createQuery(queryStr);
        query.setParameter("oudeNaam", oudeNaam);
        query.setParameter("nieuweNaam", nieuweNaam);

        return query.executeUpdate() == 1;
    }

    @Override
    @Transactional
    public Categorie getByName(String naam) {
        String queryStr = "FROM Categorie where naam = :naam";

        TypedQuery<Categorie> query = JPA.em().createQuery(queryStr, Categorie.class);
        query.setParameter("naam", naam);

        return query.getSingleResult();
    }

    @Override
    public void remove(Categorie categorie) {
        if(categorie == null) return;

        JPA.em().remove(categorie);
    }
}
