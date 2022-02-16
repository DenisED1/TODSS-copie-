package controllers;

import authentication.Administrator;
import authentication.Ingelogd;
import dao.Gebruiker.GebruikerDao;
import dao.Gebruiker.IGebruikerDao;
import formpackage.AbstractForm;
import formpackage.InlogGegevensForm;
import formpackage.PersoonsGegevensForm;
import formpackage.UserForm;
import model.Gebruiker;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import validators.AccountValidator;
import views.html.*;

import javax.json.Json;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Security.Authenticated(Ingelogd.class)
public class AccountController extends Controller {
    private static List<AbstractForm> userForms = new ArrayList<>();

    @Transactional
    public static Result accountOverzicht(){
        IGebruikerDao gebruikerDao = new GebruikerDao();
        Gebruiker gebruiker = gebruikerDao.getByUUID(session("uuid"));

        if(gebruiker.getFunctie().equals(Gebruiker.Functie.leraar))
        {
            return ok(main.render("Leerlingoverzicht", accountoverzicht.render(gebruikerDao.getAlleUsersVanBegeleider(gebruiker), dynamicmodal.render())));
        }

        if(gebruiker.getFunctie().equals(Gebruiker.Functie.administrator)){
            List<Gebruiker.Functie> functies = Arrays.asList(Gebruiker.Functie.administrator,Gebruiker.Functie.leraar);
            List<Gebruiker> gebruikers = gebruikerDao.getAlleUsersVanFuncties(functies);

            return ok(main.render("Accountoverzicht", accountoverzicht.render(gebruikers, dynamicmodal.render())));
        }

        return new Administrator().onUnauthorized(ctx());
    }

    @Transactional
    public static Result registerUser(){
        IGebruikerDao gebruikerDao = new GebruikerDao();
        Map<String, String[]> params = request().body().asMultipartFormData().asFormUrlEncoded();
        UserForm userForm = (UserForm) FormController.handleFormRequest(params, userForms);

        try {
            userForm.validate();
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }

        String uuid = Application.generateUUID();

        Gebruiker newGebruiker = new Gebruiker(
                userForm.getInlogGegevensForm().getGebruikersnaam(),
                get_SHA_512_SecurePassword(userForm.getInlogGegevensForm().getWachtwoord1(), uuid),
                userForm.getPersoonsGegevensForm().getVoornaam(),
                userForm.getPersoonsGegevensForm().getAchternaam(),
                FormController.parseDate(userForm.getPersoonsGegevensForm().getGeboortedatum()),
                userForm.getPersoonsGegevensForm().getKlas(),
                Gebruiker.Functie.valueOf(userForm.getPersoonsGegevensForm().getFunctie()),
                uuid
        );

        try {
            AccountValidator.registerUser(newGebruiker);
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }

        if(userForm.getPersoonsGegevensForm().getBegeleider() != null) {
            newGebruiker.setBegeleider(
                    gebruikerDao.getByUUID(userForm.getPersoonsGegevensForm().getBegeleider())
            );
        }

        gebruikerDao.insert(newGebruiker);
        userForms.remove(userForm);
        flash("success-message", "Gebruiker " + newGebruiker.getGebruikersnaam() + " is toegevoegd.");

        return redirectToAccountOverzicht();
    }

    @Transactional
    public static Result updateInlogGegevens(){
        IGebruikerDao gebruikerDao = new GebruikerDao();

        Map<String, String[]> params = request().body().asMultipartFormData().asFormUrlEncoded();
        InlogGegevensForm inlogGegevensForm = (InlogGegevensForm) FormController.handleFormRequest(params, userForms);

        try{
            inlogGegevensForm.validate();
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }

        Gebruiker gebruiker = gebruikerDao.getByUUID(inlogGegevensForm.getUuid());
        Gebruiker nieuweGebruiker = gebruiker;
        nieuweGebruiker.setGebruikersnaam(inlogGegevensForm.getGebruikersnaam());
        nieuweGebruiker.setWachtwoord(get_SHA_512_SecurePassword(inlogGegevensForm.getWachtwoord1(), inlogGegevensForm.getUuid()));

        try{
            AccountValidator.updateUser(nieuweGebruiker, gebruiker);
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }

        gebruikerDao.update(gebruiker);

        userForms.remove(inlogGegevensForm);
        flash("success-message", "De inloggegevens zijn bijgewerkt.");

        return redirectToAccountOverzicht();
    }

    @Transactional
    public static Result updatePersoonsgegevens(){
        IGebruikerDao gebruikerDao = new GebruikerDao();
        Map<String, String[]> params = request().body().asMultipartFormData().asFormUrlEncoded();
        PersoonsGegevensForm persoonsGegevensForm = (PersoonsGegevensForm) FormController.handleFormRequest(params, userForms);

        try{
            persoonsGegevensForm.validate();
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }

        Gebruiker gebruiker = gebruikerDao.getByUUID(persoonsGegevensForm.getUuid());
        gebruiker.setVoornaam(persoonsGegevensForm.getVoornaam());
        gebruiker.setAchternaam(persoonsGegevensForm.getAchternaam());
        gebruiker.setGeboortedatum(FormController.parseDate(persoonsGegevensForm.getGeboortedatum()));
        gebruiker.setKlas(persoonsGegevensForm.getKlas());

        gebruikerDao.update(gebruiker);

        userForms.remove(persoonsGegevensForm);
        flash("success-message", "De persoonsgegevens zijn bijgewerkt.");

        return redirectToAccountOverzicht();
    }

    @Transactional
    public static Result deleteUser(){
        IGebruikerDao gebruikerDao = new GebruikerDao();
        Map<String, String[]> params = request().body().asMultipartFormData().asFormUrlEncoded();
        PersoonsGegevensForm userForm = (PersoonsGegevensForm) FormController.handleFormRequest(params, userForms);

        gebruikerDao.remove(userForm.getUuid());

        userForms.remove(userForm);
        flash("success-message", "Gebruiker is succesvol verwijderd.");

        return redirectToAccountOverzicht();
    }

    public static String get_SHA_512_SecurePassword(String passwordToHash, String salt){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes("UTF-8"));
            byte[] bytes = md.digest(passwordToHash.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return generatedPassword;
    }

    private static Result redirectToAccountOverzicht(){
        return ok(Json.createObjectBuilder().add("redirect", routes.AccountController.accountOverzicht().url()).build().toString());
    }

    //region Modal handlers
    public static Result newLeraarModal(){
        UserForm userForm = new UserForm();

        userForm.getPersoonsGegevensForm().setFunctie(Gebruiker.Functie.leraar.name());
        userForms.add(userForm);

        return ok(dynamicmodalform.render("Leraar registreren", "/gebruiker/register", userForm.getInputFields()));
    }

    public static Result newAdminModal(){
        UserForm userForm = new UserForm();

        userForm.getPersoonsGegevensForm().setFunctie(Gebruiker.Functie.administrator.name());
        userForms.add(userForm);

        return ok(dynamicmodalform.render("Administrator registreren", "/gebruiker/register", userForm.getInputFields()));
    }

    public static Result newLeerlingModal(){
        UserForm userForm = new UserForm();

        userForm.getPersoonsGegevensForm().setFunctie(Gebruiker.Functie.leerling.name());
        userForm.getPersoonsGegevensForm().setBegeleider(session("uuid"));
        userForms.add(userForm);

        return ok(dynamicmodalform.render("Leerling registreren", "/gebruiker/register", userForm.getInputFields()));
    }

    public static Result updateInlogGegevensModal(){
        InlogGegevensForm inlogGegevensForm = new InlogGegevensForm();

        Map<String, String[]> params = request().body().asFormUrlEncoded();
        inlogGegevensForm.setGebruikersnaam(params.get("gebruikersnaam")[0]);
        inlogGegevensForm.setOudeGebruikersnaam(params.get("gebruikersnaam")[0]);
        inlogGegevensForm.setUuid(params.get("uuid")[0]);

        userForms.add(inlogGegevensForm);

        return ok(dynamicmodalform.render("Inloggegevens", "/gebruiker/inloggegevens/update", inlogGegevensForm.getInputFields()));
    }

    public static Result updatePersoonsGegevensModal(){
        PersoonsGegevensForm persoonsGegevensForm = new PersoonsGegevensForm();
        Map<String, String[]> params = request().body().asFormUrlEncoded();

        persoonsGegevensForm.setVoornaam(params.get("voornaam")[0]);
        persoonsGegevensForm.setAchternaam(params.get("achternaam")[0]);
        persoonsGegevensForm.setKlas(params.get("klas")[0]);
        persoonsGegevensForm.setGeboortedatum(params.get("geboortedatum")[0]);
        persoonsGegevensForm.setUuid(params.get("uuid")[0]);

        userForms.add(persoonsGegevensForm);

        return ok(dynamicmodalform.render("Persoonsgegevens", "/gebruiker/persoonsgegevens/update", persoonsGegevensForm.getInputFields()));
    }

    @Transactional
    public static Result deleteUserModal(){
        IGebruikerDao gebruikerDao = new GebruikerDao();

        PersoonsGegevensForm userForm = new PersoonsGegevensForm();
        Map<String, String[]> params = request().body().asFormUrlEncoded();
        String message = "de gebruiker " + params.get("gebruikersnaam")[0];

        userForm.setUuid(params.get("uuid")[0]);
        userForms.add(userForm);

        if(gebruikerDao.getByUUID(userForm.getUuid()).getFunctie().equals(Gebruiker.Functie.leraar)) {
            message += " met alle bijhorende leerlingen";
        }

        return ok(dynamicmodaldelete.render(message, "/gebruiker/delete", userForm.getInputFields()));
    }
    //endregion
}
