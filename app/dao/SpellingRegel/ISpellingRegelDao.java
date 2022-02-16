package dao.SpellingRegel;

import model.Categorie;
import model.Moeilijkheidsgraad;
import model.SpellingRegel;
import model.Woord;

import java.util.List;

public interface ISpellingRegelDao {
    void insert(SpellingRegel spellingRegel);

    SpellingRegel getByName(String naam);

    SpellingRegel getByIdEmpty(int spellingregelid);

    List<SpellingRegel> getAllSpellingRegelsEmpty();

    List<Categorie> getCategorieenBySpellingregelId(int id);

    Woord getWoord(int id, Woord woord);

    void remove(SpellingRegel spellingRegel);

    boolean updateKorteUitleg(SpellingRegel spellingRegel);

    boolean updateOpdrachtUitleg(SpellingRegel spellingRegel);

    SpellingRegel update(SpellingRegel spellingRegel);

    List<Woord> getWoordenBySpellingRegel(SpellingRegel spellingRegel);

    List<Woord> getWoordenBySpellingRegel(SpellingRegel spellingRegel, Moeilijkheidsgraad moeilijkheidsgraad);
}
