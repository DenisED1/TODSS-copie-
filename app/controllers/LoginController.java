package controllers;

import authentication.NietIngelogd;
import dao.Gebruiker.GebruikerDao;
import dao.Gebruiker.IGebruikerDao;
import formpackage.AbstractForm;
import formpackage.LoginForm;
import model.Gebruiker;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import validators.AccountValidator;
import views.html.login;
import views.html.main;

import javax.json.Json;
import javax.json.JsonArray;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Security.Authenticated(NietIngelogd.class)
public class LoginController extends Controller {
    private static IGebruikerDao gebruikerDao = new GebruikerDao();
    private static List<AbstractForm> loginForms = new ArrayList<>();

    @Transactional
    public static Result login(){
//        Gebruiker leerling = new Gebruiker("AdminDeEngh", "f2fae4cdd3cb5090af279eeba3512d13922cbbf824a78f5f1e9da5f73207d1a905cc4687dd34ecc0834d0901eab08d73950cccc59f9a9c51dc5fb6732f57caf5"," voornaam", "achternaam", new Date(), "Klas", Gebruiker.Functie.administrator, "ol1tsi4j9bk4326c0867mlimb9");
//        gebruikerDao.insert(leerling);

        //Validate form
        Map<String, String[]> params = request().body().asMultipartFormData().asFormUrlEncoded();
        LoginForm loginForm = (LoginForm) FormController.handleFormRequest(params, loginForms);

        try {
            loginForm.validate();
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }

        Gebruiker gebruiker = gebruikerDao.getByUsername(loginForm.getGebruikersnaam());

        try {
            AccountValidator.login(loginForm.getGebruikersnaam(), loginForm.getWachtwoord(), gebruiker);
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }

        session("gebruikerUuid", gebruiker.getUuid());
        session("voornaam", gebruiker.getVoornaam());
        session("achternaam", gebruiker.getAchternaam());
        session("geboortedatum", FormController.DateToString(gebruiker.getGeboortedatum()));
        session("klas", gebruiker.getKlas());
        session("functie", gebruiker.getFunctie().name());
        session("uuid", gebruiker.getUuid());

        loginForms.remove(loginForm);

        return ok(Json.createObjectBuilder().add("redirect", routes.Application.home().url()).build().toString());
    }

    public static Result loginform(){
        LoginForm loginForm  = new LoginForm();
        loginForm.setGebruikersnaam("gebruikersnaam");
        loginForm.setWachtwoord("wachtwoord");

        loginForms.add(loginForm);

        return ok(main.render("Login",login.render(loginForm.getInputFields())));
    }

}
