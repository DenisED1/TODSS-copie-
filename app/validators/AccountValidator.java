package validators;

import dao.Gebruiker.GebruikerDao;
import dao.Gebruiker.IGebruikerDao;
import model.Gebruiker;
import play.db.jpa.Transactional;

import javax.json.Json;
import javax.json.JsonArrayBuilder;

import static controllers.AccountController.get_SHA_512_SecurePassword;

public class AccountValidator extends Validator{
    @Transactional
    public static void login(String gebruikersnaam, String wachtwoord, Gebruiker gebruiker) throws Exception {
        emptyJab();

        //Validate user
        if(gebruiker == null){
            jab("Gebruikersnaam: '" + gebruikersnaam +  "' bestaat niet.");
            ThrowExceptionIfErrors(jab);
        }

        if(!get_SHA_512_SecurePassword(wachtwoord, gebruiker.getUuid()).equals(gebruiker.getWachtwoord())) {
            jab("Verkeerde inlog gegevens.");
        }

        ThrowExceptionIfErrors(jab);
    }

    @Transactional
    public static void registerUser(Gebruiker nieuweGebruiker) throws Exception {
        IGebruikerDao gebruikerDao = new GebruikerDao();
        emptyJab();

        if(gebruikerDao.getByUsername(nieuweGebruiker.getGebruikersnaam()) != null)
        {
            jab("Er is al een gebruiker met deze gebruikersnaam");
        }

        ThrowExceptionIfErrors(jab);
    }

    @Transactional
    public static void updateUser(Gebruiker nieuweGebruiker, Gebruiker gebruiker) throws Exception {
        IGebruikerDao gebruikerDao = new GebruikerDao();
        emptyJab();

        if(!gebruiker.getGebruikersnaam().equals(nieuweGebruiker.getGebruikersnaam()))
        {
            if(gebruikerDao.getByUsername(nieuweGebruiker.getGebruikersnaam()) != null){
                jab("Er is al een gebruiker met deze gebruikersnaam");
            }
        }

        ThrowExceptionIfErrors(jab);
    }
}
