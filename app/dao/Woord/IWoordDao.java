package dao.Woord;

import model.Moeilijkheidsgraad;
import model.Woord;
import play.db.jpa.Transactional;

import java.util.List;

public interface IWoordDao {
    void create(Woord u);

    Woord getById(int id);

    List<Woord> getByCategorieId(int categorieId);

    Woord getByName(String woord);

    void update(Woord woord);

    void delete(Woord woord);

    List<Woord> getBySpellingIdWhereMoeilijkheidsgraad(int spellingregelid, Moeilijkheidsgraad moeilijkheidsgraad);

    @Transactional
    boolean deleteByString(String woord);
}
