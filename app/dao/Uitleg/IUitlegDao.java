package dao.Uitleg;

import model.Uitleg;
import model.UitlegRegel;
import play.db.jpa.Transactional;

import java.util.List;

public interface IUitlegDao {
    void insert(Uitleg uitleg);

    List<Uitleg> getBySpellingRegelNaam(String naam);

    Uitleg getByUuId(String uuId);

    List<UitlegRegel> getUitlegRegels(String uuId);

    void removeIncludesRegels(Uitleg uitleg);
}
