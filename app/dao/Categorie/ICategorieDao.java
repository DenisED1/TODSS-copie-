package dao.Categorie;

import model.Categorie;
import play.db.jpa.Transactional;

public interface ICategorieDao {
    void insert(Categorie categorie);

    boolean updateNaam(String oudeNaam, String nieuweNaam);

    Categorie getByName(String naam);

    void remove(Categorie categorie);


}
