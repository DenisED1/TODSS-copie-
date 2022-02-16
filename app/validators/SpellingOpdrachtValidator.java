package validators;

import dao.SpellingRegel.ISpellingRegelDao;
import dao.SpellingRegel.SpellingRegelDao;
import model.SpellingRegel;
import play.db.jpa.Transactional;

public class SpellingOpdrachtValidator extends Validator{
    @Transactional
    public static void nieuwSpellingRegel(SpellingRegel spellingRegel) throws Exception{
        ISpellingRegelDao spellingRegelDao = new SpellingRegelDao();
        emptyJab();

        if(spellingRegelDao.getByName(spellingRegel.getNaam()) != null){
            jab("Er bestaat al een spellingregel met de naam " + spellingRegel.getNaam());

            ThrowExceptionIfErrors(jab);
        }
    }
}
