package validators;

import dao.SpellingRegel.ISpellingRegelDao;
import dao.SpellingRegel.SpellingRegelDao;
import dao.Woord.IWoordDao;
import dao.Woord.WoordDao;
import model.Categorie;
import model.SpellingRegel;
import model.Woord;
import play.db.jpa.Transactional;

public class WoordValidator extends Validator {
    @Transactional
    public static void create(Categorie categorie, SpellingRegel spellingRegel, Woord woord) throws Exception {
        ISpellingRegelDao spellingRegelDao = new SpellingRegelDao();
        emptyJab();

        if(spellingRegel.getId() < 0) {
            jab("SpellingRegel " + spellingRegel.getNaam() + " bestaat niet.");
        }

        if (!spellingRegel.getCategorieen().contains(categorie)) {
            jab("De gekozen categorie " + categorie.getNaam() + " bestaat niet.");
        }

        if(spellingRegelDao.getWoord(spellingRegel.getId(), woord) != null){
            jab("Het woord " + woord.getWoord() + " bestaat al in deze spellingregel.");
        }

        ThrowExceptionIfErrors(jab);
    }
}
