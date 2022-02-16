package controllers;

import authentication.Administrator;
import dao.SpellingRegel.ISpellingRegelDao;
import dao.SpellingRegel.SpellingRegelDao;
import formpackage.AbstractForm;
import formpackage.SpellingregelForm;
import model.SpellingRegel;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import validators.SpellingOpdrachtValidator;
import views.html.*;

import javax.json.Json;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Security.Authenticated(Administrator.class)
public class SpellingregelController extends Controller {
    private static List<AbstractForm> spellingregelForms = new ArrayList<>();

    @Transactional
    public static Result selecteren(){
        ISpellingRegelDao spellingRegelDao = new SpellingRegelDao();

        List<SpellingRegel> allSpellingRegels = spellingRegelDao.getAllSpellingRegelsEmpty();
        return ok(
                main.render(
                        "SpellingRegel selecteren",
                        spellingregelselecteren.render(
                                allSpellingRegels,
                                dynamicmodal.render()
                        )
                )
        );
    }

    @Transactional
    public static Result createSpellingregel(){
        ISpellingRegelDao spellingRegelDao = new SpellingRegelDao();
        Http.MultipartFormData body = request().body().asMultipartFormData();
        SpellingregelForm spellingregelForm;

        try {
            spellingregelForm = (SpellingregelForm) FormController.handleFormRequest(body.asFormUrlEncoded(), spellingregelForms);
            spellingregelForm.validate();
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }

        if(spellingregelForm == null) {
            return badRequest();
        }

        SpellingRegel spellingRegel = new SpellingRegel(spellingregelForm.getSpellingregelnaam());

        try {
            SpellingOpdrachtValidator.nieuwSpellingRegel(spellingRegel);
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }

        spellingRegelDao.insert(spellingRegel);

        flash("spellingregelnaam",spellingregelForm.getSpellingregelnaam());
        flash("success-message", "SpellingRegel " + spellingregelForm.getSpellingregelnaam() + " is succesvol aangemaakt.");

        spellingregelForms.remove(spellingregelForm);

        return redirectToBeheer();
    }

    @Transactional
    public static Result deleteSpellingregel(){
        ISpellingRegelDao spellingRegelDao = new SpellingRegelDao();
        Http.MultipartFormData body = request().body().asMultipartFormData();
        SpellingregelForm spellingregelForm = null;

        try {
            spellingregelForm = (SpellingregelForm) FormController.handleFormRequest(body.asFormUrlEncoded(), spellingregelForms);
        } catch (Exception e) {
            badRequest(e.getMessage());
        }

        if(spellingregelForm == null){
            return badRequest();
        }

        SpellingRegel spellingRegel = spellingRegelDao.getByName(spellingregelForm.getSpellingregelnaam());
        spellingRegelDao.remove(spellingRegel);

        flash("success-message", "SpellingRegel " + spellingregelForm.getSpellingregelnaam() + " is succesvol verwijderd.");

        spellingregelForms.remove(spellingregelForm);

        return redirectToBeheer();
    }

    @Transactional
    private static Result beheer(String spellingregelnaam){
        ISpellingRegelDao spellingRegelDao = new SpellingRegelDao();

        SpellingRegel spellingRegel = spellingRegelDao.getByName(spellingregelnaam);

//        spellingRegel.setUitleg();
        if(spellingRegel == null){
            return redirect(routes.SpellingregelController.selecteren());
        }

        return ok(
                main.render("SpellingRegel beheren",
                    spellingregelbeheren.render(
                        "spellingregelbeheren",
                        spellingRegel,
                        dynamicmodal.render())
                )
        );
    }

    @Transactional
    public static Result korteUitlegOpslaan(){
        ISpellingRegelDao spellingRegelDao = new SpellingRegelDao();
        Map<String, String[]> params = request().body().asMultipartFormData().asFormUrlEncoded();

        String korteuitleg = params.get("korteuitleg")[0];
        String spellingregelnaam = params.get("spellingRegel")[0];

        SpellingRegel spellingRegel = spellingRegelDao.getByName(spellingregelnaam);
        spellingRegel.setKorteUitleg(korteuitleg);

        SpellingRegel result = spellingRegelDao.update(spellingRegel) ;

        return ok(result != null ? "De korteuitleg is succesvol geupdate." : "Er is wat foutgegaan met de korte uitleg updaten");
    }

    @Transactional
    public static Result opdrachtUitlegOpslaan(){
        ISpellingRegelDao spellingRegelDao = new SpellingRegelDao();
        Map<String, String[]> params = request().body().asMultipartFormData().asFormUrlEncoded();

        String opdrachtuitleg = params.get("opdrachtuitleg")[0];
        String spellingregelnaam = params.get("spellingRegel")[0];

        SpellingRegel spellingRegel = spellingRegelDao.getByName(spellingregelnaam);
        spellingRegel.setOpdrachtUitleg(opdrachtuitleg);

        SpellingRegel result = spellingRegelDao.update(spellingRegel) ;

        return ok(result != null ? "De opdracht uitleg is succesvol geupdate." : "Er is wat foutgegaan met de opdracht uitleg updaten");
    }

    @Transactional
    public static Result getBeheer(){
        return beheer(flash("spellingregelnaam"));
    }

    @Transactional
    public static Result beheer(){
        String spellingregelnaam = request().body().asMultipartFormData().asFormUrlEncoded().get("spellingregelnaam")[0];

        return beheer(spellingregelnaam);
    }

    public static Result redirectToBeheer(){
        return ok(Json.createObjectBuilder().add("redirect", routes.SpellingregelController.getBeheer().url()).build().toString());
    }

    //region Modal handlers
    public static Result createSpellingregelModal(){
        SpellingregelForm spellingregelForm = new SpellingregelForm();
        spellingregelForms.add(spellingregelForm);

        return ok(dynamicmodalform.render("SpellingRegel toevoegen", "/spellingregel/create", spellingregelForm.getInputFields()));
    }

    public static Result deleteSpellingregelModal(){
        Map<String, String[]> params = request().body().asFormUrlEncoded();

        SpellingregelForm spellingregelForm = new SpellingregelForm();
        spellingregelForm.setSpellingregelnaam(params.get("spellingregelnaam")[0]);

        spellingregelForms.add(spellingregelForm);

        return ok(dynamicmodaldelete.render("de spellingregel " + spellingregelForm.getSpellingregelnaam() ,"/spellingregel/delete", spellingregelForm.getInputFields()));
    }

    //endregion
}
