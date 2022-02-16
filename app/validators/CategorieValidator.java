package validators;

import dao.Woord.IWoordDao;
import dao.Woord.WoordDao;
import model.Categorie;
import model.Woord;
import play.db.jpa.Transactional;

import java.util.List;

public class CategorieValidator extends Validator {

    @Transactional
    public static void delete(Categorie categorie) throws Exception {
        IWoordDao woordDao = new WoordDao();
        emptyJab();

        List<Woord> woorden = woordDao.getByCategorieId(categorie.getId());

        if(woorden.size() != 0){
            jab("Er zitten nog woorden in deze categorie. Verander de categorie van deze woorden eerst, of verwijder ze.");

            ThrowExceptionIfErrors(jab);
        }
    }

    @Transactional
    public static void update(String nieuweNaam) throws Exception {
        IWoordDao woordDao = new WoordDao();
        emptyJab();

       if(woordDao.getByName(nieuweNaam) != null){
           jab("Er bestaat al een categorie met deze naam.");

           ThrowExceptionIfErrors(jab);
       }
    }
}
